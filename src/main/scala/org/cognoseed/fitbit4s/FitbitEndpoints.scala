package org.cognoseed.fitbit4s

trait FitbitEndpoints {
  protected val BaseUrl = "https://api.fitbit.com/1/user/-/"
  protected val RequestTokenUrl = "https://api.fitbit.com/oauth/request_token"
  protected val AccessTokenUrl = "https://api.fitbit.com/oauth/access_token"
  protected val AuthorizeUrl = "https://www.fitbit.com/oauth/authorize"
}
