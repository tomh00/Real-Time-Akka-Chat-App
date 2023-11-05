package chatapp

import actors.UserActor
import messages.{ AddWebSocket, ChatMessage, UpdateChatList }

import akka.actor.{ ActorRef, ActorSystem }
import akka.testkit.{ ImplicitSender, TestActorRef, TestKit, TestProbe }
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.collection.mutable

class UserActorTest extends TestKit( ActorSystem( "UserActorTest" ) )
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with ImplicitSender {

  private val testUserActor : TestActorRef[ UserActor ] = TestActorRef[ UserActor ]

  override def afterAll : Unit = {
    TestKit.shutdownActorSystem( system )
  }

  def getWebsocketMap( userActor : TestActorRef[ UserActor ] ) : mutable.Map[ String, ActorRef ] = {
    val webSocketActors = userActor.underlyingActor.getClass.getDeclaredField( "webSocketActors" )
    webSocketActors.setAccessible( true )
    val map = webSocketActors.get( userActor.underlyingActor ).asInstanceOf[ mutable.Map[ String,
      ActorRef ] ]
    print(map)
    map
  }

  "A UserActor" when {
    "receiving ChatMessage" should {
      "forward the message to ChatMessageSocket" in {
        // Create a mock ChatMessageSocket
        val chatMessageSocket = TestProbe()
        testUserActor ! AddWebSocket( "ChatMessageSocket", chatMessageSocket.ref )
        val chatMessage = ChatMessage( "User1", "Hello, everyone!" )
        testUserActor ! chatMessage
        chatMessageSocket.expectMsg( s"${chatMessage.userName}: ${chatMessage.message}" )
      }
    }

    "receiving UpdateChatList" should {
      "forward the message to ChatListSocket" in {
        // Create a mock ChatListSocket
        val chatListSocket = TestProbe()
        testUserActor ! AddWebSocket( "ChatListSocket", chatListSocket.ref )
        val updateChatList = UpdateChatList( "Room1" )
        testUserActor ! updateChatList
        chatListSocket.expectMsg( "Update chat list" )
      }
    }

    "receiving AddWebSocket" should {
      "store the ActorRef in webSocketActors" in {
        val websocketActor = TestProbe().ref
        testUserActor ! AddWebSocket( "TestWebSocket", websocketActor )
        val webSocketActors = getWebsocketMap( testUserActor )
        webSocketActors should contain( "TestWebSocket" -> websocketActor )

      }
    }

    /*"receiving AddWebSocket with an existing WebSocket" should {
      "replace the ActorRef in webSocketActors" in {
        val websocketActor1 = TestProbe().ref
        val websocketActor2 = TestProbe().ref

        // Send AddWebSocket messages to UserActor
        testUserActor ! AddWebSocket( "TestWebSocket", websocketActor1 )
        testUserActor ! AddWebSocket( "TestWebSocket", websocketActor2 )
        val webSocketActors = testUserActor.underlyingActor.getClass.getDeclaredField( "webSocketActors" )
        webSocketActors.setAccessible( true )
        val webSocketMap = webSocketActors.get( testUserActor.underlyingActor ).asInstanceOf[ mutable.Map[ String,
          ActorRef ] ]
        webSocketMap should contain( "TestWebSocket" -> websocketActor2 )
      }
    }*/
  }
}
