package org.cognoseed.fitbit4s;

import retrofit.http.*;
import java.util.List;

public interface FitbitService {
    @GET("/1/user/{user}/profile.json")
    UserRecord getUserInfo(@Path("user") String user);

    @GET("/1/user/{user}/{group}/{type}/date/{date}/{range}.json")
    List<TimeSeriesRecord> getTimeSeries(@Path("user") String user, @Path("group") String group, @Path("type") String type, @Path("date") String date, @Path("range") String range);
}
