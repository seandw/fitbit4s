package org.cognoseed.fitbit4s

import org.scalatest._
import prop._

import play.api.libs.json._

class UserRecordSpec extends PropSpec with Matchers {
  // Mock JSON user record.
  val json = 
    """
      {
        "avatar":"avatar",
        "avatar150":"avatar150",
        "city":"city",
        "country":"country",
        "dateOfBirth":"dateOfBirth",
        "displayName":"displayName",
        "distanceUnit":"distanceUnit",
        "encodedId":"encodedId",
        "foodsLocale":"foodsLocale",
        "fullName":"fullName",
        "gender":"gender",
        "glucoseUnit":"glucoseUnit",
        "height":8.8,
        "heightUnit":"heightUnit",
        "locale":"locale",
        "memberSince":"memberSince",
        "offsetFromUTCMillis":-28800000,
        "state":"state",
        "strideLengthRunning":1,
        "strideLengthWalking":2,
        "timezone":"timezone",
        "waterUnit":"waterUnit",
        "weight":9.9,
        "weightUnit":"weightUnit"
      }
    """
  
  property("a valid JSON record should result in a completely filled record") {
    val record = Json.parse(json).as[UserRecord]

    record.avatar should equal ("avatar")
    record.avatar150 should equal ("avatar150")
    record.city should equal ("city")
    record.country should equal ("country")
    record.dateOfBirth should equal ("dateOfBirth")
    record.displayName should equal ("displayName")
    record.distanceUnit should equal ("distanceUnit")
    record.encodedId should equal ("encodedId")
    record.foodsLocale should equal ("foodsLocale")
    record.fullName should equal ("fullName")
    record.gender should equal ("gender")
    record.glucoseUnit should equal ("glucoseUnit")
    record.height should equal (8.8)
    record.heightUnit should equal ("heightUnit")
    record.locale should equal ("locale")
    record.memberSince should equal ("memberSince")
    record.offsetFromUTCMillis should equal (-28800000)
    record.state should equal ("state")
    record.strideLengthRunning should equal (1)
    record.strideLengthWalking should equal (2)
    record.timezone should equal ("timezone")
    record.waterUnit should equal ("waterUnit")
    record.weight should equal (9.9)
    record.weightUnit should equal ("weightUnit")
  }

}
