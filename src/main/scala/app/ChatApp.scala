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
import chatapp.routes.{HomeRoutes, JsonHandlingExample, RegistrationRoutes, UserAuthenticationRoutes}

object ChatApp extends App {
  implicit val system: ActorSystem = ActorSystem( "ChatSystem" )
  val userManager = new UserManager( system )

  val routes: Route =
    Directives.concat (
      HomeRoutes.homeRoute,
      RegistrationRoutes.routes( userManager ),
      JsonHandlingExample.route,
      JsonHandlingExample.displayPersonRoute,
      UserAuthenticationRoutes.authenticateRoute( userManager )
    )

  val serverBinding = Http().newServerAt( "localhost", 8080 )bind( routes )


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
