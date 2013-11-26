package me.seandw

import java.io.FileInputStream
import java.util.Properties

import org.cognoseed.fitbit4s.FitbitClient
import org.cognoseed.fitbit4s.FitbitService

import retrofit._
import se.akerfeldt.signpost.retrofit._
import org.cognoseed.retrofit._
import com.google.gson._
import scala.collection.JavaConversions._

object FitbitApp extends App {
  val prop = new Properties()
  prop.load(new FileInputStream("config.properties"))
  val consumer = new RetrofitHttpOAuthConsumer(
    prop.getProperty("consumerKey"),
    prop.getProperty("consumerSecret")
  )
  consumer.setTokenWithSecret(
    prop.getProperty("accessToken"),
    prop.getProperty("accessTokenSecret")
  )

  val adapter = new RestAdapter.Builder().setServer("https://api.fitbit.com").setClient(new SigningUrlConnectionClient(consumer)).setConverter(new GsonNestedConverter(new Gson())).build()
  val service = adapter.create(classOf[FitbitService])

  val user = service.getUserInfo("-")
  println(user.fullName)

  val series = service.getTimeSeries("-", "activities", "steps", "today", "3m")
  series.toList foreach (r => println(r.dateTime + " -> " + r.value))
}
