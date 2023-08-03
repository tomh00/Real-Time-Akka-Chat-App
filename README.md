# Chat Application with Akka (Scala, Akka)

This project is a real-time chat application built using Scala and Akka. The application utilizes Akka's Actor model to manage real-time communication between users.

## Project Overview

The current implementation of the project includes the following components:

- **ChatActor**: An Akka actor that receives and handles messages. It currently logs any received message.

- **ChatApp**: The main application class that initializes the ActorSystem and creates an instance of the ChatActor.

## How to Run

To run the Chat Application, follow these steps:

1. Make sure you have [Scala](https://www.scala-lang.org/) and [Akka](https://akka.io/) installed on your machine.

2. Clone the project repository to your local machine.

3. Open the project in [IntelliJ IDEA](https://www.jetbrains.com/idea/) or any other compatible Scala IDE.

4. Navigate to the `ChatApp` object located at `chatapp.app.ChatApp`.

5. Run the `ChatApp` object. This will start the ActorSystem and create the ChatActor.

6. The application will log a message indicating that the ChatActor has received a message. However, the current implementation only logs the messages and does not have complete message handling logic.

## Next Steps

The current implementation serves as a foundation for the Chat Application. The next steps in the development process include:

- Implementing message handling logic inside the ChatActor to handle different types of messages, such as joining the chat room, leaving the chat room, sending messages to specific users, and handling commands.

- Adding support for multiple users and managing user interactions.

- Creating a User Interface (UI) to allow users to interact with the chat application.

- Writing comprehensive unit tests and integration tests to ensure the application works as expected.

- Deploying the application to a cloud service or hosting it on a server to make it accessible to users from different machines.

## Contributions

Contributions to the project are welcome. If you encounter any issues or have ideas for improvements, you can open an issue or create a pull request. Your contributions will be valuable to the project's progress.

## License

This project is open-source and available under the [MIT License](LICENSE). You are free to use, modify, and distribute it under the terms of the license.

---

_This README summarizes the current state of the Chat Application with Akka. It will be updated as the project progresses and new features are added._
