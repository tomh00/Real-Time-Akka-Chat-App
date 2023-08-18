package chatapp
package routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
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

}
