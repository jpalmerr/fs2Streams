ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val root = (project in file("."))
  .settings(
    name := "fs2Streams"
  )

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "3.2.0",
  "co.fs2" %% "fs2-core" % "3.2.3",
  "co.fs2" %% "fs2-io" % "3.2.3",
  "co.fs2" %% "fs2-reactive-streams" % "3.2.3",
  "co.fs2" %% "fs2-scodec" % "3.2.3"
)
