package org.cognoseed.fitbit

trait FitbitEndpoints {
  val BaseUrl = "https://api.fitbit.com/1/user/-/"
  val RequestTokenUrl = "https://api.fitbit.com/oauth/request_token"
  val AccessTokenUrl = "https://api.fitbit.com/oauth/access_token"
  val AuthorizeUrl = "https://www.fitbit.com/oauth/authorize"
}
