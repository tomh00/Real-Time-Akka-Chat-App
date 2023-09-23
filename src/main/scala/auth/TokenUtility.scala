package chatapp
package auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import java.security.SecureRandom
import java.util.{ Base64, Date }

object TokenUtility {
  private val TokenExpirationTimeMillis : Long = 3600000 // 1 hour
  private val SecretKeyLength : Int = 32
  private val secretKey = generateSecretKey( SecretKeyLength )

  def generateToken( username : String ) : String = {
    val algorithm = Algorithm.HMAC256( secretKey )
    val expirationDate = new Date( System.currentTimeMillis() + TokenExpirationTimeMillis )
    val token = JWT.create().withSubject( username ).withExpiresAt( expirationDate ).sign( algorithm )
    token
  }

  private def generateSecretKey( length : Int ) : String = {
    try {
      val random = new SecureRandom()
      val bytes = new Array[ Byte ]( length )
      random.nextBytes( bytes )
      Base64.getUrlEncoder.encodeToString( bytes )
    } catch {
      case e : Exception =>
        throw new RuntimeException( "Error generating secret key", e )
    }
  }
}
