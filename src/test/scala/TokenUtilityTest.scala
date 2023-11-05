package chatapp

import auth.TokenUtility

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TokenUtilityTest extends AnyFlatSpec with Matchers {
  "TokenUtility" should "generate a valid token" in {
    val token = TokenUtility.generateToken( "testUser" )
    token should not be empty
  }
}