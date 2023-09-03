package chatapp
package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import chatapp.messages.{AddChatActor, AddWebSocket, ChatMessage}

import scala.collection.mutable

class UserActor extends Actor with ActorLogging {
  private val webSocketActors : mutable.Map[ String, ActorRef ] = mutable.Map.empty[ String, ActorRef ]
  private val chatRooms : mutable.Set[ ActorRef ] = mutable.Set.empty

  override def receive: Receive = {
    case ChatMessage( username, message ) =>
      webSocketActors.get( "theWebSocket" ) match {
        case Some( socket ) =>
          socket ! s"$username: $message"
      }

    case AddChatActor( chatActor ) =>
      chatRooms.addOne( chatActor )

    case AddWebSocket( chatName, actor ) =>
      webSocketActors.addOne( chatName, actor )
  }

  def getChatRooms : mutable.Set[ ActorRef ] = chatRooms
}

case class InitializeUserActor( sessionId : String )
