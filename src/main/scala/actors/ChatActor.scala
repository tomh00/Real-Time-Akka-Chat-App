package chatapp
package actors

import messages.{ChatMessage, JoinChat, LeaveChat}

import akka.actor.{Actor, ActorLogging}
import chatapp.models.User

class ChatActor extends Actor with ActorLogging {
  /*
  * Current implementation of the ChatApp will use an in-memory data structure
  * However an external database could be considered as the app scales
   */
  private var usersInChat : Set[ User ] = Set.empty

  override def receive: Receive = {
    case join @ JoinChat( user ) => handleJoinChat( join )
    case leave @ LeaveChat( user ) => handleLeaveChat( leave )
    case chatMsg @ ChatMessage( username, message ) => handleChatMessage( chatMsg )

  }

  def handleJoinChat( join : JoinChat ) : Unit = {
    if ( ! usersInChat.contains( join.getUser ) ) {
      usersInChat += join.getUser

      log.info(s"${join.getUser.getUserName} has joined the chat!")
      //sender() ! join.getUser.getUserName + " joined the chat."
    }
    else {
      //sender() ! join.getUser.getUserName + " is already in the chat."
    }
  }

  def handleLeaveChat( leave : LeaveChat ) : Unit = {
    if (usersInChat.contains(leave.getUser)) {
      usersInChat -= leave.getUser

      log.info(s"${leave.getUser.getUserName} has left the chat!")
      //sender() ! leave.getUser.getUserName + " left the chat."
    }

    else {
      //sender() ! leave.getUser.getUserName + " is not in the chat."
    }
  }

  def handleChatMessage( chatMsg : ChatMessage ) : Unit = {
    log.info( s"New message from: ${chatMsg.userName}. Message: ${ chatMsg.message }" )

    // sending message to all group chat members
    usersInChat.foreach { user =>
      if ( user.getUserName != chatMsg.userName ) {
        user.getRef ! ChatMessage( chatMsg.userName, chatMsg.message )
      }
    }

  }

  def getUsers: Set[ User ] = usersInChat

}
