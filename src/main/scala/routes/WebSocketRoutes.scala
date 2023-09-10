// This code is based on the akka-websockets-demo repository by amdelamar.
// Original repository: https://github.com/amdelamar/akka-websockets-demo


package chatapp
package routes

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import chatapp.actors.ChatActor
import chatapp.messages.{AddWebSocket, ChatMessage, JoinChat, LeaveChat}
import chatapp.auth.UserManager
import chatapp.models.User
import org.reactivestreams.Publisher

class WebSocketRoutes( implicit system : ActorSystem ) {

  def websocketRoute( userManager : UserManager, chatActor : ActorRef ) : Route =
    path ( "ws" ) {
      parameter( "token", "room" ) { ( userSessionToken, chatRoom ) =>
        // find the relevant chatroom actor
        userManager.getLoggedInUsersByToken.get( userSessionToken ) match {
          case Some( user ) =>

            if ( user.getChatRooms.contains( chatRoom ) ) {
              val actor = user.getChatRooms( chatRoom )
              handleWebSocketMessages(websocketFlow( actor, user ) )
            }
            else {
              complete( "no chat room found" )
            }
        }
      }
    }


  private def websocketFlow( chatActor : ActorRef, user : User ) : Flow[ Message, Message, Any ] = {
    // set up a source
    val ( actorRef: ActorRef, publisher: Publisher[ TextMessage.Strict ] ) =
      Source.actorRef[ String ] ( 16, OverflowStrategy.fail )
        .map ( msg => TextMessage.Strict ( msg ) )
        .toMat ( Sink.asPublisher ( false ) ) ( Keep.both ).run()


    // send the websocket to the user actor for returning messages
    user.getRef ! AddWebSocket( "theWebSocket", actorRef )

    // set up sink
    val sink: Sink[ Message, Any ] = Flow[ Message ]
      .map {
        case TextMessage.Strict ( msg ) =>
          // Incoming message from ws
          chatActor ! ChatMessage( user.getUserName, msg )
      }
      .to (Sink.onComplete (_ =>
      // Announce the user has left
      chatActor ! LeaveChat( user.getUserName )
    ) )

    // Pair sink and source
    Flow.fromSinkAndSource( sink, Source.fromPublisher( publisher ) )
  }
}
