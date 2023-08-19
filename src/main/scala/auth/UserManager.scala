package chatapp
package auth

import akka.actor.{ActorSystem, Props}
import chatapp.actors.UserActor
import chatapp.models.User
import scala.collection.mutable

class UserManager ( system : ActorSystem ) {
  val users : mutable.Map[ String, User ] = mutable.Map()
  private val passwords : mutable.Map[ User, String ] = mutable.Map()

  def registerUser( userName : String, password : String ): Boolean = {
    // ensure user doesn't exist
    if ( !users.contains( userName ) ) {
      val hashedPassword = PasswordHasher.hashPassword( password ) // hash the password
      val user = User( userName, system.actorOf( Props[ UserActor ] ) )

      users.addOne( userName, user )
      passwords.addOne( user, hashedPassword )

      true
    }
    else {
      false
    }
  }

  def authenticateUser( userName : String, password : String ) : Boolean = {
    users.get( userName ) match {
      case Some( user ) =>
        passwords.get( user ) match {
          case Some( hashedPassword ) =>
            PasswordHasher.checkPassword(password, hashedPassword )

        }
      case None => false
    }
  }

}
