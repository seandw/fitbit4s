package org.cognoseed.fitbit4s

import play.api.libs.json._

case class TimeSeriesRecord(dateTime: String, value: String)

object TimeSeriesRecord {
  implicit val reader: Reads[TimeSeriesRecord] = new Reads[TimeSeriesRecord] {
    def reads(json: JsValue): JsResult[TimeSeriesRecord] =
      JsSuccess[TimeSeriesRecord](
        TimeSeriesRecord(
          (json \ "dateTime").as[String],
          (json \ "value").as[String]
        )
      )
  }
}
