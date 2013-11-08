package org.cognoseed.fitbit4s

class FitbitRecord

case class TimeSeriesRecord(dateTime: String, value: Int)
  extends FitbitRecord
