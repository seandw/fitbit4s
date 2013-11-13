package org.cognoseed.fitbit4s

import org.scalatest._
import prop._

import org.json4s._

class UserRecordSpec
    extends PropSpec
    with TableDrivenPropertyChecks
    with Matchers
{
  // Mock AST, info scrubbed. If we add validation, these will need to change.
  val ast = 
    JObject(
      List(
        ("avatar", JString("avatar")),
        ("avatar150", JString("avatar150")),
        ("city", JString("city")),
        ("country", JString("country")),
        ("dateOfBirth", JString("dateOfBirth")),
        ("displayName", JString("displayName")),
        ("distanceUnit", JString("distanceUnit")),
        ("encodedId", JString("encodedId")),
        ("foodsLocale", JString("foodsLocale")),
        ("fullName", JString("fullName")),
        ("gender", JString("gender")),
        ("glucoseUnit", JString("glucoseUnit")),
        ("height", JDouble(8.8)),
        ("heightUnit", JString("heightUnit")),
        ("locale", JString("locale")),
        ("memberSince", JString("memberSince")),
        ("offsetFromUTCMillis", JInt(-28800000)),
        ("state", JString("state")),
        ("strideLengthRunning", JInt(0)),
        ("strideLengthWalking", JInt(0)),
        ("timezone", JString("timezone")),
        ("waterUnit", JString("waterUnit")),
        ("weight", JDouble(8.8)),
        ("weightUnit", JString("weightUnit"))
      )
    )
  
  property("a valid AST should result in a completely filled record") {
    val record = UserRecord.fromAst(ast)

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
    record.height should equal ("8.8")
    record.heightUnit should equal ("heightUnit")
    record.locale should equal ("locale")
    record.memberSince should equal ("memberSince")
    record.offsetFromUTCMillis should equal ("-28800000")
    record.state should equal ("state")
    record.strideLengthRunning should equal ("0")
    record.strideLengthWalking should equal ("0")
    record.timezone should equal ("timezone")
    record.waterUnit should equal ("waterUnit")
    record.weight should equal ("8.8")
    record.weightUnit should equal ("weightUnit")
  }

}
