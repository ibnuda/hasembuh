import play.PlayScala

import scalariform.formatter.preferences._

name := """hasembuh"""

version := "2.0-RC2"

scalaVersion := "2.11.6"

resolvers := ("Atlassian Releases" at "https://maven.atlassian.com/public/") +: resolvers.value

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
	"com.mohiva" %% "play-silhouette" % "2.0",
	"org.webjars" %% "webjars-play" % "2.3.0",
	"org.webjars" % "bootstrap" % "3.1.1",
	"org.webjars" % "jquery" % "1.11.0",
	"com.typesafe.play" %% "play-slick" % "0.8.1",
	"net.codingwell" %% "scala-guice" % "4.0.0-beta5",
	"com.mohiva" %% "play-silhouette-testkit" % "2.0-RC2" % "test",
	"org.postgresql" % "postgresql" % "9.3-1102-jdbc4",
  "com.github.tminglei" %% "slick-pg" % "0.8.2",
  "org.apache.poi" % "poi" % "3.10.1",
  "org.apache.poi" % "poi-ooxml" % "3.10.1",
	cache
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalacOptions ++= Seq(
	"-deprecation", // Emit warning and location for usages of deprecated APIs.
	"-feature", // Emit warning and location for usages of features that should be imported explicitly.
	"-unchecked", // Enable additional warnings where generated code depends on assumptions.
	"-Xfatal-warnings", // Fail the compilation if there are any warnings.
	"-Xlint", // Enable recommended additional warnings.
	"-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
	"-Ywarn-dead-code", // Warn when dead code is identified.
	"-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
	"-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
	"-Ywarn-numeric-widen" // Warn when numerics are widened.
)

//********************************************************
// Scalariform settings
//********************************************************

defaultScalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
	.setPreference(FormatXml, false)
	.setPreference(DoubleIndentClassDeclaration, false)
	.setPreference(PreserveDanglingCloseParenthesis, true)


//fork in run := true