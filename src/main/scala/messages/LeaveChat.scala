package chatapp
package messages

import models.User

case class LeaveChat( private val user : User ) {
  def getUser : User = user

}
