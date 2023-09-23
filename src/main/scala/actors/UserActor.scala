package chatapp
package actors

import messages.{ AddWebSocket, ChatMessage, UpdateChatList }
import akka.actor.{ Actor, ActorLogging, ActorRef }
import scala.collection.mutable

class UserActor extends Actor with ActorLogging {
  // Stores actor references to the user's corresponding web sockets.
  private val webSocketActors : mutable.Map[ String, ActorRef ] = mutable.Map.empty[ String, ActorRef ]

  override def receive : Receive = {
    // Forward chat messages to the ChatMessageSocket.
    case ChatMessage( username, message ) =>
      forwardToWebSocket( "ChatMessageSocket", s"$username: $message" )

    // Notify the ChatListSocket to update the chat list.
    case UpdateChatList( _ ) =>
      forwardToWebSocket( "ChatListSocket", "Updating chat list" )

    // Store an actor reference to the user's corresponding web socket.
    case AddWebSocket( websocketName, actor ) =>
      webSocketActors.addOne( websocketName, actor )
  }

  private def forwardToWebSocket( websocketName : String, message : String ) : Unit = {
    webSocketActors.get( websocketName ) match {
      case Some( socket ) =>
        socket ! message
      case None =>
        log.warning( s"WebSocket $websocketName not found for message: $message" )
    }
  }
}

