package chatapp
package models

import akka.actor.ActorRef

import scala.collection.mutable

case class User( userName : String, private val actorRef : ActorRef, sessionId : String ) {
  private val chatRooms : mutable.Map[ String, ActorRef ] = mutable.Map()

  def addChatActor( chatName : String, chatActor : ActorRef ) : Unit =
    chatRooms.addOne( chatName, chatActor )

  def getUserName : String = userName

  def getRef : ActorRef = actorRef

  def getSessionId : String = sessionId

  def getChatRooms : mutable.Map[ String, ActorRef ] = chatRooms

}
