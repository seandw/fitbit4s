/*
 * Copyright (C) 2012 Square, Inc.
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
 * Added a step to fromBody to (hopefully) get the first nested object.
 *
 * ORIGINAL SOURCE:
 * https://github.com/square/retrofit/blob/master/retrofit/src/main/java/retrofit/converter/GsonConverter.java
 */

package org.cognoseed.retrofit;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;
import retrofit.converter.Converter;
import retrofit.converter.ConversionException;

public class GsonNestedConverter implements Converter {
    private final Gson gson;
    private String encoding;
    
    /**
     * Create an instance using the supplied {@link Gson} object for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public GsonNestedConverter(Gson gson) {
        this(gson, "UTF-8");
    }
    
    /**
     * Create an instance using the supplied {@link Gson} object for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use the specified encoding.
     */
    public GsonNestedConverter(Gson gson, String encoding) {
        this.gson = gson;
        this.encoding = encoding;
    }
    
    @Override public Object fromBody(TypedInput body, Type type) throws ConversionException {
        String charset = "UTF-8";
        if (body.mimeType() != null) {
            charset = MimeUtil.parseCharset(body.mimeType());
        }
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(body.in(), charset);
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(isr).getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet())
                return gson.fromJson(entry.getValue(), type);
        } catch (IOException e) {
            throw new ConversionException(e);
        } catch (JsonParseException e) {
            throw new ConversionException(e);
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException ignored) {
                }
            }
        }
        return null;
    }
    
    @Override public TypedOutput toBody(Object object) {
        try {
            return new JsonTypedOutput(gson.toJson(object).getBytes(encoding), encoding);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
    
    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;
        
        JsonTypedOutput(byte[] jsonBytes, String encode) {
            this.jsonBytes = jsonBytes;
            this.mimeType = "application/json; charset=" + encode;
        }
        
        @Override public String fileName() {
            return null;
        }
        
        @Override public String mimeType() {
            return mimeType;
        }
        
        @Override public long length() {
            return jsonBytes.length;
        }
        
        @Override public void writeTo(OutputStream out) throws IOException {
            out.write(jsonBytes);
        }
    }
    
}
