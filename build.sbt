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
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.8.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % Test
libraryDependencies += "org.mindrot" % "jbcrypt" % "0.4"


