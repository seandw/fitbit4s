package org.cognoseed.fitbit

import java.net.URL
import java.util.Properties
import java.io.{FileInputStream, FileOutputStream}
import scala.io.BufferedSource

import oauth.signpost.OAuthConsumer
import oauth.signpost.basic.{DefaultOAuthConsumer, DefaultOAuthProvider}

class FitbitClient(consumer: OAuthConsumer) extends FitbitEndpoints {
  def getResource(resource: String): String = {
    val url = new URL(BaseUrl + resource)
    val request = url.openConnection()
    consumer.sign(request)
    request.connect()

    new BufferedSource(request.getInputStream()).mkString
  }
}

object FitbitClient extends FitbitEndpoints {
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

    val consumer = new DefaultOAuthConsumer(consumerKey, consumerSecret)

    if (accessToken == null || accessTokenSecret == null)
      getAccessTokens(consumer)
    else
      consumer.setTokenWithSecret(accessToken, accessTokenSecret)

    new FitbitClient(consumer)
  }

  private def getAccessTokens(consumer: OAuthConsumer) = {
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

    saveTokens(consumer)
  }

  private def saveTokens(consumer: OAuthConsumer) =
    try {
      val prop = new Properties()
      prop.setProperty("consumerKey", consumer.getConsumerKey)
      prop.setProperty("consumerSecret", consumer.getConsumerSecret)
      prop.setProperty("accessToken", consumer.getToken)
      prop.setProperty("accessTokenSecret", consumer.getTokenSecret)
      prop.store(new FileOutputStream("config.properties"),
        "OAuth credentials for this application.")
    } catch { case e: Exception =>
        e.printStackTrace()
        sys.exit(2)
    }
}
