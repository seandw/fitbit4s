package me.seandw
import org.cognoseed.fitbit.FitbitClient
import org.json4s._
import org.json4s.native.JsonMethods._

object FitbitApp extends App {
  val client = FitbitClient()
  val res = client.getResource("activities/steps/date/today/1m.json")
  val ast = parse(res)

  println(ast)
  println(ast \\ "activities-steps")
  for {
    JArray(child) <- ast \\ "activities-steps"
    JObject(entry) <- child
    JField("dateTime", JString(dateTime)) <- entry
    JField("value", JString(value)) <- entry
  } println(dateTime + " -> " + value)
}
