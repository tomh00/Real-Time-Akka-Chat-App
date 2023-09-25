package chatapp

import actors.ChatActor
import auth.UserManager
import messages.{ ChatMessage, JoinChat }
import models.User

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActorRef, TestKit, TestProbe }
import org.mockito.MockitoSugar.{ mock, verify, when }
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.mutable

class ChatActorTest extends TestKit( ActorSystem( "TestSystem" ) )
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with ImplicitSender {

  // Mock the scenario
  private val user1Probe = TestProbe()
  private val userManagerMock = mock[ UserManager ]
  private val user2Probe = TestProbe()
  private val user3Probe = TestProbe()
  private val user4Probe = TestProbe()
  private val user1 = User( "User1", user1Probe.ref, "session1" )
  private val user2 = User( "User2", user2Probe.ref, "session2" )
  private val user3 = User( "User3", user3Probe.ref, "session3" )
  private val user4 = User( "User4", user4Probe.ref, "session4" )
  private val loggedInUsersMap = mutable.Map( "User1" -> user1, "User2" -> user2, "User3" -> user3, "User4" -> user4 )
  val chatActor : TestActorRef[ ChatActor ] = TestActorRef[ ChatActor ]( ChatActor.props( "TestRoom",
    userManagerMock
  ) )
  chatActor ! JoinChat( "User1" )
  chatActor ! JoinChat( "User2" )
  chatActor ! JoinChat( "User3" )
  chatActor ! JoinChat( "User4" )

  private def getUsersInChat( testChatActor : TestActorRef[ ChatActor ] ) : Set[ String ] = {
    val usersInChatSet = testChatActor.underlyingActor.getClass.getDeclaredField( "usersInChat" )
    usersInChatSet.setAccessible( true )
    usersInChatSet.get( testChatActor.underlyingActor ).asInstanceOf[ Set[ String ] ]
  }

  // Cleanup the actor system after all tests are executed
  override def afterAll : Unit = {
    TestKit.shutdownActorSystem( system )
  }

  "A ChatActor" when {
    "receiving a JoinChat message" should {
      "add the user to the chat" in {
        getUsersInChat( chatActor ) should contain( "User1" )
        getUsersInChat( chatActor ) should contain( "User2" )
        getUsersInChat( chatActor ) should contain( "User3" )
        getUsersInChat( chatActor ) should contain( "User4" )
      }

      "notify UserManager" in {
        verify( userManagerMock ).addUserToChat( "User1", "TestRoom", chatActor )
      }

      "not add the same user to the chat multiple times" in {
        chatActor ! JoinChat( "User1" )
        chatActor ! JoinChat( "User1" )
        getUsersInChat( chatActor ).count( _ == "User1" ) shouldBe 1
      }
    }

    "receiving a ChatMessage message" should {
      "broadcast the message to each member of the chat" in {
        when( userManagerMock.getLoggedInUsers ).thenReturn( loggedInUsersMap )
        val chatMessage = ChatMessage( "User1", "Hello, everyone!" )
        chatActor ! chatMessage

        user2Probe.expectMsg( chatMessage )
        user3Probe.expectMsg( chatMessage )
        user4Probe.expectMsg( chatMessage )
      }

      "ignore messages from users not in the chat" in {
        when( userManagerMock.getLoggedInUsers ).thenReturn( loggedInUsersMap )
        val chatMessage = ChatMessage( "NonExistentUser", "Hello, everyone!" )
        chatActor ! chatMessage
        // Verify that no user receives the message
        user2Probe.expectNoMessage()
        user3Probe.expectNoMessage()
        user4Probe.expectNoMessage()
      }

      "not send the message back to the sender" in {
        when( userManagerMock.getLoggedInUsers ).thenReturn( loggedInUsersMap )
        val chatMessage = ChatMessage( "User1", "Hello, everyone!" )
        chatActor ! chatMessage
        // Verify that User1 does not receive the message
        user1Probe.expectNoMessage()
        // Verify that other users receive the message
        user2Probe.expectMsg( chatMessage )
        user3Probe.expectMsg( chatMessage )
        user4Probe.expectMsg( chatMessage )
      }

      "ignore empty chat messages" in {
        when( userManagerMock.getLoggedInUsers ).thenReturn( loggedInUsersMap )
        val chatMessage = ChatMessage( "User1", "" )
        chatActor ! chatMessage
        // Verify that no user receives the message
        user2Probe.expectNoMessage()
        user3Probe.expectNoMessage()
        user4Probe.expectNoMessage()
      }
    }
  }
}
