package org.cognoseed.fitbit

import java.net.URL
import java.util.Properties
import java.io.{InputStream, OutputStream}
import scala.io.BufferedSource

import oauth.signpost.OAuthConsumer
import oauth.signpost.basic.{DefaultOAuthConsumer, DefaultOAuthProvider}

class FitbitClient(consumer: OAuthConsumer) extends FitbitEndpoints {

  def this(
    consumerKey: String,
    consumerSecret: String,
    accessToken: String,
    accessTokenSecret: String
  ) = {
    this(new DefaultOAuthConsumer(consumerKey, consumerSecret))
    consumer.setTokenWithSecret(accessToken, accessTokenSecret)
  }

  def getResource(resource: String): String = {
    val url = new URL(BaseUrl + resource)
    val request = url.openConnection()
    consumer.sign(request)
    request.connect()

    new BufferedSource(request.getInputStream()).mkString
  }

  def store(stream: OutputStream) = {
    val prop = new Properties()
    prop.setProperty("consumerKey", consumer.getConsumerKey)
    prop.setProperty("consumerSecret", consumer.getConsumerSecret)
    prop.setProperty("accessToken", consumer.getToken)
    prop.setProperty("accessTokenSecret", consumer.getTokenSecret)
    prop.store(stream, "OAuth credentials for this application.")
  }

  def getActivity(
    activity: String,
    range: String = "1m",
    start: String = "today"
  ) = {
    getResource("activities/"+activity+"/date/"+start+"/"+range+".json")
  }

}

object FitbitClient extends FitbitEndpoints {

  def fromProperties(prop: Properties): FitbitClient = {
    val (
      consumerKey,
      consumerSecret,
      accessToken,
      accessTokenSecret
    ) = (
      prop.getProperty("consumerKey"),
      prop.getProperty("consumerSecret"),
      prop.getProperty("accessToken"),
      prop.getProperty("accessTokenSecret")
    )
    
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
  }

}
