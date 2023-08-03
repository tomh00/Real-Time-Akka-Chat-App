package chatapp
package messages

case class JoinChat( private val username : String ) {
  def getUsername : String = username
}
