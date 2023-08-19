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
            userManager.registerUser(username, password) match {
              case Some( user ) =>
                // Create a unique token for user identification
                val userSessionToken = TokenUtility.generateToken(username)
                complete(HttpEntity(ContentTypes.`application/json`, s"""{"token": "$userSessionToken"}"""))
              case None =>
                complete("Registration not successful")
            }
          }
        }
    }
}