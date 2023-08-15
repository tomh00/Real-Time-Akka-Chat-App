package chatapp
package auth

import org.mindrot.jbcrypt.BCrypt

object PasswordHasher {
  def hashPassword( password : String ) : String = {
    BCrypt.hashpw( password, BCrypt.gensalt() )
  }

  def checkPassword( candidate : String, hashedPassword : String ) : Boolean = {
    BCrypt.checkpw( candidate, hashedPassword )
  }
}
