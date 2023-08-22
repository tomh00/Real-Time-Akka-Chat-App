ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "Akka-Scala-Chat-App",
    idePackagePrefix := Some("chatapp")
  )

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.8.0"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.8.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.5.0"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.0"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.8.0"
libraryDependencies += "com.typesafe.akka" %% "akka-serialization-jackson" % "2.8.0"
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.8.0"

libraryDependencies += "com.auth0" % "java-jwt" % "4.3.0"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.8.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % "10.5.0" % Test
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.4"
libraryDependencies += "org.scala-lang" % "scala-library" % scalaVersion.value
libraryDependencies += "org.java-websocket" % "Java-WebSocket" % "1.5.3"
libraryDependencies += "com.typesafe.play" %% "play" % "2.8.18"  // Replace 2.x.y with the Play version you want to use
libraryDependencies += "javax.inject" % "javax.inject" % "1"  // You can adjust the version if needed




