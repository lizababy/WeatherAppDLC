package liza.weatherappdlc;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import liza.weatherappdlc.Api.ApiService;
import liza.weatherappdlc.Api.RetrofitClient;
import liza.weatherappdlc.Models.WeatherForecast;
import liza.weatherappdlc.Models.WeatherListItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements Callback<WeatherForecast> {

    public static final String LAT_INTENT_KEY = "latitude";
    public static final String LON_INTENT_KEY = "longitude";
    private ProgressDialog dialog;
    private View coordinatedLayoutView;
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        RecyclerView mRecyclerView = findViewById(R.id.listView);
        coordinatedLayoutView = findViewById(R.id.coordinator_layout);
        dialog = ProgressDialog.show(DetailActivity.this, "",
                "Loading. Please wait...", false);
        dialog.show();

        adapter = new CustomAdapter(this, new ArrayList<WeatherListItem>());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        if(getIntent() !=null){
            double latitude = getIntent().getDoubleExtra(LAT_INTENT_KEY,0);
            double longitude = getIntent().getDoubleExtra(LON_INTENT_KEY,0);
            requestWeatherForecastInfo(latitude,longitude);
        }

    }
    private void requestWeatherForecastInfo(double lat, double lon) {

        //make data request - api call
        ApiService apiService = RetrofitClient.getApiService(DetailActivity.this);
        //calling Json
        Log.d("location", "requestedWeatherInfo");

        Call<WeatherForecast> apiCall = apiService.getJsonWeatherForecast(lat, lon, this.getString(R.string.APP_ID));

        apiCall.enqueue(this);
    }


    @Override
    public void onResponse(Call<WeatherForecast> call, Response<WeatherForecast> response) {
        //dismiss dialog
        dialog.dismiss();
        if(response.isSuccessful()){
            if (response.body() != null) {
                WeatherForecast weatherForecast = response.body();
                if(weatherForecast != null) {
                    Log.d("city",weatherForecast.getCity().getName());
                    adapter.setData(weatherForecast.getTemperatureList());
                    adapter.notifyDataSetChanged();
                }
            }else {
                //show failure message
                showMessage("No response");
            }

        }
    }
    private void showMessage(String message){
        Snackbar.make(coordinatedLayoutView,message, Snackbar.LENGTH_LONG).show();
    }
    @Override
    public void onFailure(Call<WeatherForecast> call, Throwable t) {
        showMessage("Failed Network Request");
    }
}
