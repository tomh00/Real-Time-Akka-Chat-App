package chatapp
package app

import actors.{ChatActor, UserActor, UserInputActor}

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.Http
import chatapp.auth.UserManager
import chatapp.messages.JoinChat
import chatapp.routes.{ChatroomRoutes, RegistrationRoutes, UserAuthenticationRoutes, WebSocketRoutes}
import chatapp.utilities.ChatManager

object ChatApp extends App {
  implicit val system: ActorSystem = ActorSystem( "ChatSystem" )
  val userManager = new UserManager( system )
  val chatActor = system.actorOf( ChatActor.props( "room1", userManager ), "chatActor" )
  val tomsRoom = system.actorOf( ChatActor.props( "toms-room", userManager ), "toms-actor" )
  val webSocketRoutes = new WebSocketRoutes()
  val chatManager = new ChatManager( system, userManager )

  // Set up routes and bind them to the server
  val routes: Route =
    Directives.concat (
      RegistrationRoutes.routes( chatActor, userManager ),
      ChatroomRoutes.chatroomRoute,
      ChatroomRoutes.rooms,
      ChatroomRoutes.newChatRoute( userManager, chatManager ),
      UserAuthenticationRoutes.authenticateRoute( chatActor, userManager ),
      UserAuthenticationRoutes.testRoute,
      webSocketRoutes.websocketChatMessageRoute( userManager, chatActor ),
      webSocketRoutes.websocketChatListRoute( userManager )
    )

  val serverBinding = Http().newServerAt( "localhost", 8080 )bind( routes )

  // Register users for demonstrating functionality
  //val tom = userManager.registerUser( "tom", "tom" )
//  val bob = userManager.registerUser("bob", "bob")
//  val alice = userManager.registerUser("alice", "alice")
//  val user1 = userManager.registerUser("user", "user")
//  val lily = userManager.registerUser("lily", "lily")

}
