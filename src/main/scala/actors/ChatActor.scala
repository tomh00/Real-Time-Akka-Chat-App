package chatapp
package actors

import akka.actor.{ Actor, ActorLogging }

class ChatActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case message: String =>
      // TODO: Handle incoming messages here
      log.info( s"Received message: $message" )
  }

}
