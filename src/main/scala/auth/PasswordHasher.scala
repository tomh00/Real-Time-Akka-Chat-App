package chatapp
package auth

import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory


object PasswordHasher {
  private val logger = LoggerFactory.getLogger( getClass )

  def hashPassword( password : String ) : String = {
    try {
      BCrypt.hashpw( password, BCrypt.gensalt() )
    } catch {
      case e : Exception =>
        logger.error( s"Error hashing password: ${e.getMessage}" )
        throw e
    }
  }

  def checkPassword( candidate : String, hashedPassword : String ) : Boolean = {
    try {
      BCrypt.checkpw( candidate, hashedPassword )
    } catch {
      case e : Exception =>
        logger.error( s"Error checking password: ${e.getMessage}" )
        false
    }
  }
}

