package org.cognoseed

import oauth.signpost.basic.DefaultOAuthConsumer
import oauth.signpost.basic.DefaultOAuthProvider

package object fitbit {

    val provider = new DefaultOAuthProvider(
      endpoints.RequestToken,
      endpoints.AccessToken,
      endpoints.Authorize
    )

    val consumer = new DefaultOAuthConsumer(
      conf.consumerKey,
      conf.consumerSecret
    )

}
