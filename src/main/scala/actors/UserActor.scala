package chatapp
package actors

import messages.{ AddChatActor, AddWebSocket, ChatMessage, UpdateChatList }

import akka.actor.{ Actor, ActorLogging, ActorRef }
import akka.http.scaladsl.model.ws.TextMessage

import scala.collection.mutable

class UserActor extends Actor with ActorLogging {
  private val webSocketActors : mutable.Map[ String, ActorRef ] = mutable.Map.empty[ String, ActorRef ]
  private val chatRooms : mutable.Set[ ActorRef ] = mutable.Set.empty

  override def receive : Receive = {
    case ChatMessage( username, message ) =>
      webSocketActors.get( "ChatMessageSocket" ) match {
        case Some( socket ) =>
          socket ! s"$username: $message"
      }

    case UpdateChatList( username ) =>
      println(s"${self.toString()} is getting sent an update message")
      webSocketActors.get( "ChatListSocket" ) match {
        case Some( socket ) =>
          socket ! "Updating chat list"
      }

    case AddChatActor( chatActor ) =>
      chatRooms.addOne( chatActor )

    case AddWebSocket( websocketName, actor ) =>
      webSocketActors.addOne( websocketName, actor )

  }

  def getChatRooms : mutable.Set[ ActorRef ] = chatRooms
}

case class InitializeUserActor( sessionId : String )
