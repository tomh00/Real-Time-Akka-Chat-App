package chatapp

import actors.ChatActor

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
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
    "send back the same message it receives" in {
      val chatActor = system.actorOf( Props[ ChatActor ], "ChatActor")
      chatActor ! "Hello, ChatActor!"
      expectMsg("Hello, ChatActor!")
    }
  }

}
