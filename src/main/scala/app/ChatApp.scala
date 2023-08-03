package chatapp
package app

import actors.ChatActor
import messages.JoinChat

import akka.actor.{ActorSystem, Props}
import chatapp.models.User

object ChatApp extends App {
  val system = ActorSystem( "ChatSystem" )
  val chatActor = system.actorOf( Props[ ChatActor ], "chatActor" )

  val user1 = User( "Tom Higgins" )
  chatActor ! JoinChat( user1 )

  // TODO: Implement the logic to handle user input, send messages to the chatActor, etc.
}
