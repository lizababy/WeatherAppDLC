package liza.weatherappdlc;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//http://api.openweathermap.org/data/2.5/forecast?lat=42.331429&lon=-83.045753&appid=926fb1e93ed8ac0e107542bc8fe98a69

    public static String baseUrl = "http://api.openweathermap.org";

    private static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService(){
        return getRetrofitInstance().create(ApiService.class);
    }

}
