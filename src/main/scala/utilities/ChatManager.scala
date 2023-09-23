package chatapp
package utilities

import actors.ChatActor
import auth.UserManager
import messages.JoinChat

import akka.actor.{ ActorRef, ActorSystem }

import scala.collection.mutable

class ChatManager( system : ActorSystem, userManager : UserManager ) {
  private val chatsMap : mutable.Map[ String, ActorRef ] = mutable.Map()

  def createChatRoom( roomName : String, usersToAdd : List[ String ] ) : ActorRef = {
    val chatRoom = chatsMap.getOrElseUpdate(roomName, system.actorOf(ChatActor.props(roomName, userManager)))
    usersToAdd.foreach { username =>
      if ( userManager.getUsers.contains( username ) ) {
        chatRoom ! JoinChat( username )
      }
      else{

      }
    }
    chatRoom
  }
}
