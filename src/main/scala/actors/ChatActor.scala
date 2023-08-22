package chatapp
package actors

import messages.{AddChatActor, ChatMessage, JoinChat, LeaveChat}

import akka.actor.{Actor, ActorLogging, ActorRef}
import chatapp.models.User

class ChatActor extends Actor with ActorLogging {
  /*
  * Current implementation of the ChatApp will use an in-memory data structure
  * However an external database could be considered as the app scales
   */
  private var usersInChat : Set[ User ] = Set.empty

  override def receive: Receive = {
    case join @ JoinChat( user, socketActor ) => handleJoinChat( join, socketActor )
    case leave @ LeaveChat( user ) => handleLeaveChat( leave )
    case chatMsg @ ChatMessage( username, message ) => handleChatMessage( chatMsg )

  }

  private def handleJoinChat( join : JoinChat, socketActor : ActorRef ) : Unit = {
    if ( ! usersInChat.contains( join.getUser ) ) {
      usersInChat += join.getUser
      join.getUser.getRef ! AddChatActor( "the-chat", socketActor )

      log.info(s"${join.getUser.getUserName} has joined the chat!")
    }
    else {
      //sender() ! join.getUser.getUserName + " is already in the chat."
    }
  }

  private def handleLeaveChat(leave : LeaveChat ) : Unit = {
    if (usersInChat.contains(leave.getUser)) {
      usersInChat -= leave.getUser

      log.info(s"${leave.getUser.getUserName} has left the chat!")
    }

    else {
      //sender() ! leave.getUser.getUserName + " is not in the chat."
    }
  }

  private def handleChatMessage( chatMsg : ChatMessage ) : Unit = {
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
