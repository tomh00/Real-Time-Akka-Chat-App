package chatapp

import akka.actor.ActorSystem
import chatapp.auth.UserManager
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UserManagerSpec extends AnyFlatSpec with Matchers {
  implicit val system: ActorSystem = ActorSystem("TestSystem")
  val userManager: UserManager = new UserManager(system)

  "UserManager" should "register a new user" in {
    val userResult = userManager.registerUser("newUser", "newPassword" )
    userResult shouldBe a[ Some[ _ ] ]
  }

  it should "not register a user with the same username" in {
    val userResult1 = userManager.registerUser( "existingUser", "password" )
    val userResult2 = userManager.registerUser( "existingUser", "newPassword" )
    userResult1 shouldBe a[ Some[ _ ] ]
    userResult2 shouldBe None
  }

  it should "authenticate a registered user" in {
    userManager.registerUser( "authUser", "authPassword" )
    val authResult = userManager.authenticateUser( "authUser", "authPassword" )
    authResult shouldBe a[ Some[ _ ] ]
  }

  it should "not authenticate a user with incorrect password" in {
    userManager.registerUser( "authUser2", "authPassword2" )
    val authResult = userManager.authenticateUser( "authUser2", "incorrectPassword" )
    authResult shouldBe None
  }
}

