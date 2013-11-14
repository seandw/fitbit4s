package org.cognoseed.fitbit4s

import play.api.libs.json._

class UserRecord(
  val aboutMe: String,
  val avatar: String,
  val avatar150: String,
  val city: String,
  val country: String,
  val dateOfBirth: String,
  val displayName: String,
  val distanceUnit: String,
  val encodedId: String,
  val foodsLocale: String,
  val fullName: String,
  val gender: String,
  val glucoseUnit: String,
  val height: Double,
  val heightUnit: String,
  val locale: String,
  val memberSince: String,
  val nickname: String,
  val offsetFromUTCMillis: Int,
  val state: String,
  val strideLengthRunning: Int,
  val strideLengthWalking: Int,
  val timezone: String,
  val waterUnit: String,
  val weight: Double,
  val weightUnit: String
)

object UserRecord {
  implicit val reader: Reads[UserRecord] = new Reads[UserRecord] {
    def reads(json: JsValue): JsResult[UserRecord] = JsSuccess[UserRecord](
      new UserRecord(
        (json \ "aboutMe").asOpt[String].getOrElse(""),
        (json \ "avatar").as[String],
        (json \ "avatar150").as[String],
        (json \ "city").as[String],
        (json \ "country").as[String],
        (json \ "dateOfBirth").as[String],
        (json \ "displayName").as[String],
        (json \ "distanceUnit").as[String],
        (json \ "encodedId").as[String],
        (json \ "foodsLocale").as[String],
        (json \ "fullName").as[String],
        (json \ "gender").as[String],
        (json \ "glucoseUnit").as[String],
        (json \ "height").as[Double],
        (json \ "heightUnit").as[String],
        (json \ "locale").as[String],
        (json \ "memberSince").as[String],
        (json \ "nickname").asOpt[String].getOrElse(""),
        (json \ "offsetFromUTCMillis").as[Int],
        (json \ "state").as[String],
        (json \ "strideLengthRunning").as[Int],
        (json \ "strideLengthWalking").as[Int],
        (json \ "timezone").as[String],
        (json \ "waterUnit").as[String],
        (json \ "weight").as[Double],
        (json \ "weightUnit").as[String]
      )
    )
  }
}
