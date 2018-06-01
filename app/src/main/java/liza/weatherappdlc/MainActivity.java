package liza.weatherappdlc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.net.InetAddress;

import liza.weatherappdlc.Models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView cityTV;
    TextView currentTV;
    private WeatherResponse weatherResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        cityTV = (TextView) findViewById(R.id.city_textView);
        currentTV = (TextView) findViewById(R.id.temperature_textView);

        //get user location
        //checking internet connection
        if(isNetworkConnected()){

            //make data request - api call
            ApiService apiService = RetrofitClient.getApiService();
            //calling Json
            Call<WeatherResponse> apiCall = apiService.getJson();

            apiCall.enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    //dismiss dialog
                    if(response.isSuccessful()){
                        if (response.body() != null) {
                            weatherResponse = response.body();
                            Log.d("response",weatherResponse.getCity().getName());
                            if(weatherResponse!=null) {
                                cityTV.setText(weatherResponse.getCity().getName());
                                currentTV.setText(weatherResponse.getTemperatureList().get(0).getWeather().getTemp().toString() + "\u2109");
                            }
                        }
                    }else{
                        //show failure message

                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        //dismiss dialog
                        //show failure message
                }
            });


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }else{
            //show dialog -  not connected to internet
        }


        //convert json to pojo



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo() != null;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("api.openweathermap.org");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }
}
