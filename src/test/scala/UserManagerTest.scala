package chatapp

import akka.actor.ActorSystem
import chatapp.auth.UserManager
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UserManagerTest extends AnyFlatSpec with Matchers {

  "UserManager" should "register a user" in {
    val system = ActorSystem("TestSystem")
    val userManager = new UserManager(system)

    val registered = userManager.registerUser("alice", "mypassword")
    registered should be(true)
  }

  it should "not register a duplicate username" in {
    val system = ActorSystem("TestSystem")
    val userManager = new UserManager(system)

    userManager.registerUser("alice", "mypassword") // Register once
    val registeredAgain = userManager.registerUser("alice", "anotherpassword") // Try to register again

    registeredAgain should be(false)
  }

  it should "authenticate a user" in {
    val system = ActorSystem("TestSystem")
    val userManager = new UserManager(system)

    userManager.registerUser("alice", "mypassword") // Register a user

    val authenticated = userManager.authenticateUser("alice", "mypassword")
    authenticated should be(true)
  }

  it should "not authenticate with incorrect password" in {
    val system = ActorSystem("TestSystem")
    val userManager = new UserManager(system)

    userManager.registerUser("alice", "mypassword") // Register a user

    val authenticated = userManager.authenticateUser("alice", "wrongpassword")
    authenticated should be(false)
  }
}
