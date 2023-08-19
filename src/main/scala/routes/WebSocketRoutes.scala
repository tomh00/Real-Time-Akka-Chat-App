package chatapp
package routes

import akka.actor.ActorRef
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Flow
import chatapp.actors.ChatActor
import chatapp.messages.ChatMessage
import chatapp.auth.UserManager

object WebSocketRoutes {
  def websocketRoute( userManager: UserManager, chatActor: ActorRef ) : Route =
    path ( "ws" ) {
      parameter( "token" ) { userSessionToken =>
        handleWebSocketMessages( websocketFlow( userSessionToken, chatActor, userManager  ) )
      }
    }

  private def websocketFlow( userSessionToken : String, chatActor: ActorRef, userManager: UserManager ) : Flow[ Message, Message, Any ] = {
    Flow[ Message ].map {
      case TextMessage.Strict( text ) =>
        // Convert the incoming message to a ChatMessage
        userManager.getLoggedInUsers.get( userSessionToken ) match {
          case Some( user ) =>
            val chatMessage = ChatMessage( user.getUserName, text )
            // Send the chatMessage to the ChatActor for broadcasting
            chatActor ! chatMessage

          case None =>
            TextMessage( "No User Found" )
        }

        TextMessage( s"Received: $text" )
      case _ =>
        TextMessage( "Invalid message format" )
    }
  }

}
