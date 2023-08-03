ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.11"

lazy val root = (project in file("."))
  .settings(
    name := "Akka-Scala-Chat-App",
    idePackagePrefix := Some("chatapp")
  )

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.16"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.6.16" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test
