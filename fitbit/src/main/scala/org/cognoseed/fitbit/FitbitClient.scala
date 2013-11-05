package org.cognoseed.fitbit

import java.net.URL
import java.util.Properties
import java.io.{InputStream, OutputStream}
import scala.io.BufferedSource

import oauth.signpost.OAuthConsumer
import oauth.signpost.basic.{DefaultOAuthConsumer, DefaultOAuthProvider}

class FitbitClient(consumer: OAuthConsumer) extends FitbitEndpoints {
  private lazy val provider =
    new DefaultOAuthProvider(
      RequestTokenUrl,
      AccessTokenUrl,
      AuthorizeUrl
    )

  def this(
    consumerKey: String,
    consumerSecret: String,
    accessToken: String = null,
    accessTokenSecret: String = null
  ) = {
    this(new DefaultOAuthConsumer(consumerKey, consumerSecret))
    if (accessToken != null && accessTokenSecret != null)
      consumer.setTokenWithSecret(accessToken, accessTokenSecret)
  }

  def getResource(resource: String): String = {
    val url = new URL(BaseUrl + resource)
    val request = url.openConnection()
    consumer.sign(request)
    request.connect()

    new BufferedSource(request.getInputStream()).mkString
  }

  def store(stream: OutputStream): Unit = {
    val prop = new Properties()
    prop.setProperty("consumerKey", consumer.getConsumerKey)
    prop.setProperty("consumerSecret", consumer.getConsumerSecret)
    prop.setProperty("accessToken", consumer.getToken)
    prop.setProperty("accessTokenSecret", consumer.getTokenSecret)
    prop.store(stream, "OAuth credentials for this application.")
  }

  def getAccessTokens(): Unit = {
    val url = provider.retrieveRequestToken(consumer, null)
    println("Navigate to " + url + " to get your verifier.")

    print("Enter your verifier: ")
    val verifier = Console.readLine()
    provider.retrieveAccessToken(consumer, verifier)
  }


  def getActivity(
    activity: String,
    range: String = "1m",
    start: String = "today"
  ): String = {
    getResource("activities/"+activity+"/date/"+start+"/"+range+".json")
  }

}

object FitbitClient {
  def fromProperties(prop: Properties): FitbitClient = {
    new FitbitClient(
      prop.getProperty("consumerKey"),
      prop.getProperty("consumerSecret"),
      prop.getProperty("accessToken"),
      prop.getProperty("accessTokenSecret")
    )
  }
}
