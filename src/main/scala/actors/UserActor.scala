package chatapp
package actors

import akka.actor.{Actor, ActorLogging}
import chatapp.messages.ChatMessage

class UserActor extends Actor with ActorLogging{
  override def receive: Receive = {
    case ChatMessage( userName, message ) =>
      log.info( s"i am $self and i received this message from $userName: $message ")

  }
}
