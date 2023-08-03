package chatapp
package app

import akka.actor.{ActorSystem, Props}
import actors.ChatActor

object ChatApp extends App {
  val system = ActorSystem( "ChatSystem" )
  val chatActor = system.actorOf( Props[ ChatActor ], "chatActor" )

  chatActor.!( "yo yo heres your first message" )

  // TODO: Implement the logic to handle user input, send messages to the chatActor, etc.
}
