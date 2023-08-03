package chatapp
package messages

import models.User

case class JoinChat( private val user : User ) {
  def getUser : User = user
}
