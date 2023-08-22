package chatapp
package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.model.ws.TextMessage
import chatapp.messages.{AddChatActor, ChatMessage}
import routes.WebSocketRoutes

import scala.collection.mutable

class UserActor extends Actor with ActorLogging {
  private val webSocketActors: mutable.Map[ String, ActorRef ] = mutable.Map.empty[ String, ActorRef ]

  override def receive: Receive = {
    case ChatMessage(username, message) =>

      webSocketActors.get( "the-chat" ) match {
        case Some( socket ) =>
          socket ! s"$username: $message"
      }

    case AddChatActor( chatName, actor ) =>
      webSocketActors.addOne( chatName, actor )
  }

  /*def setUserWebSocket( userWebSocket : ActorRef ) : Unit = {
    this.userWebSocket = userWebSocket
  }*/
}

case class InitializeUserActor( sessionId : String )
