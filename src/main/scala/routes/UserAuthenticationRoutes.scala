package chatapp
package routes

import auth.UserManager

import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object UserAuthenticationRoutes {
  def authenticateRoute( userManager : UserManager ) : Route = {
    path( "authenticate" ) {
      get {
        getFromResource( "userAuthentication.html" )
      } ~
        post {
          formFields( "username", "password" ) { ( username, password ) =>
            userManager.authenticateUser( username, password ) match {
              case Some( user ) =>
                // When a user logs in, we generate a unique token for them for identification
                complete( HttpEntity( ContentTypes.`application/json`, s"""{"token": "${user.getSessionId}"}""" ) )
              case None =>
                if ( !userManager.getUsers.contains( username ) ) {
                  complete( "User does not exist." )
                }
                else {
                  complete( "Log in failed! Incorrect credentials" )
                }
            }
          }
        }
    }
  }
}
