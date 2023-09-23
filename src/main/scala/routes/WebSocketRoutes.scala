// This code is based on the akka-websockets-demo repository by amdelamar.
// Original repository: https://github.com/amdelamar/akka-websockets-demo

package chatapp
package routes

import auth.UserManager
import messages.{ AddWebSocket, ChatMessage }
import models.User

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{ Flow, Keep, Sink, Source }
import org.reactivestreams.Publisher

class WebSocketRoutes( implicit system : ActorSystem ) {

  def websocketChatMessageRoute( userManager : UserManager ) : Route =
    path( "ws" ) {
      parameter( "token", "room" ) { ( userSessionToken, chatRoom ) =>
        // find the relevant chatroom actor
        userManager.getLoggedInUsersByToken.get( userSessionToken ) match {
          case Some( user ) =>
            if ( user.getChatRooms.contains( chatRoom ) ) {
              val actor = user.getChatRooms( chatRoom )
              handleWebSocketMessages( websocketChatMessageFlow( actor, user ) )
            }
            else {
              complete( StatusCodes.NotFound -> "Chat room not found" )
            }
        }
      }
    }

  private def websocketChatMessageFlow( chatActor : ActorRef, user : User ) : Flow[ Message, Message, Any ] = {
    val (actorRef : ActorRef, publisher : Publisher[ TextMessage.Strict ]) = createWebSocketSourceAndActorRef
    user.getRef ! AddWebSocket( "ChatMessageSocket", actorRef )

    // set up sink
    val sink : Sink[ Message, Any ] = Flow[ Message ]
      .map {
        case TextMessage.Strict( msg ) =>
          // Incoming message from ws
          chatActor ! ChatMessage( user.getUserName, msg )
      }
      .to( Sink.onComplete( _ =>
        complete( "Chat room WebSocket connection closed." )
      ) )

    Flow.fromSinkAndSource( sink, Source.fromPublisher( publisher ) )
  }

  def websocketChatListRoute( userManager : UserManager ) : Route = {
    path( "ws-chat-list" ) {
      parameter( "token" ) { userSessionToken =>
        // find the relevant chatroom actor
        userManager.getLoggedInUsersByToken.get( userSessionToken ) match {
          case Some( user ) =>
            handleWebSocketMessages( websocketChatListFlow( user ) )
          case None =>
            complete( StatusCodes.NotFound -> "User not found" )
        }
      }
    }
  }

  def websocketChatListFlow( user : User ) : Flow[ Message, Message, Any ] = {
    val (actorRef : ActorRef, publisher : Publisher[ TextMessage.Strict ]) = createWebSocketSourceAndActorRef
    user.getRef ! AddWebSocket( "ChatListSocket", actorRef )
    Flow.fromSinkAndSource( Sink.ignore, Source.fromPublisher( publisher ) )
  }

  private def createWebSocketSourceAndActorRef : (ActorRef, Publisher[ TextMessage.Strict ]) = {
    val (actorRef : ActorRef, publisher : Publisher[ TextMessage.Strict ]) =
      Source.actorRef[ String ]( 16, OverflowStrategy.fail )
        .map( msg => TextMessage.Strict( msg ) )
        .toMat( Sink.asPublisher( false ) )( Keep.both )
        .run()

    (actorRef, publisher)
  }
}
