name := "fitbit4s"

version := "0.0.1"

scalaVersion := "2.10.3"

resolvers += (
  "Play Maven Repository" at "http://repo.typesafe.com/typesafe/releases"
)

libraryDependencies ++= Seq(
  "oauth.signpost" % "signpost-core" % "1.2.1.2",
  "com.typesafe.play" %% "play-json" % "2.2.1",
  "com.squareup.retrofit" % "retrofit" % "1.2.2",
  "com.google.code.gson" % "gson" % "2.2.4",
  "org.scalatest" %% "scalatest" % "2.0" % "test"
)
