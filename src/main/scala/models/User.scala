package chatapp.models

import akka.actor.ActorRef

case class User( userName : String, private val actorRef : ActorRef, sessionId : String ) {
  def getUserName : String = userName
  def getRef : ActorRef = actorRef
  def getSessionId : String = sessionId

}
