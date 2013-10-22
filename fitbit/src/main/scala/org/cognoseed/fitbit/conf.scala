package org.cognoseed.fitbit

import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream

object conf {

  var (
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

  def getAccess(): Unit = {
    val url = provider.retrieveRequestToken(consumer, null)
    println("Navigate to " + url + " to get your verifier.")
    
    print("Enter your verifier: ")
    val verifier = Console.readLine()
    provider.retrieveAccessToken(consumer, verifier)
    
    accessToken = consumer.getToken()
    accessTokenSecret = consumer.getTokenSecret()
    
    saveTokens()
  }

  private def saveTokens(): Unit =
    try {
      val prop = new Properties()
      prop.setProperty("consumerKey", consumerKey)
      prop.setProperty("consumerSecret", consumerSecret)
      prop.setProperty("accessToken", accessToken)
      prop.setProperty("accessTokenSecret", accessTokenSecret)
      prop.store(new FileOutputStream("config.properties"),
        "OAuth credentials for this application.")
    } catch { case e: Exception =>
        e.printStackTrace()
        sys.exit(2)
    }

}
