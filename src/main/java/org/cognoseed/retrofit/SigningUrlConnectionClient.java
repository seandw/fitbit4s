/*
 * Really just Patrik Akerfeldt's SigningOkClient with a different superclass.
 * https://github.com/pakerfeldt/signpost-retrofit/blob/master/src/main/java/se/akerfeldt/signpost/retrofit/SigningOkClient.java
 */

package org.cognoseed.retrofit;

import java.io.IOException;

import oauth.signpost.exception.*;
import retrofit.client.*;
import se.akerfeldt.signpost.retrofit.*;

public class SigningUrlConnectionClient extends UrlConnectionClient {

    public final RetrofitHttpOAuthConsumer consumer;

    public SigningUrlConnectionClient(RetrofitHttpOAuthConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public Response execute(Request request) throws IOException {
        Request requestToSend = request;
        try {
            HttpRequestAdapter signed =
                (HttpRequestAdapter) consumer.sign(request);
            requestToSend = (Request) signed.unwrap();
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        }
        return super.execute(requestToSend);
    }

}
