package org.cognoseed.fitbit

import java.net.URL
import scala.io.BufferedSource

import oauth.signpost.basic.DefaultOAuthConsumer

class FitbitClient {
  val conf = new FitbitConfiguration()
  val consumer = new DefaultOAuthConsumer(
    conf.consumerKey,
    conf.consumerSecret
  )

  if (conf.accessToken == null || conf.accessTokenSecret == null)
    conf.getAccess(consumer)

  consumer.setTokenWithSecret(conf.accessToken, conf.accessTokenSecret)

  def getResource(resource: String): String = {
    val url = new URL(endpoints.BaseUrl + resource)
    val request = url.openConnection()
    consumer.sign(request)
    request.connect()

    new BufferedSource(request.getInputStream()).mkString
  }
}
