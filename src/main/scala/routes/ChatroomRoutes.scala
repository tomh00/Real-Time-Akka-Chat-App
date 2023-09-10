package chatapp
package routes

import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import chatapp.models.NewChatRoomRequest
import spray.json.{JsArray, JsString}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import chatapp.utilities.ChatManager
import spray.json.RootJsonFormat
import spray.json.DefaultJsonProtocol._

object ChatroomRoutes {
  val chatroomRoute : Route =
    path ( "chatroom" ) {
      get {
        getFromResource( "chatroom.html" )
      }
    }

  var chatRooms : List[ String ] = List()

  val rooms : Route =
    path( "chatroom" / "rooms" ) {
      get {
        val chatRoomsJson = JsArray( chatRooms.map( JsString( _ ) ) : _* )

        complete( HttpEntity( ContentTypes.`application/json`, chatRoomsJson.prettyPrint ) )
      }
    }

  implicit val newChatRoomRequestFormat: RootJsonFormat[ NewChatRoomRequest ] = jsonFormat2( NewChatRoomRequest )

  def newChatRoute( chatManager : ChatManager ) : Route =
    path( "new-chat" ) {
      post {
        entity( as[ NewChatRoomRequest ] ) { newChatRoomRequest =>
          val roomName = newChatRoomRequest.name
          val usersToAdd = newChatRoomRequest.users

          chatManager.createChatRoom( roomName, usersToAdd )

          chatRooms = chatRooms :+ roomName
          complete( "hi" )
        }
      }
    }


}
