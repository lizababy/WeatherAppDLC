package liza.weatherappdlc.Api;

import liza.weatherappdlc.Models.CurrentWeather;
import liza.weatherappdlc.Models.WeatherForecast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    String currentWeatherUrlPath = "/data/2.5/weather?units=imperial";
    String forecastWeatherUrlPath = "/data/2.5/forecast?units=imperial";


    @GET(currentWeatherUrlPath)
    Call<CurrentWeather> getJsonCurrentWeather(@Query("lat") double latitude, @Query("lon") double longitude, @Query("appid")String appID);

    @GET(forecastWeatherUrlPath)
    Call<WeatherForecast> getJsonWeatherForecast(@Query("lat") double latitude, @Query("lon") double longitude, @Query("appid") String appID);
}
