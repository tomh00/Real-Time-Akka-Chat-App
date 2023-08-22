package chatapp
package auth

import akka.actor.{ActorRef, ActorSystem, Props}
import chatapp.actors.{InitializeUserActor, UserActor}
import chatapp.models.User

import scala.collection.mutable

class UserManager ( system : ActorSystem ) {
  private val temporaryUserData : mutable.Map[ String, String ] = mutable.Map()
  private val loggedInUsers : mutable.Map[ String, User ] = mutable.Map()
  private val passwords : mutable.Map[ User, String ] = mutable.Map()

  def registerUser( username: String, password: String ): Option[ User ] = {
    // Ensure user doesn't exist
    if ( !temporaryUserData.contains( username ) ) {
      val hashedPassword = PasswordHasher.hashPassword( password ) // Hash the password
      val user = instantiateUser( username )
      loggedInUsers.addOne( user.sessionId, user )
      temporaryUserData.addOne( username, hashedPassword )
      Some( user ) // Return the newly created user as Some
    } else {
      None // Return None if the user already exists
    }
  }


  def authenticateUser( username: String, password: String ) : Option[ User ] = {
    temporaryUserData.get( username ) match {
      case Some( hashedPassword ) =>
        if ( PasswordHasher.checkPassword( password, hashedPassword ) ) {
          val user = instantiateUser( username )
          loggedInUsers.addOne( user.sessionId, user )
          Some( user )
        }
        else {
          None
        }
      case None => None
    }
  }

  private def instantiateUser( username : String ) : User = {
    val userActor : ActorRef = system.actorOf( Props[ UserActor ] )
    val sessionId : String = TokenUtility.generateToken( username )
    userActor ! InitializeUserActor( sessionId )
    User( username, userActor, sessionId )
  }

  def getLoggedInUsers : mutable.Map[ String, User ] = loggedInUsers
}
