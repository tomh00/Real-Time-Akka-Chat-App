package chatapp
package actors

import akka.actor.{Actor, ActorLogging}
import chatapp.messages.{ChatMessage, JoinChat, LeaveChat}

class ChatActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case JoinChat( username ) =>
      // TODO: Handle join chat messages here
      log.info( s"Join message from: $username" )
      sender() ! username + " joined the chat."

    case LeaveChat( username ) =>
      // TODO: Handle leave chat messages here
      log.info( s"Leave message from: $username" )
      sender() ! username + " left the chat."

    case ChatMessage( username, message ) =>
      // TODO: Handle chat messages here
      log.info( s"New message from: $username. \nMessage: $message" )
      sender() ! username + ": " + message
  }

}
