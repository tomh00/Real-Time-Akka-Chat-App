// This code is based on the akka-websockets-demo repository by amdelamar.
// Original repository: https://github.com/amdelamar/akka-websockets-demo


package chatapp
package routes

import auth.UserManager
import messages.{ AddWebSocket, ChatMessage }
import models.User

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{ Flow, Keep, Sink, Source }
import org.reactivestreams.Publisher

class WebSocketRoutes( implicit system : ActorSystem ) {

  def websocketChatMessageRoute( userManager : UserManager, chatActor : ActorRef ) : Route =
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
              complete( "no chat room found" )
            }
        }
      }
    }


  private def websocketChatMessageFlow( chatActor : ActorRef, user : User ) : Flow[ Message, Message, Any ] = {
    // set up a source
    val (actorRef : ActorRef, publisher : Publisher[ TextMessage.Strict ]) =
      Source.actorRef[ String ]( 16, OverflowStrategy.fail )
        .map( msg => TextMessage.Strict( msg ) )
        .toMat( Sink.asPublisher( false ) )( Keep.both ).run()


    // send the websocket to the user actor for returning messages
    user.getRef ! AddWebSocket( "ChatMessageSocket", actorRef )

    // set up sink
    val sink : Sink[ Message, Any ] = Flow[ Message ]
      .map {
        case TextMessage.Strict( msg ) =>
          // Incoming message from ws
          chatActor ! ChatMessage( user.getUserName, msg )
      }
      .to( Sink.onComplete( _ =>
        // Announce the user has left
        //chatActor ! LeaveChat( user.getUserName )
        complete( "hi" )
      ) )

    // Pair sink and source
    Flow.fromSinkAndSource( sink, Source.fromPublisher( publisher ) )
  }

  def websocketChatListRoute( userManager : UserManager ) : Route = {
    path( "ws-chat-list" ) {
      parameter( "token" ) { userSessionToken =>
        // find the relevant chatroom actor
        userManager.getLoggedInUsersByToken.get( userSessionToken ) match {
          case Some( user ) =>
            handleWebSocketMessages( websocketChatListFlow( user ) )
        }
      }
    }
  }

  def websocketChatListFlow( user : User ) : Flow[ Message, Message, Any ] = {
    // set up a source
    val (actorRef : ActorRef, publisher : Publisher[ TextMessage.Strict ]) =
      Source.actorRef[ String ]( 16, OverflowStrategy.fail )
        .map( msg => TextMessage.Strict( msg ) )
        .toMat( Sink.asPublisher( false ) )( Keep.both ).run()


    // send the websocket to the user actor for returning messages
    user.getRef ! AddWebSocket( "ChatListSocket", actorRef )

    // set up sink
    val sink : Sink[ Message, Any ] = Flow[ Message ]
      .map {
        case TextMessage.Strict( msg ) =>
        // Incoming message from ws
        // chatActor ! ChatMessage( user.getUserName, msg )
      }
      .to( Sink.onComplete( _ =>
        // Announce the user has left
        //chatActor ! LeaveChat( user.getUserName )
        complete( "hi" )
      ) )

    // Pair sink and source
    Flow.fromSinkAndSource( sink, Source.fromPublisher( publisher ) )
  }
}
