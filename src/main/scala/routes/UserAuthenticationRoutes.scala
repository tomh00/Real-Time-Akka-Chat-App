package chatapp
package routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import chatapp.auth.{TokenUtility, UserManager}

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
              val userSessionToken = TokenUtility.generateToken( username )
              complete( HttpEntity( ContentTypes.`application/json`, s"""{"token": "$userSessionToken"}""" ) )
            } else {
              println( "Login failed" )
              complete( "Log in failed! Incorrect credentials" )
            }
          }
        }
    }

}
