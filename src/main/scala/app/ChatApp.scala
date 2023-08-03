package chatapp
package app

import actors.ChatActor
import messages.JoinChat
import akka.actor.{ActorSystem, Props}

object ChatApp extends App {
  val system = ActorSystem( "ChatSystem" )
  val chatActor = system.actorOf( Props[ ChatActor ], "chatActor" )

  chatActor ! JoinChat( "Tom Higgins" )

  // TODO: Implement the logic to handle user input, send messages to the chatActor, etc.
}
