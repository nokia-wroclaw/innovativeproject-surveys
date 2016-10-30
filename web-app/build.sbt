name := """play-java"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

libraryDependencies ++= Seq(
  "io.swagger" %% "swagger-play2" % "1.5.1"
)

// https://mvnrepository.com/artifact/com.typesafe.play/play-mailer_2.11
libraryDependencies += "com.typesafe.play" % "play-mailer_2.11" % "5.0.0"

libraryDependencies += javaJdbc
libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"


fork in run := false

fork in run := false

fork in run := false