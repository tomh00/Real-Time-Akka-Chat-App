package chatapp
package actors

import akka.actor.{Actor, ActorLogging, Props}
import akka.stream.scaladsl.{Sink, Source}
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer, OverflowStrategy}

import scala.io.StdIn
import akka.actor.Props
import chatapp.messages.{ChatMessage, StartListening}

class UserInputActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case StartListening( targetActor ) =>
        while ( true ) {
          val input = scala.io.StdIn.readLine( "Enter your input: " )

          targetActor ! ChatMessage( "tomh00", input )
        }
  }
}
