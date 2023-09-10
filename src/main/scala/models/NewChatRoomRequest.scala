package chatapp
package models

import spray.json.{DefaultJsonProtocol, RootJsonFormat, enrichAny}

case class NewChatRoomRequest( name : String, users : List[ String ] )

/*object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val newChatRoomRequestFormat: RootJsonFormat[ NewChatRoomRequest ] = jsonFormat2( NewChatRoomRequest )
}*/

