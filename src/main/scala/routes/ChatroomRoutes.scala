package chatapp
package routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

object ChatroomRoutes {
  val chatroomRoute : Route =
    path ( "chatroom" ) {
      get {
        getFromResource( "chatroom.html" )
      }
    }

}
