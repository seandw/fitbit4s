package me.seandw

import java.io.FileInputStream
import java.util.Properties

import org.cognoseed.fitbit4s.FitbitClient
import org.cognoseed.fitbit4s.TimeSeriesRecord

object FitbitApp extends App {
  val prop = new Properties()
  prop.load(new FileInputStream("config.properties"))
  val client = FitbitClient.fromProperties(prop)

  val map = client.getUserInfo()
  for ((key, value) <- map) println(key + " -> " + value)
}
