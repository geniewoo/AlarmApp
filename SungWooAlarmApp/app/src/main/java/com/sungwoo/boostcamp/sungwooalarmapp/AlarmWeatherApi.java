package com.sungwoo.boostcamp.sungwooalarmapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by psw10 on 2017-01-28.
 */
// 날씨정보를 가져오기 위한 Retrofit interface이다
public interface AlarmWeatherApi {
    @Headers({"Accept: application/json","appKey: fc369937-da59-33ff-b66d-14fc5c60cb86"})
    @GET("weather/current/minutely")
    Call<WeatherGson> getCurrentWeather(@Query("version") int version, @Query("lat") String lat, @Query("lon") String lon);
}
