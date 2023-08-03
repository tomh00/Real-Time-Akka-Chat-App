package chatapp

import actors.ChatActor
import messages.{ChatMessage, JoinChat, LeaveChat}

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class ChatActorTest extends TestKit( ActorSystem( "TestSytem" ) )
  with AnyWordSpecLike
  with BeforeAndAfterAll
  with ImplicitSender {

  // Cleanup the actor system after all tests are executed
  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A ChatActor" should {
    "send a join message back to the sender" in {
      val chatActorRef = TestActorRef[ChatActor]
      val chatActor = chatActorRef.underlyingActor

      // Simulate a user joining the chat
      chatActorRef ! JoinChat("JohnDoe")

      // Expect a response from the actor
      expectMsg("JohnDoe joined the chat.")
    }

    "send back the leave message it receives" in {
      val chatActorRef = TestActorRef[ChatActor]
      val chatActor = chatActorRef.underlyingActor

      chatActorRef ! LeaveChat("Tom Higgins")
      expectMsg("Tom Higgins left the chat.")
    }

    "send back the chat message it receives" in {
      val chatActorRef = TestActorRef[ChatActor]
      val chatActor = chatActorRef.underlyingActor

      chatActorRef ! ChatMessage("Tom Higgins", "Hello, everyone!")
      expectMsg("Tom Higgins: Hello, everyone!")
    }
  }

}
