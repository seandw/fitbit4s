package me.seandw
import org.cognoseed.fitbit.FitbitClient

object FitbitApp extends App {
  val client = FitbitClient()

  println(client.getResource("activities/steps/date/today/1m.json"))
}
