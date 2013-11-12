package org.cognoseed.fitbit4s

// Things just got dirty.
import scala.reflect.runtime.{universe => ru}

import org.json4s._

class UserRecord(
  var aboutMe: String = "",
  var avatar: String = "",
  var avatar150: String = "",
  var city: String = "",
  var country: String = "",
  var dateOfBirth: String = "",
  var displayName: String = "",
  var distanceUnit: String = "",
  var encodedId: String = "",
  var foodsLocale: String = "",
  var fullName: String = "",
  var gender: String = "",
  var glucoseUnit: String = "",
  var height: String = "",
  var heightUnit: String = "",
  var locale: String = "",
  var memberSince: String = "",
  var nickname: String = "",
  var offsetFromUTCMillis: String = "",
  var state: String = "",
  var strideLengthRunning: String = "",
  var strideLengthWalking: String = "",
  var timezone: String = "",
  var waterUnit: String = "",
  var weight: String = "",
  var weightUnit: String = ""
)

object UserRecord {
  def fromAst(ast: JValue): UserRecord = {
    val record = new UserRecord()
    val mirror = ru.runtimeMirror(record.getClass.getClassLoader)
    val reflection = mirror.reflect(record)

    for (JObject(child) <- ast; JField(key, value) <- child)
      try {
        val term =
          ru.typeOf[UserRecord].declaration(ru.newTermName(key)).asTerm
        val field = reflection.reflectField(term)

        value match {
          case JString(s) => field.set(s)
          case JDouble(num) => field.set(num.toString)
          case JInt(num) => field.set(num.toString)
          case _ => () // _Someone_ changed the API.
        }
      } catch {
        case e: ScalaReflectionException => () // _Someone_ changed the API.
      }

    record
  }
}
