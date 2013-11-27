package me.seandw

import java.io.FileInputStream
import java.util.Properties

import org.cognoseed.fitbit4s.FitbitClient
import org.cognoseed.fitbit4s.FitbitService

object FitbitApp extends App {
  val prop = new Properties()
  prop.load(new FileInputStream("config.properties"))
  val client = FitbitClient.fromProperties(prop)

  val user = client.getUserInfo()
  println(user.fullName)

  val series = client.getTimeSeries("activities/steps", "3m")
  series.toList foreach (r => println(r.dateTime + " -> " + r.value))
}
