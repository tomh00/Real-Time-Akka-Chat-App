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

  // register a user
  val username = scala.io.StdIn.readLine( "Enter your username: " )
  val password = scala.io.StdIn.readLine( "Enter your password: " )

  userManager.registerUser( username, password )

  val username2 = scala.io.StdIn.readLine( "Enter your username: " )
  val password2 = scala.io.StdIn.readLine( "Enter your password: " )

  userManager.registerUser( username2, password2 )

  userManager.users.foreach { case ( username: String, user : User ) =>
    println( username )
  }







  /*val chatActor = system.actorOf( Props[ ChatActor ], "chatActor" )

  val user1 = User( "tomh00", system.actorOf( Props[ UserActor ], "userActor" ) )
  chatActor ! JoinChat( user1 )
  val user2 = User( "johndoe", system.actorOf( Props[ UserActor ], "userActor2" ) )
  chatActor ! JoinChat( user2 )

  val userInputActor = system.actorOf( Props[ UserInputActor ], "userInputActor" )
  userInputActor ! StartListening( chatActor )*/
}
