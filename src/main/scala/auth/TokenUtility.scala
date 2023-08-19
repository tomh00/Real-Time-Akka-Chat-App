package chatapp
package auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import java.security.SecureRandom
import java.util.{Base64, Date}

object TokenUtility {
  private val secretKey = generateSecretKey( 32 )

  def generateToken( username : String ) : String = {
    val algorithm = Algorithm.HMAC256( secretKey )
    val expirationDate = new Date( System.currentTimeMillis() + 3600000 )

    val token = JWT.create().withSubject( username ).withExpiresAt( expirationDate ).sign( algorithm )
    token
  }

  private def generateSecretKey( length: Int ): String = {
    val random = new SecureRandom()
    val bytes = new Array[ Byte ]( length )
    random.nextBytes( bytes )
    Base64.getUrlEncoder.encodeToString( bytes )
  }
}
