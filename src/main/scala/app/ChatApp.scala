package chatapp
package app

import actors.{ChatActor, UserActor, UserInputActor}

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.Http
import chatapp.auth.UserManager
import chatapp.messages.JoinChat
import chatapp.routes.{ChatroomRoutes, RegistrationRoutes, UserAuthenticationRoutes, WebSocketRoutes}

object ChatApp extends App {
  implicit val system: ActorSystem = ActorSystem( "ChatSystem" )
  val userManager = new UserManager( system )
  val chatActor = system.actorOf( ChatActor.props( "room1" ), "chatActor" )
  val webSocketRoutes = new WebSocketRoutes()

  // Set up routes and bind them to the server
  val routes: Route =
    Directives.concat (
      RegistrationRoutes.routes( chatActor, userManager ),
      ChatroomRoutes.chatroomRoute,
      ChatroomRoutes.rooms,
      UserAuthenticationRoutes.authenticateRoute( chatActor, userManager ),
      UserAuthenticationRoutes.testRoute,
      webSocketRoutes.websocketRoute( userManager, chatActor )
    )

  val serverBinding = Http().newServerAt( "localhost", 8080 )bind( routes )

  // Register users for demonstrating functionality
  val tom = userManager.registerUser( "tom", "tom" )
  val bob = userManager.registerUser("bob", "bob")
  val alice = userManager.registerUser("alice", "alice")
  val user1 = userManager.registerUser("user", "user")
  val lily = userManager.registerUser("lily", "lily")

}
