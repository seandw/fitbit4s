package me.seandw

import java.io.FileInputStream
import java.util.Properties

import org.cognoseed.fitbit4s.FitbitClient
import org.cognoseed.fitbit4s.TimeSeriesRecord

object FitbitApp extends App {
  val prop = new Properties
  prop.load(new FileInputStream("config.properties"))
  val client = FitbitClient.fromProperties(prop)

  val res = client.getActivity("steps", "3m")
  for (TimeSeriesRecord(dt, v) <- res)
    println(dt + " -> " + v)
}
