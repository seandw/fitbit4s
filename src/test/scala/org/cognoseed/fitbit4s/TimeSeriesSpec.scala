package org.cognoseed.fitbit4s

import org.scalatest._
import prop._

import java.util.Properties
import java.io.{FileInputStream, IOException}

class TimeSeriesSpec
    extends PropSpec
    with TableDrivenPropertyChecks
    with Matchers
{
  
  val prop = new Properties()
  prop.load(new FileInputStream("config.properties"))
  val client = FitbitClient.fromProperties(prop)

  val validArgs =
    Table(
      "arg",
      "foods/log/caloriesIn",
      "foods/log/water",
      "activities/calories",
      "activities/caloriesBMR",
      "activities/steps",
      "activities/distance",
      "activities/minutesSedentary",
      "activities/minutesLightlyActive",
      "activities/minutesFairlyActive",
      "activities/minutesVeryActive",
      "activities/activityCalories",
      "activities/tracker/calories",
      "activities/tracker/steps",
      "activities/tracker/distance",
      "activities/tracker/minutesSedentary",
      "activities/tracker/minutesLightlyActive",
      "activities/tracker/minutesFairlyActive",
      "activities/tracker/minutesVeryActive",
      "activities/tracker/activityCalories",
      "sleep/startTime",
      "sleep/timeInBed",
      "sleep/minutesAsleep",
      "sleep/awakeningsCount",
      "sleep/minutesAwake",
      "sleep/minutesToFallAsleep",
      "sleep/minutesAfterWakeup",
      "sleep/efficiency",
      "body/weight",
      "body/bmi",
      "body/fat"
    )

  // I don't have a One or Ultra
  val invalidArgs =
    Table(
      "arg",
      "activities/floors",
      "activities/elevation",
      "activities/tracker/floors",
      "activities/tracker/elevation"
    )
      
  val invalidDates =
    Table(
      ("start", "end"),
      ("today", "2011-11-1"),     // Dates MUST be YYYY-MM-DD
      ("2011-11-11", "invalid"),  // end must be a date or a valid range
      ("11-11-11", "2011-11-11"),
      ("2011-1-11", "3m")
    )

  // This test is the worst. The creator should be shot.
  ignore("a valid time series should produce a nonempty list") {
    forAll (validArgs) { arg =>
      client.getTimeSeries(arg, "1w").size should be (7)
    }
  }

  // See above.
  ignore("an invalid time series should throw an exception") {
    forAll (invalidArgs) { arg =>
      evaluating {
        client.getTimeSeries(arg)
      } should produce [IOException]
    }
  }

  property("an invalid start/end should throw an exception") {
    forAll (invalidDates) { (start, end) =>
      evaluating {
        client.getTimeSeries("activities/steps", end, start)
      } should produce [IllegalArgumentException]
    }
  }

}
