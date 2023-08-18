package chatapp
package routes

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Flow

object WebSocketRoutes {
  def websocketRoute : Route =
    path ( "ws" ) {
      handleWebSocketMessages( websocketFlow )
    }

  def websocketFlow : Flow[ Message, Message, Any ] = {
    Flow[ Message ].map {
      case TextMessage.Strict( text ) =>
        // Handle incoming message
        println( text )
        TextMessage( s"Received: $text" )
      case _ =>
        TextMessage( "Invalid message format" )
    }
  }

}
