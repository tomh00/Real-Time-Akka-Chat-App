package chatapp

import actors.{ChatActor, UserActor}
import messages.{ChatMessage, JoinChat, LeaveChat}

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActorRef, TestKit, TestProbe}
import chatapp.models.User
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}

class ChatActorTest extends TestKit( ActorSystem( "TestSytem" ) )
  with AnyWordSpecLike
  with BeforeAndAfterAll
  with BeforeAndAfterEach
  with ImplicitSender {

  var user : User = User( "John Doe", system.actorOf( Props[ UserActor ], "userActor" ) )
  var user2: User = User( "Tom Higgins", system.actorOf( Props[ UserActor ], "userActor2" ) )

  // Cleanup the actor system after all tests are executed
  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "A ChatActor" should {
    "add user to the chat room on JoinChat message" in {
      val chatActorRef = TestActorRef[ChatActor]
      val chatActor = chatActorRef.underlyingActor

      chatActorRef ! JoinChat( user )

      chatActorRef ! JoinChat( user2 )

      // Expect a response from the actor
      assert( chatActor.getUsers.map( _.userName ).contains( user.getUserName ) )
      assert( chatActor.getUsers.map( _.userName ).contains( user2.getUserName ) )

    }

    "remove user from the chat room on LeaveChat message" in {
      val chatActorRef = TestActorRef[ ChatActor ]
      val chatActor = chatActorRef.underlyingActor

      chatActorRef ! LeaveChat( user )
      assert( ! chatActor.getUsers.map( _.userName ).contains( user.getUserName ) )

    }

    "broadcast chat message to all users in the chat room" in {
      val chatActorRef = TestActorRef[ ChatActor ]
      val chatActor = chatActorRef.underlyingActor

      // Create test probes for the user actors
      val userProbe1 = TestProbe()
      val userProbe2 = TestProbe()

      // Add users to the chat room using test probes
      val userRef1 = userProbe1.ref
      val userRef2 = userProbe2.ref
      chatActorRef ! JoinChat(User("John Doe", userRef1))
      chatActorRef ! JoinChat(User("Tom Higgins", userRef2))

      // Send a chat message
      chatActorRef ! ChatMessage( "John Doe", "Hello, everyone!" )

      // Check if the user actors received the chat message
      userProbe2.expectMsg( ChatMessage( "John Doe", "Hello, everyone!" ) )
    }
  }

}
