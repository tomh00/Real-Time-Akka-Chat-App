package chatapp
package messages

import models.User

case class LeaveChat( private val user : String ) {
  def getUsername : String = user

}
