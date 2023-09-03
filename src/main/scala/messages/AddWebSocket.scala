package chatapp
package messages

import akka.actor.ActorRef

case class AddWebSocket( chatName : String, socketActor : ActorRef )
