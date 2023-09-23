package chatapp
package app

import auth.UserManager
import routes.{ ChatroomRoutes, RegistrationRoutes, UserAuthenticationRoutes, WebSocketRoutes }
import utilities.ChatManager

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{ Directives, Route }

object ChatApp extends App {
  implicit val system : ActorSystem = ActorSystem( "ChatSystem" )
  val userManager = new UserManager( system )
  val webSocketRoutes = new WebSocketRoutes()
  val chatManager = new ChatManager( system, userManager )

  // Set up routes and bind them to the server
  val routes : Route =
    Directives.concat(
      RegistrationRoutes.routes( userManager ),
      ChatroomRoutes.chatroomRoute,
      ChatroomRoutes.rooms( userManager ),
      ChatroomRoutes.newChatRoute( userManager, chatManager ),
      UserAuthenticationRoutes.authenticateRoute( userManager ),
      webSocketRoutes.websocketChatMessageRoute( userManager ),
      webSocketRoutes.websocketChatListRoute( userManager )
    )

  val serverBinding = Http().newServerAt( "localhost", 8080 ) bind routes
}
