package chatapp
package routes

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import chatapp.auth.{TokenUtility, UserManager}

object RegistrationRoutes {
  def routes(userManager: UserManager): Route =
    path("register") {
      get {
        getFromResource("registrationForm.html" )
      } ~
        post {
          formFields("username", "password") { (username, password) =>
            val success = userManager.registerUser(username, password)
            if (success) {
              // Create a unique token for user identification
              val userSessionToken = TokenUtility.generateToken( username )
              complete( HttpEntity( ContentTypes.`application/json`, s"""{"token": "$userSessionToken"}""" ) )
            } else {
              complete("Registration not successful")
            }
          }
        }
    }
}