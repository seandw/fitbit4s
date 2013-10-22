package org.cognoseed.fitbit

import java.net.URL
import scala.io.BufferedSource

object Fitbit extends App {
  if (conf.accessToken == null || conf.accessTokenSecret == null)
    conf.getAccess()

  consumer.setTokenWithSecret(conf.accessToken, conf.accessTokenSecret)
  
  val url = new URL(endpoints.BaseUrl + "activities/steps/date/today/1m.json")
  val request = url.openConnection()
  consumer.sign(request)
  println("Sending request to Fitbit...")

  request.connect()

  println(new BufferedSource(request.getInputStream()).mkString)
}
