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
    case join @ JoinChat( user ) => handleJoinChat( join )
    case leave @ LeaveChat( user ) => handleLeaveChat( leave )
    case chatMsg @ ChatMessage( username, message ) => handleChatMessage( chatMsg )

  }

  private def handleJoinChat( join : JoinChat ) : Unit = {
    if ( !usersInChat.contains( join.getUsername ) ) {
      usersInChat += join.getUsername
      // tell the user manager to add the chat actor to the corresponding user
      // when a user logs in you add the chat actors to the instantiated users
      userManager.addUserToChat( join.getUsername, roomName, self )

      //log.info( s"${ join.getUsername.getUserName } has joined the chat!" )
    }
    else {
      //sender() ! join.getUser.getUserName + " is already in the chat."
    }
  }

  private def handleLeaveChat( leave : LeaveChat ) : Unit = {
    if ( usersInChat.contains( leave.getUsername ) ) {
      usersInChat -= leave.getUsername

      //log.info(s"${leave.getUsername.getUserName} has left the chat!")
    }

    else {
      //sender() ! leave.getUser.getUserName + " is not in the chat."
    }
  }

  private def handleChatMessage( chatMsg : ChatMessage ) : Unit = {
    println( s"${chatMsg.userName} said ${chatMsg.message}" )

    // sending message to all group chat members
    print( s"Users in $roomName: " )
    usersInChat.foreach { user =>
      println( user )
      if ( user != chatMsg.userName ) {
        userManager.getLoggedInUsers( user ).getRef ! ChatMessage( chatMsg.userName, chatMsg.message )
      }
    }

  }

  def getUsers : Set[ String ] = usersInChat

}

object ChatActor {
  def props( roomName : String, userManager : UserManager ) : Props = Props( new ChatActor( roomName, userManager ) )
}
