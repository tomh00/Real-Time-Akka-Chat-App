# Scala and Akka Chat App

Welcome to the Scala and Akka Chat App, a simple chat application built using Scala and the Akka actor model.

## Overview

This application implements a basic chat system where users can join and leave the chat room and exchange messages with other participants.

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
1. Run the chat app using SBT:
    ```
   sbt run
   ```
2. Follow the on-screen prompts to interact with the chat app.

### Register and Authenticate Users
- Open a web browser and navigate to http://localhost:8080/register to register a new user.
- Provide a username, email, and password to create your account.
- After registering, navigate to http://localhost:8080/authenticate to log in with your newly created user credentials.

### Join the Chat Room
- Upon successful authentication, you will be redirected to the main chat room.
- Here, you can connect to the chat room using multiple browser windows or different browsers.
- Authenticate each connection as different users to simulate multiple participants.

### Exchange Messages
- Type your message in the input field at the bottom of the chat window.
- Click the "Send" button or press "Enter" to send your message to the chat.
- Messages sent by one user will be received by other authenticated users in real-time.
- Experience the dynamic interaction of a chat environment!

## Features
- Join and leave chat rooms.
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

- Implement multiple chat rooms for focused discussions.
- Introduce message persistence for an uninterrupted chat experience.
- Enhance the user interface with modern and intuitive design.
- Integrate an external database for improved data management.
