package chatapp
package routes

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Route
import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

import scala.concurrent.ExecutionContextExecutor

// Case class representing a simple JSON entity
case class Person(name: String, age: Int)

object JsonHandlingExample {
  implicit val system: ActorSystem = ActorSystem("json-handling-example")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // JSON format for Person case class
  implicit val personFormat: RootJsonFormat[Person] = jsonFormat2(Person)

  val route = path("person") {
    get {
      val person = Person("Alice", 30) // Create a sample Person object
      complete( person ) // Automatically serialize the Person object to JSON
    }
  }

  val displayPersonRoute : Route =
    path ( "person-display" ) {
      get {
        getFromResource( "exampleOfBackToFront.html" )
      }
    }
}
