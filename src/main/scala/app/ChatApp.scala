package chatapp
package app

import actors.{ChatActor, UserActor, UserInputActor}
import messages.{JoinChat, StartListening}

import akka.actor.Status.{Failure, Success}
import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.pathPrefix
import chatapp.auth.UserManager
import chatapp.routes.{ChatroomRoutes, HomeRoutes, RegistrationRoutes, UserAuthenticationRoutes, WebSocketRoutes}

object ChatApp extends App {
  implicit val system: ActorSystem = ActorSystem( "ChatSystem" )
  val userManager = new UserManager( system )
  val chatActor = system.actorOf( Props[ ChatActor ], "chatActor" )

  val routes: Route =
    Directives.concat (
      HomeRoutes.homeRoute,
      RegistrationRoutes.routes( userManager ),
      ChatroomRoutes.chatroomRoute,
      UserAuthenticationRoutes.authenticateRoute( userManager ),
      WebSocketRoutes.websocketRoute( userManager, chatActor )
    )

  val serverBinding = Http().newServerAt( "localhost", 8080 )bind( routes )




  // Register a user
  val tom = userManager.registerUser( "tom", "tom" )
  val bob = userManager.registerUser("bob", "bob")
  val alice = userManager.registerUser("alice", "alice")
  val user1 = userManager.registerUser("user", "user")
  val lily = userManager.registerUser("lily", "lily")

  // Pattern matching to send JoinChat message
  bob match {
    case Some(user) => chatActor ! JoinChat(user)
    case None => println("Bob registration failed")
  }

  alice match {
    case Some(user) => chatActor ! JoinChat(user)
    case None => println("Alice registration failed")
  }

  user1 match {
    case Some(user) => chatActor ! JoinChat(user)
    case None => println("User1 registration failed")
  }

  lily match {
    case Some(user) => chatActor ! JoinChat(user)
    case None => println("Lily registration failed")
  }


  // Authenticate a user
  val authenticated = userManager.authenticateUser( "alice", "mypassword" )







  /*val chatActor = system.actorOf( Props[ ChatActor ], "chatActor" )

  val user1 = User( "tomh00", system.actorOf( Props[ UserActor ], "userActor" ) )
  chatActor ! JoinChat( user1 )
  val user2 = User( "johndoe", system.actorOf( Props[ UserActor ], "userActor2" ) )
  chatActor ! JoinChat( user2 )

  val userInputActor = system.actorOf( Props[ UserInputActor ], "userInputActor" )
  userInputActor ! StartListening( chatActor )*/
}
