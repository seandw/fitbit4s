package org.cognoseed.fitbit4s

import org.json4s._

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

// Too large for a case class means writing this ourselves...
class UserRecordSerializer extends CustomSerializer[UserRecord](format => (
  {
    case JObject(
      //JField("aboutMe", JString(aboutMe)) ::
      JField("avatar", JString(avatar)) ::
        JField("avatar150", JString(avatar150)) ::
        JField("city", JString(city)) ::
        JField("country", JString(country)) ::
        JField("dateOfBirth", JString(dateOfBirth)) ::
        JField("displayName", JString(displayName)) ::
        JField("distanceUnit", JString(distanceUnit)) ::
        JField("encodedId", JString(encodedId)) ::
        JField("foodsLocale", JString(foodsLocale)) ::
        JField("fullName", JString(fullName)) ::
        JField("gender", JString(gender)) ::
        JField("glucoseUnit", JString(glucoseUnit)) ::
        JField("height", JDouble(height)) ::
        JField("heightUnit", JString(heightUnit)) ::
        JField("locale", JString(locale)) ::
        JField("memberSince", JString(memberSince)) ::
        //JField("nickname", JString(nickname)) ::
        JField("offsetFromUTCMillis", JInt(offsetFromUTCMillis)) ::
        JField("state", JString(state)) ::
        JField("strideLengthRunning", JInt(strideLengthRunning)) ::
        JField("strideLengthWalking", JInt(strideLengthWalking)) ::
        JField("timezone", JString(timezone)) ::
        JField("waterUnit", JString(waterUnit)) ::
        JField("weight", JDouble(weight)) ::
        JField("weightUnit", JString(weightUnit)) :: Nil
    ) =>
      new UserRecord(
        "",
        avatar,
        avatar150,
        city,
        country,
        dateOfBirth,
        displayName,
        distanceUnit,
        encodedId,
        foodsLocale,
        fullName,
        gender,
        glucoseUnit,
        height,
        heightUnit,
        locale,
        memberSince,
        "",
        offsetFromUTCMillis.toInt,
        state,
        strideLengthRunning.toInt,
        strideLengthWalking.toInt,
        timezone,
        waterUnit,
        weight,
        weightUnit
      )
  },
  {
    case x: UserRecord =>
      JObject(
        JField("aboutMe", JString(x.aboutMe)) ::
          JField("avatar", JString(x.avatar)) ::
          JField("avatar150", JString(x.avatar150)) ::
          JField("city", JString(x.city)) ::
          JField("country", JString(x.country)) ::
          JField("dateOfBirth", JString(x.dateOfBirth)) ::
          JField("displayName", JString(x.displayName)) ::
          JField("distanceUnit", JString(x.distanceUnit)) ::
          JField("encodedId", JString(x.encodedId)) ::
          JField("foodsLocale", JString(x.foodsLocale)) ::
          JField("fullName", JString(x.fullName)) ::
          JField("gender", JString(x.gender)) ::
          JField("glucoseUnit", JString(x.glucoseUnit)) ::
          JField("height", JDouble(x.height)) ::
          JField("heightUnit", JString(x.heightUnit)) ::
          JField("locale", JString(x.locale)) ::
          JField("memberSince", JString(x.memberSince)) ::
          JField("nickname", JString(x.nickname)) ::
          JField("offsetFromUTCMillis", JInt(x.offsetFromUTCMillis)) ::
          JField("state", JString(x.state)) ::
          JField("strideLengthRunning", JInt(x.strideLengthRunning)) ::
          JField("strideLengthWalking", JInt(x.strideLengthWalking)) ::
          JField("timezone", JString(x.timezone)) ::
          JField("waterUnit", JString(x.waterUnit)) ::
          JField("weight", JDouble(x.weight)) ::
          JField("weightUnit", JString(x.weightUnit)) :: Nil
      )
  }
))
