package chatapp
package routes

import akka.actor.ActorSystem
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
              redirect( "/username", StatusCodes.SeeOther )
            } else {
              complete("Registration not successful")
            }
          }
        }
    }


  val registrationConfirmationRoute: Route =
    path("registration-confirmation") {
      get {
        complete("Registration Successful")
      }
    }

  def usernamesRoute( userManager: UserManager ) : Route =
    path( "username" ) {
      get {
        val usernames = userManager.users
        complete( usernames.toString() )
      }
    }


}