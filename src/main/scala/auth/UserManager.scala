package chatapp
package auth

import akka.actor.{ActorSystem, Props}
import chatapp.actors.UserActor
import chatapp.models.User
import scala.collection.mutable

class UserManager ( system : ActorSystem ) {
  private val temporaryUserData : mutable.Map[ String, String ] = mutable.Map()
  private val loggedInUsers : mutable.Map[ String, User ] = mutable.Map()
  private val passwords : mutable.Map[ User, String ] = mutable.Map()

  def registerUser(username : String, password : String ): Boolean = {
    // ensure user doesn't exist
    if ( !temporaryUserData.contains( username ) ) {
      val hashedPassword = PasswordHasher.hashPassword( password ) // hash the password
      loggedInUsers.addOne( username, instantiateUser( username ) )
      temporaryUserData.addOne( username, hashedPassword )
      true
    }
    else {
      false
    }
  }

  def authenticateUser(username: String, password: String ): Option[ User ] = {
    temporaryUserData.get( username ) match {
      case Some( hashedPassword ) =>
        if ( PasswordHasher.checkPassword( password, hashedPassword ) ) {
          Some( instantiateUser( username ) )
        }
        else {
          None
        }
      case None => None
    }
  }

  private def instantiateUser( username : String ) : User = {
    User( username, system.actorOf( Props[ UserActor ] ), TokenUtility.generateToken( username ) )
  }

}
