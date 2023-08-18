package chatapp
package routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import chatapp.auth.UserManager

object UserAuthenticationRoutes {
  def authenticateRoute( userManager: UserManager ) : Route =
    path ( "authenticate") {
      get {
        getFromResource( "userAuthentication.html" )
      } ~
        post {
          formFields( "username", "password") { ( username, password ) =>
            val success = userManager.authenticateUser( username, password )

            if ( success ) {
              println( "Login success" )
              complete( "You are logged in!" )
            } else {
              println( "Login failed" )
              complete( "Log in failed! Incorrect credentials" )
            }
          }
        }
    }

}
