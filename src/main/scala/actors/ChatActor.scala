package chatapp
package actors

import auth.UserManager
import messages.{ ChatMessage, JoinChat, LeaveChat }
import akka.actor.{ Actor, ActorLogging, Props }

class ChatActor( roomName : String, userManager : UserManager ) extends Actor with ActorLogging {
  /*
  * Current implementation of the ChatApp will use an in-memory data structure
  * However an external database could be considered as the app scales
   */
  private var usersInChat : Set[ String ] = Set.empty

  override def receive : Receive = {
    case join @ JoinChat( _ ) => handleJoinChat( join )
    case leave @ LeaveChat( _ ) => handleLeaveChat( leave )
    case chatMsg @ ChatMessage( _, _ ) => handleChatMessage( chatMsg )
    case _ => log.warning( "Received an unexpected message in ChatActor" )
  }

  private def handleJoinChat( join : JoinChat ) : Unit = {
    if ( !usersInChat.contains( join.username ) ) {
      usersInChat += join.username
      userManager.addUserToChat( join.username, roomName, self )
      log.info( s"${join.username} has joined the chat!" )
    }

    else {
      log.error( s"${join.username} is already in the chat." )
    }
  }

  private def handleLeaveChat( leave : LeaveChat ) : Unit = {
    if ( usersInChat.contains( leave.username ) ) {
      usersInChat -= leave.username
      log.info( s"${leave.username} has left the chat!" )
    }

    else {
      log.error( s"${leave.username} is not in the chat." )
    }
  }

  private def handleChatMessage( chatMsg : ChatMessage ) : Unit = {
    log.info( s"Received message: \nMessage: $chatMsg\nFrom: ${chatMsg.userName}" )
    // sending message to all group chat members
    usersInChat.foreach { user =>
      if ( user != chatMsg.userName ) {
        userManager.getLoggedInUsers( user ).getRef ! ChatMessage( chatMsg.userName, chatMsg.message )
      }
    }
  }
}

object ChatActor {
  def props( roomName : String, userManager : UserManager ) : Props = Props( new ChatActor( roomName, userManager ) )
}
