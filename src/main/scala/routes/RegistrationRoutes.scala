package chatapp
package routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import chatapp.auth.UserManager

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
              redirect( "/usernames", StatusCodes.SeeOther )
            } else {
              complete("Registration not successful")
            }
          }
        }
    }
}