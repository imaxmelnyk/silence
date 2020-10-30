
organization := "com.imaxmelnyk"
name := "silence"
version := "0.1"
scalaVersion := "2.12.12"

resolvers += Resolver.url("bintray-sbt-plugins", url("https://dl.bintray.com/eed3si9n/sbt-plugins/"))(Resolver.ivyStylePatterns)

libraryDependencies += "com.lucidchart" %% "xtract" % "2.0.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "com.github.scopt" %% "scopt" % "4.0.0-RC2"
libraryDependencies ++= Seq("io.circe" %% "circe-core", "io.circe" %% "circe-generic", "io.circe" %% "circe-parser").map(_ % "0.12.3")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % Test

test in assembly := {}
assemblyJarName in assembly := "silence.jar"
