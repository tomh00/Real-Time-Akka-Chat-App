package chatapp
package messages

import models.User

import akka.actor.ActorRef

case class JoinChat( private val user : User, private val socketActor : ActorRef ) {
  def getUser : User = user
  def getSocketActor : ActorRef = socketActor
}
