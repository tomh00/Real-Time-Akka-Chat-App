package chatapp
package routes

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import spray.json.{JsArray, JsString}

object ChatroomRoutes {
  val chatroomRoute : Route =
    path ( "chatroom" ) {
      get {
        getFromResource( "chatroom.html" )
      }
    }

  val chatRooms = List( "room1", "room2", "room3" )

  val rooms : Route =
    path( "chatroom" / "rooms" ) {
      get {
        val chatRoomsJson = JsArray( chatRooms.map( JsString( _ ) ) : _* )

        complete( HttpEntity( ContentTypes.`application/json`, chatRoomsJson.prettyPrint ) )
      }
    }

}
