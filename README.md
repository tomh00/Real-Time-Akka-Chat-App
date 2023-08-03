# ChatApp - Chat Application

ChatApp is a simple chat application built using Scala and Akka. It allows users to join a chat room, send messages, and leave the chat. The application is still in its early stages and currently has limited functionality, but it serves as a starting point for building a fully functional chat application.

## Features

- Users can join a chat room.
- Users can send messages to all other users in the chat room.
- Users can leave the chat room.

## Getting Started

### Prerequisites

- Scala 2.13.x
- SBT 1.x
- Akka 2.6.x

### Installation

1. Clone the repository.
```
git clone https://github.com/yourusername/chatapp.git
cd chatapp
```
2. Build the project using SBT.
```
sbt compile
```


## Usage

To run the chat application, use the `ChatApp` object in the `app` package. Currently, the application only allows users to join the chat room, and it lacks a UI or command-line interface for user interaction. However, you can implement the logic to handle user input and send messages to the chatActor to make the chat application fully functional.

```scala
package chatapp.app

import actors.ChatActor
import messages.JoinChat
import models.User

import akka.actor.{ActorSystem, Props}

object ChatApp extends App {
  val system = ActorSystem("ChatSystem")
  val chatActor = system.actorOf(Props[ChatActor], "chatActor")

  val user1 = User("Tom Higgins", system.actorOf(Props[UserActor], "userActor1"))
  val user2 = User("John Doe", system.actorOf(Props[UserActor], "userActor2"))

  // Users join the chat room
  chatActor ! JoinChat(user1)
  chatActor ! JoinChat(user2)

  // TODO: Implement the logic to handle user input, send messages to the chatActor, etc.
}
```


