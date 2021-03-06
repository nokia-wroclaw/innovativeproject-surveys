name := """surveys2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

// https://mvnrepository.com/artifact/com.typesafe.play/play-mailer_2.11
libraryDependencies += "com.typesafe.play" %% "play-mailer" % "5.0.0"

libraryDependencies += javaJdbc
libraryDependencies += "com.adrianhurt" %% "play-bootstrap" % "1.0-P25-B3"
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
libraryDependencies += filters

libraryDependencies += "org.mindrot" % "jbcrypt" % "0.3m"

fork in run := true