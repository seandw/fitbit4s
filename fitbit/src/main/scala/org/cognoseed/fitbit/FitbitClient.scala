package org.cognoseed.fitbit

import java.net.URL
import java.util.Properties
import java.io.{FileInputStream, FileOutputStream}
import scala.io.BufferedSource

import oauth.signpost.basic.{DefaultOAuthConsumer, DefaultOAuthProvider}

class FitbitClient(
  consumerKey: String,
  consumerSecret: String)
extends FitbitEndpoints {
  val consumer = new DefaultOAuthConsumer(
    consumerKey,
    consumerSecret
  )

  def getAccessTokens() = {
    val provider = new DefaultOAuthProvider(
      RequestTokenUrl,
      AccessTokenUrl,
      AuthorizeUrl
    )

    val url = provider.retrieveRequestToken(consumer, null)
    println("Navigate to " + url + " to get your verifier.")
    
    print("Enter your verifier: ")
    val verifier = Console.readLine()
    provider.retrieveAccessToken(consumer, verifier)
    
    //saveTokens()
  }

  def setAccessTokens(accessToken: String, accessTokenSecret: String) =
    consumer.setTokenWithSecret(accessToken, accessTokenSecret)

  def getResource(resource: String): String = {
    val url = new URL(BaseUrl + resource)
    val request = url.openConnection()
    consumer.sign(request)
    request.connect()

    new BufferedSource(request.getInputStream()).mkString
  }
}

object FitbitClient {
  def apply() = {
    val (
      consumerKey,
      consumerSecret,
      accessToken,
      accessTokenSecret
    ) =
      try {
        val prop = new Properties()
        prop.load(new FileInputStream("config.properties"))

        (
          prop.getProperty("consumerKey"),
          prop.getProperty("consumerSecret"),
          prop.getProperty("accessToken"),
          prop.getProperty("accessTokenSecret")
        )
      } catch { case e: Exception =>
          e.printStackTrace()
          sys.exit(1)
      }

    val client = new FitbitClient(consumerKey, consumerSecret)

    if (accessToken == null || accessTokenSecret == null)
      client.getAccessTokens()
    else
      client.setAccessTokens(accessToken, accessTokenSecret)

    client
  }

}
