package liza.weatherappdlc.Api;

import android.content.Context;

import liza.weatherappdlc.Api.ApiService;
import liza.weatherappdlc.MainActivity;
import liza.weatherappdlc.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {


    private static Retrofit getRetrofitInstance(Context context){
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.API_BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getApiService(Context context) {
        return getRetrofitInstance(context).create(ApiService.class);
    }
}
