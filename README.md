# Scala and Akka Chat App

Welcome to the Scala and Akka Chat App, a simple chat application built using Scala and the Akka actor model.

## Overview

This application implements a basic chat system where users can create and add other users to chat rooms, and exchange messages with other participants.

## Prerequisites

- Scala 2.x
- SBT (Simple Build Tool)

## Installation

1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/tomh00/Real-Time-Akka-Chat-App.git
    ```
2. Navigate to the project directory:
    ```
   cd chat-app
    ```
3. Compile the project using SBT:
    ```bash
    sbt compile
    ```
   
## Usage
### Run the chat app using SBT:
``` sbt
sbt run
```

### Register and Authenticate Users
- Open a web browser and navigate to http://localhost:8080/register to register a new user.
- Provide a username, email, and password to create your account.
- You will be redirected to the main section of the application.
- After registering, you also may navigate to http://localhost:8080/authenticate to log in with your newly created user credentials.

### Join the Chat Room
- Upon successful registration/authentication, you will be redirected to the main chat room.
- Here, you can join existing chats that you are a member of, and create new chats with other existing members.
- To create a new chat room, click "Create New Chat Room" button, provide a name for the chat room along with a comma seperated list of usernames you would like to add including your own username!
- To send messages in a chat room you must click on the button containing the name of the room.
- You can simulate multiple users and send each other messages by registering and authenticating as different users using different browsers.

### Exchange Messages
- Type your message in the input field at the bottom of the chat window.
- Click the "Send" button or press "Enter" to send your message to the chat.
- Messages sent by one user will be received by other authenticated users who are in the chat room in real-time.
- Experience the dynamic interaction of a chat environment!

## Features
- Create and add other users to chat rooms.
- Enter chat rooms as you choose.
- Exchange messages with other participants.
- Basic error handling for user actions.

## Testing
To run the tests for this project, use the following command:
    ```
    sbt test
    ```

## Acknowledgments

This project includes code based on the [akka-websockets-demo](https://github.com/amdelamar/akka-websockets-demo) repository by [amdelamar](https://github.com/amdelamar). I extend my gratitude for their open-source contribution, which inspired and assisted in the development of this chat application.

## Future Enhancements
The chat app is actively being developed, and its current state is a work-in-progress with planned improvements and refinements:

- Introduce message persistence for an uninterrupted chat experience.
- Enhance the user interface with modern and intuitive design.
- Integrate an external database for improved data management.
