package chatapp
package actors

import akka.actor.{Actor, ActorLogging }
import chatapp.messages.{ChatMessage, StartListening}

class UserInputActor extends Actor with ActorLogging {
  override def receive: Receive = {
    // Note: This actor is currently using console input for testing purposes.
    // In the future, it will be replaced with GUI interaction.

    case StartListening( targetActor ) =>
        while ( true ) {
          val input = scala.io.StdIn.readLine( "Enter your input: " )

          targetActor ! ChatMessage( "tomh00", input )
        }
  }
}
