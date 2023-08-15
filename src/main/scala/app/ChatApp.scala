package chatapp
package app

import actors.{ChatActor, UserActor, UserInputActor}
import messages.{JoinChat, StartListening}

import akka.actor.{ActorSystem, Props}
import akka.stream.ActorMaterializer
import chatapp.models.User

object ChatApp extends App {
  val system = ActorSystem( "ChatSystem" )
  val chatActor = system.actorOf( Props[ ChatActor ], "chatActor" )

  val user1 = User( "tomh00", system.actorOf( Props[ UserActor ], "userActor" ) )
  chatActor ! JoinChat( user1 )
  val user2 = User( "johndoe", system.actorOf( Props[ UserActor ], "userActor2" ) )
  chatActor ! JoinChat( user2 )

  val userInputActor = system.actorOf( Props[ UserInputActor ], "userInputActor" )
  userInputActor ! StartListening( chatActor )
}
