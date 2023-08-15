package chatapp
package messages

import akka.actor.ActorRef

case class StartListening( targetActor : ActorRef )
