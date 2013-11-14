package me.seandw

import java.io.FileInputStream
import java.util.Properties

import org.cognoseed.fitbit4s.FitbitClient

object FitbitApp extends App {
  val prop = new Properties()
  prop.load(new FileInputStream("config.properties"))
  val client = FitbitClient.fromProperties(prop)

  val series = client.getTimeSeries("activities/steps")
  series foreach (r => println(r.dateTime + " -> " + r.value))

  val user = client.getUserInfo()
  println(user.fullName)
  println(user.nickname)
}
