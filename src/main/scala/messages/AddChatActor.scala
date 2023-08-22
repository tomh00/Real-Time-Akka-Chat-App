package chatapp
package messages

import akka.actor.ActorRef

case class AddChatActor( chatName : String, actor : ActorRef )
