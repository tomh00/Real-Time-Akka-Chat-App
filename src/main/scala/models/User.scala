package chatapp.models

import akka.actor.ActorRef

case class User( val userName : String, private val actorRef : ActorRef ) {
  def getUserName : String = userName
  def getRef : ActorRef = actorRef
}
