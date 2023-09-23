package chatapp
package routes

import auth.UserManager

import akka.http.scaladsl.model.{ ContentTypes, HttpEntity }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object RegistrationRoutes {
  def routes( userManager : UserManager ) : Route =
    path( "register" ) {
      get {
        getFromResource( "registrationForm.html" )
      } ~
        post {
          formFields( "username", "password" ) { ( username, password ) =>
            userManager.registerUser( username, password ) match {
              case Some( user ) =>
                // When a user logs in, we generate a unique token for them for identification
                complete( HttpEntity( ContentTypes.`application/json`, s"""{"token": "${user.getSessionId}"}""" ) )
              case None =>
                complete( "Registration not successful. User is already registered." )
            }
          }
        }
    }
}