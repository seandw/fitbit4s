package me.seandw

import java.io.FileInputStream
import java.util.Properties

import org.cognoseed.fitbit.FitbitClient

import org.json4s._
import org.json4s.native.JsonMethods._

object FitbitApp extends App {
  val prop = new Properties
  prop.load(new FileInputStream("config.properties"))
  val client = FitbitClient.fromProperties(prop)

  val res = client.getActivity("steps", "3m")
  val ast = parse(res)

  println(ast)
  println(ast \\ "activities-steps")

  for {
    JArray(child) <- ast \\ "activities-steps"
    JObject(entry) <- child
    JField("dateTime", JString(dateTime)) <- entry
    JField("value", JString(value)) <- entry
  } println(dateTime + " -> " + value)
}
