package org.cognoseed.fitbit4s

import java.net.URL
import java.util.Properties
import java.io.{InputStream, OutputStream}

import scala.io.BufferedSource
import scala.collection.immutable.Set

import oauth.signpost.OAuthConsumer
import oauth.signpost.basic.{DefaultOAuthConsumer, DefaultOAuthProvider}

import org.json4s._
import org.json4s.native.JsonMethods._

class FitbitClient(consumer: OAuthConsumer) extends FitbitEndpoints {
  private lazy val provider =
    new DefaultOAuthProvider(
      RequestTokenUrl,
      AccessTokenUrl,
      AuthorizeUrl
    )

  if (consumer.getConsumerKey == null || consumer.getConsumerSecret == null)
    throw new IllegalArgumentException("consumerKey/Secret cannot be null.")

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

  def accessTokenUrl: String =
    provider.retrieveRequestToken(consumer, null)
    
  def getAccessTokens(verifier: String): Unit =
    provider.retrieveAccessToken(consumer, verifier)

  def store(stream: OutputStream): Unit = {
    val prop = new Properties()
    prop.setProperty("consumerKey", consumer.getConsumerKey)
    prop.setProperty("consumerSecret", consumer.getConsumerSecret)
    prop.setProperty("accessToken", consumer.getToken)
    prop.setProperty("accessTokenSecret", consumer.getTokenSecret)
    prop.store(stream, "OAuth credentials for this application.")
  }

  def getResource(resource: String): String = {
    val url = new URL(BaseUrl + resource)
    val request = url.openConnection()
    consumer.sign(request)
    request.connect()

    new BufferedSource(request.getInputStream()).mkString
  }

  def getTimeSeries(
    resource: String,
    end: String = "1m",
    start: String = "today"
  ): List[TimeSeriesRecord] = {
    if (!start.equals("today") && !FitbitClient.isDate(start))
      throw new IllegalArgumentException("start must be a date or \"today\"")
    if (!FitbitClient.isDate(end) && !FitbitClient.isRange(end))
      throw new IllegalArgumentException("end must be a date or range")

    val json = getResource(resource + "/date/" + start + "/" + end + ".json")
    val ast = parse(json)

    for {
      JArray(child) <- ast \\ resource.replace('/', '-')
      JObject(entry) <- child
      JField("dateTime", JString(dateTime)) <- entry
      JField("value", JString(value)) <- entry
    } yield TimeSeriesRecord(dateTime, value)
  }

}

object FitbitClient {
  private val range =
    Set("1d", "7d", "30d", "1w", "1m", "3m", "6m", "1y", "max")

  def fromProperties(prop: Properties): FitbitClient = {
    new FitbitClient(
      prop.getProperty("consumerKey"),
      prop.getProperty("consumerSecret"),
      prop.getProperty("accessToken"),
      prop.getProperty("accessTokenSecret")
    )
  }

  def isRange(in: String): Boolean =
    range.contains(in)

  def isDate(in: String): Boolean =
    in.matches("""(\d\d\d\d)-(\d\d)-(\d\d)""")
}
