package chatapp
package auth

import actors.UserActor
import models.User

import akka.actor.{ ActorRef, ActorSystem, Props }

import scala.collection.mutable

class UserManager( system : ActorSystem ) {
  private val users : mutable.Set[ String ] = mutable.Set()
  private val temporaryUserData : mutable.Map[ String, String ] = mutable.Map()
  private val loggedInUserTokens : mutable.Map[ String, User ] = mutable.Map()
  private val loggedInUsers : mutable.Map[ String, User ] = mutable.Map()
  private val userToChatRooms : mutable.Map[ String, mutable.Set[ Map[ String, ActorRef ] ] ] = mutable.Map()
  private val passwords : mutable.Map[ User, String ] = mutable.Map()

  def registerUser( username : String, password : String ) : Option[ User ] = {
    // Ensure user doesn't exist
    if ( !temporaryUserData.contains( username ) ) {
      val hashedPassword = PasswordHasher.hashPassword( password ) // Hash the password
      val user = instantiateUser( username )


      // temporary local storage of user info until external DB support is implemented
      loggedInUserTokens.addOne( user.sessionId, user )
      loggedInUsers += ( username -> user )
      temporaryUserData.addOne( username, hashedPassword )
      users += username


      Some( user ) // Return the newly created user as Some
    } else {
      None // Return None if the user already exists
    }
  }


  def authenticateUser( username : String, password : String ) : Option[ User ] = {
    temporaryUserData.get( username ) match {
      case Some( hashedPassword ) =>
        if ( PasswordHasher.checkPassword( password, hashedPassword ) ) {
          val user = instantiateUser( username )
          loggedInUserTokens.addOne( user.sessionId, user )
          loggedInUsers += ( username -> user )
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
    //userActor ! InitializeUserActor( sessionId )
    val user = User( username, userActor, sessionId )

    // add the chatrooms to the user
    if ( userToChatRooms.contains( username ) ) {
      val userChatRooms = userToChatRooms( username )
      for ( chatMap <- userChatRooms ) {
        for ( (chatName, chatActorRef) <- chatMap ) {
          user.addChatActor( chatName, chatActorRef )
        }
      }

    }
    user
  }

  def addUserToChat( username : String, chatName : String, chatActor : ActorRef ) : Unit = {
    val chatInfo = Map( chatName -> chatActor )
    if ( userToChatRooms.contains( username ) ) {
      userToChatRooms( username ) += chatInfo
    }
    else {
      userToChatRooms( username ) = mutable.Set( chatInfo )
    }

    if ( loggedInUsers.contains( username ) ) {
      loggedInUsers( username ).addChatActor( chatName, chatActor )
    }

  }

  def getLoggedInUsersByToken : mutable.Map[ String, User ] = loggedInUserTokens

  def getLoggedInUsers : mutable.Map[ String, User ] = loggedInUsers

  def getTemporaryUserData : mutable.Map[ String, String ] = temporaryUserData

  def getUserToChatRooms : mutable.Map[ String, mutable.Set[ Map[ String, ActorRef ] ] ] = userToChatRooms

  def getUsers : mutable.Set[ String ] = users
}
