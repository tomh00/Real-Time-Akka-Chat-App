package chatapp
package messages

import models.User

import akka.actor.ActorRef

case class JoinChat( private val username : String ) {
  def getUsername : String = username
}
