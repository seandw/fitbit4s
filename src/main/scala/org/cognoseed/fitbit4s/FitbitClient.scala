package org.cognoseed.fitbit4s

import org.cognoseed.fitbit4s.model._

import java.net.URL
import java.util.Properties
import java.io.{InputStream, OutputStream}

import oauth.signpost.OAuthConsumer
import oauth.signpost.basic.DefaultOAuthProvider

import retrofit.RestAdapter

import se.akerfeldt.signpost.retrofit.RetrofitHttpOAuthConsumer
import org.cognoseed.retrofit.GsonNestedConverter
import org.cognoseed.retrofit.SigningUrlConnectionClient

import com.google.gson.Gson

import scala.collection.JavaConversions._

class FitbitClient(consumer: RetrofitHttpOAuthConsumer) {
  private lazy val provider =
    new DefaultOAuthProvider(
      FitbitClient.RequestTokenUrl,
      FitbitClient.AccessTokenUrl,
      FitbitClient.AuthorizeUrl
    )

  if (consumer.getConsumerKey == null || consumer.getConsumerSecret == null)
    throw new IllegalArgumentException("consumerKey/Secret cannot be null.")

  private val adapter = new RestAdapter.Builder()
    .setServer(FitbitClient.BaseUrl)
    .setClient(new SigningUrlConnectionClient(consumer))
    .setConverter(new GsonNestedConverter(new Gson())).build()
  private val service = adapter.create(classOf[FitbitService])

  def this(
    consumerKey: String,
    consumerSecret: String,
    accessToken: String = null,
    accessTokenSecret: String = null
  ) = {
    this(new RetrofitHttpOAuthConsumer(consumerKey, consumerSecret))
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

  def getTimeSeries(
    resource: String,
    end: String = "1m",
    start: String = "today"
  ): List[TimeSeriesRecord] = {
    if (!start.equals("today") && !FitbitClient.isDate(start))
      throw new IllegalArgumentException("start must be a date or \"today\"")
    if (!FitbitClient.isDate(end) && !FitbitClient.isRange(end))
      throw new IllegalArgumentException("end must be a date or range")

    val split = resource.split("/")

    if (split.length == 2)
      service.getTimeSeries(
        "-",
        split(0),
        split(1),
        start,
        end
      ).toList
    else
      service.getTimeSeries(
        "-",
        split(0),
        split(1),
        split(2),
        start,
        end
      ).toList
  }

  def getUserInfo(): User = {
    service.getUserInfo("-")
  }

}

object FitbitClient {
  val BaseUrl = "https://api.fitbit.com"
  val RequestTokenUrl = "https://api.fitbit.com/oauth/request_token"
  val AccessTokenUrl = "https://api.fitbit.com/oauth/access_token"
  val AuthorizeUrl = "https://www.fitbit.com/oauth/authorize"

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
