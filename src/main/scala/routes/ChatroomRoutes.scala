package chatapp
package routes

import auth.UserManager
import messages.UpdateChatList
import models.NewChatRoomRequest
import utilities.ChatManager

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, StatusCodes }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import spray.json.DefaultJsonProtocol._
import spray.json.{ JsArray, JsString, RootJsonFormat }

object ChatroomRoutes {
  val chatroomRoute : Route =
    path( "chatroom" ) {
      get {
        getFromResource( "chatroom.html" )
      }
    }

  def rooms( userManager : UserManager ) : Route =
    path( "chatroom" / "rooms" ) {
      parameter( "token" ) { userSessionToken =>
        get {
          userManager.getLoggedInUsersByToken.get( userSessionToken ) match {
            case Some( user ) =>
              val chatRooms = user.getChatRooms.keys.toList
              val chatRoomsJson = JsArray( chatRooms.map( JsString( _ ) ) : _* )
              complete( HttpEntity( ContentTypes.`application/json`, chatRoomsJson.prettyPrint ) )
            case None =>
              complete( "Error finding user. Please authenticate again." )
          }
        }
      }
    }

  implicit val newChatRoomRequestFormat : RootJsonFormat[ NewChatRoomRequest ] = jsonFormat2( NewChatRoomRequest )

  def newChatRoute( userManager : UserManager, chatManager : ChatManager ) : Route =
    path( "new-chat" ) {
      post {
        entity( as[ NewChatRoomRequest ] ) { newChatRoomRequest =>
          val roomName = newChatRoomRequest.name
          val usersToAdd = newChatRoomRequest.users
          chatManager.createChatRoom( roomName, usersToAdd )
          userManager.getLoggedInUsers.foreach { case (username, user) =>
            user.getRef ! UpdateChatList( username )
          }
          complete( StatusCodes.Created )
        }
      }
    }
}
