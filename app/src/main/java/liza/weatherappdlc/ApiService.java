package liza.weatherappdlc;

import liza.weatherappdlc.Models.WeatherResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    @GET("/data/2.5/forecast?appid=926fb1e93ed8ac0e107542bc8fe98a69")
    Call<WeatherResponse> getJson(@Query("lat") double latitude, @Query("lon") double longitude);
}
