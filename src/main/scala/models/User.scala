package chatapp
package models

import akka.actor.ActorRef
import java.util.UUID

case class User( val userName : String, private val actorRef : ActorRef ) {
  val id: String = UUID.randomUUID().toString

  def getUserName : String = userName
  def getRef : ActorRef = actorRef
}
