/*
 * Copyright (C) 2013 Patrik Akerfeldt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* CHANGES FROM ORIGINAL SOURCE:
 * Different superclass (OkClient -> UrlConnectionClient)
 *
 * ORIGINAL SOURCE:
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
