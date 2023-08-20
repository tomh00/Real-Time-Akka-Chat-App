package chatapp.auth

import akka.actor.ActorSystem
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class PasswordHasherTest extends AnyFlatSpec with Matchers {
  "PasswordHasher" should "hash and check passwords correctly" in {
    val password = "securePassword"
    val hashedPassword = PasswordHasher.hashPassword( password )
    val isPasswordValid = PasswordHasher.checkPassword( password, hashedPassword )

    hashedPassword should not be password
    isPasswordValid shouldBe true
  }

  it should "return false for incorrect password check" in {
    val password = "securePassword"
    val incorrectPassword = "wrongPassword"
    val hashedPassword = PasswordHasher.hashPassword( password )
    val isPasswordValid = PasswordHasher.checkPassword( incorrectPassword, hashedPassword )

    isPasswordValid shouldBe false
  }
}
