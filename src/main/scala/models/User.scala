package chatapp
package models

import java.util.UUID

case class User( val userName : String ) {
  val id: String = UUID.randomUUID().toString

  def getUserName : String = userName
}
