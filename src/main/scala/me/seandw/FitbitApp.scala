package me.seandw

import java.io.FileInputStream
import java.util.Properties

import org.cognoseed.fitbit.FitbitClient
import org.cognoseed.fitbit.TimeSeriesResource

object FitbitApp extends App {
  val prop = new Properties
  prop.load(new FileInputStream("config.properties"))
  val client = FitbitClient.fromProperties(prop)

  val res = client.getActivity("steps", "3m")
  for (TimeSeriesResource(dt, v) <- res)
    println(dt + " -> " + v)
}
