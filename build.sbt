name := "fitbit4s"

version := "0.0.2"

scalaVersion := "2.10.3"

libraryDependencies ++= Seq(
  "oauth.signpost" % "signpost-core" % "1.2.1.2",
  "com.squareup.retrofit" % "retrofit" % "1.2.2",
  "com.google.code.gson" % "gson" % "2.2.4",
  "org.scalatest" %% "scalatest" % "2.0" % "test"
)
