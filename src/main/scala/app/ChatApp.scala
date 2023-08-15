package chatapp
package app

import actors.{ChatActor, UserActor, UserInputActor}
import messages.{JoinChat, StartListening}

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import chatapp.auth.UserManager
import chatapp.models.User

import scala.collection.IterableOnce.iterableOnceExtensionMethods

object ChatApp extends App {
  val system = ActorSystem( "ChatSystem" )

  val userManager = new UserManager( system )

  // Register a user
  val registered = userManager.registerUser( "alice", "mypassword" )
  if ( registered ) {
    println( "User registered successfully" )
  } else {
    println( "Username already exists" )
  }

  // Authenticate a user
  val authenticated = userManager.authenticateUser( "alice", "mypassword" )
  if ( authenticated ) {
    println( "Authentication successful" )
  } else {
    println( "Authentication failed" )
  }







  /*val chatActor = system.actorOf( Props[ ChatActor ], "chatActor" )

  val user1 = User( "tomh00", system.actorOf( Props[ UserActor ], "userActor" ) )
  chatActor ! JoinChat( user1 )
  val user2 = User( "johndoe", system.actorOf( Props[ UserActor ], "userActor2" ) )
  chatActor ! JoinChat( user2 )

  val userInputActor = system.actorOf( Props[ UserInputActor ], "userInputActor" )
  userInputActor ! StartListening( chatActor )*/
}
