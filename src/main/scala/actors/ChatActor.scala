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
    if ( chatMsg.message.isEmpty ) {
      log.error( s"Message from ${chatMsg.userName} to $roomName is an empty message." )
    }
    else if ( !usersInChat.contains( chatMsg.userName ) ) {
      log.error( s"${chatMsg.userName} is not a member of room $roomName." )
    }
    // sending message to all group chat members
    else {
      usersInChat.foreach { user =>
        if ( user != chatMsg.userName ) {
          userManager.getLoggedInUsers.get( user ) match {
            case Some( user ) =>
              user.getRef ! ChatMessage( chatMsg.userName, chatMsg.message )
            case None =>
              log.info( s"$user not logged in" )
          }
        }
      }
    }
  }
}

object ChatActor {
  def props( roomName : String, userManager : UserManager ) : Props = Props( new ChatActor( roomName, userManager ) )
}
