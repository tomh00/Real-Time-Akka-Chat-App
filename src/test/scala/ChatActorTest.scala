package chatapp

import actors.ChatActor
import messages.{ChatMessage, JoinChat, LeaveChat}

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import chatapp.models.User
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.wordspec.AnyWordSpecLike

class ChatActorTest extends TestKit( ActorSystem( "TestSytem" ) )
  with AnyWordSpecLike
  with BeforeAndAfterAll
  with BeforeAndAfterEach
  with ImplicitSender {

  var user : User = User( "John Doe" )

  // Cleanup the actor system after all tests are executed
  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A ChatActor" should {
    "add user to the chat room on JoinChat message" in {
      val chatActorRef = TestActorRef[ChatActor]
      val chatActor = chatActorRef.underlyingActor

      // Simulate a user joining the chat
      //val user = User( "John Doe" )
      chatActorRef ! JoinChat( user )

      // Expect a response from the actor
      assert( chatActor.getUsers.map( _.userName ).contains( user.getUserName ) )
    }

    "remove user from the chat room on LeaveChat message" in {
      val chatActorRef = TestActorRef[ChatActor]
      val chatActor = chatActorRef.underlyingActor

      chatActorRef ! LeaveChat( user )
      assert( ! chatActor.getUsers.map( _.userName ).contains( user.getUserName ) )

    }

/*
    "send back the chat message it receives" in {
      val chatActorRef = TestActorRef[ChatActor]
      val chatActor = chatActorRef.underlyingActor

      chatActorRef ! ChatMessage("Tom Higgins", "Hello, everyone!")
      expectMsg("Tom Higgins: Hello, everyone!")
    }*/
  }

}
