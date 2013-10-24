package org.cognoseed.fitbit

object Fitbit extends App {
  val client = new FitbitClient()

  println(client.getResource("activities/steps/date/today/1m.json"))
}
