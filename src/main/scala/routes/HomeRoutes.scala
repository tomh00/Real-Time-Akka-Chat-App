package chatapp
package routes

import akka.http.scaladsl.model.{FormData, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import chatapp.auth.UserManager

object HomeRoutes {

  // Define a simple route for the homepage
  val homeRoute: Route =
    path("") {
      get {
        complete("Hello, this is the homepage!")
      }
    }

  val registrationRoute: Route =
    path("register") {
      get {
        getFromResource("registrationForm.html")
      } ~
        post {
          formFields( "username", "password" ) { ( userName, password ) =>
            val success = register
            if (success) {
              complete("Registration successful")
            } else {
              complete("Registration failed")
            }

          }
          redirect("/registration-confirmation", StatusCodes.SeeOther)
        }
    }

  val registrationConfirmationRoute: Route =
    path("registration-confirmation" ) {
      get{
        complete( "Registration Successful" )
      }
    }


}
