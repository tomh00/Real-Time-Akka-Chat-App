package chatapp.models

import akka.actor.ActorRef
import chatapp.messages.AddChatActor

import scala.collection.mutable

case class User( userName : String, private val actorRef : ActorRef, sessionId : String ) {
  private val chatRooms : mutable.Map[ String, ActorRef ] = mutable.Map.empty

  def getUserName : String = userName
  def getRef : ActorRef = actorRef
  def getSessionId : String = sessionId

  def addChatActor( chatName : String, chatActor : ActorRef ) : Unit =
    chatRooms.addOne( chatName, chatActor )

  def getChatRooms : mutable.Map[ String, ActorRef ] = chatRooms

}
