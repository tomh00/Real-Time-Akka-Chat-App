package chatapp
package utilities

import akka.actor.{ActorRef, ActorSystem}
import chatapp.actors.ChatActor
import chatapp.auth.UserManager
import chatapp.messages.JoinChat
import chatapp.models.User

import scala.collection.mutable

class ChatManager( system : ActorSystem, userManager: UserManager ) {
  private val chatsMap : mutable.Map[ String, ActorRef ] = mutable.Map()

  def createChatRoom( roomName : String, usersToAdd : List[ String ] ) : ActorRef = {
    val chatRoom = system.actorOf( ChatActor.props( roomName, userManager ) )
    chatsMap.addOne( roomName, chatRoom )
    usersToAdd.foreach { username =>
      if ( userManager.getUsers.contains( username ) ) {
        chatRoom ! JoinChat( username )
      }
    }
    chatRoom
  }

}
