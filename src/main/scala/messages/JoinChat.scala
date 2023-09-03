package chatapp
package messages

import models.User

import akka.actor.ActorRef

case class JoinChat( private val user : User ) {
  def getUser : User = user
}
