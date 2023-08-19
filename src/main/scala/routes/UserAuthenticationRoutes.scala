package chatapp
package routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import chatapp.auth.{TokenUtility, UserManager}
import chatapp.models.User

object UserAuthenticationRoutes {
  def authenticateRoute( userManager: UserManager ) : Route =
    path ( "authenticate") {
      get {
        getFromResource( "userAuthentication.html" )
      } ~
        post {
          formFields( "username", "password" ) { ( username, password ) =>

            userManager.authenticateUser( username, password ) match {
              case Some( user ) =>
                // When a user logs in, we generate a unique token for them for identification
                complete( HttpEntity( ContentTypes.`application/json`, s"""{"token": "${ user.getSessionId }"}""" ) )
              case None =>
                complete( "Log in failed! Incorrect credentials" )
            }
          }
        }
    }

}
