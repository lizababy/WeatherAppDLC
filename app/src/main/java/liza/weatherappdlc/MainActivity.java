package liza.weatherappdlc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;

import liza.weatherappdlc.Models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener, Callback<WeatherResponse> {

    private static final int REQUEST_PERMISSION_CODE = 101;
    private static final long MIN_TIME_BW_UPDATES = 0;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 100;
    TextView cityTV;
    TextView currentTV;
    private WeatherResponse weatherResponse;
    LocationManager locationManager;
    private Location userLocation;
    private View coordinatedLayoutView;
    private Criteria criteria;
    private Context context;
    private boolean isGpsEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        coordinatedLayoutView = findViewById(R.id.coordinator_layout);
        cityTV = findViewById(R.id.city_textView);
        currentTV = findViewById(R.id.temperature_textView);
        context = getApplicationContext();
        locationManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //checking internet connection
        if (!isNetworkConnected()) {
            showMessage("Not Connected to Internet!");
            return;
        }

        //get user location
        enableRuntimePermission();
        criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        checkGpsStatus();

        if(isGpsEnabled) {
            if (provider != null) {
                if (ActivityCompat.checkSelfPermission(
                        MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                userLocation = locationManager.getLastKnownLocation(provider);
                locationManager.requestLocationUpdates(provider, 12000, 7, MainActivity.this);
            }
        }else {
            showMessage("Please Enable GPS First");
        }
        //get Weather info with known location
        if (userLocation != null) {
            requestWeatherInfo();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                showMessage("Loading 5 day Forecast...");
            }
        });
    }

    private void checkGpsStatus() {

        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }

    private void enableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            showMessage("ACCESS_FINE_LOCATION permission allows us to Access GPS in app");
        } else {

            checkGpsStatus();
            if(!isGpsEnabled)
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);

        }
    }

    @SuppressLint("MissingPermission")
    private void requestLocation(){

        //request Permission
            if(permissionDenied())
                requestPermission();
            else
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,(LocationListener)this);

    }
    private boolean permissionDenied(){
        showMessage("Checking GPS Permissions...");

        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_CODE);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_PERMISSION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showMessage("Permission Granted, Now your application can access GPS.");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                } else {
                    showMessage("Permission Canceled, Now your application cannot access GPS.");

                }
                break;
        }

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
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkConnected() {
        showMessage("Checking Network Connection...");

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm != null && cm.getActiveNetworkInfo() != null;
    }
    private void showMessage(String message){
        Snackbar.make(coordinatedLayoutView,message, Snackbar.LENGTH_LONG).show();
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("api.openweathermap.org");
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d("location", "location changed");
        userLocation = location;

        requestWeatherInfo();
    }

    private void requestWeatherInfo() {

        //make data request - api call
        ApiService apiService = RetrofitClient.getApiService();
        //calling Json
        if(userLocation!=null) {
            Log.d("location", "requestedWeatherInfo");

            Call<WeatherResponse> apiCall = apiService.getJson(userLocation.getLatitude(), userLocation.getLongitude());

            apiCall.enqueue(this);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        requestWeatherInfo();
    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
        //dismiss dialog
        if(response.isSuccessful()){
            if (response.body() != null) {
                weatherResponse = response.body();
                //Log.d("response",weatherResponse.getCity().getName());
               updateUI();
            }else {
                //show failure message
                showMessage("No response");
            }

        }
    }

    private void updateUI() {
        if(weatherResponse!=null) {
            cityTV.setText(weatherResponse.getCity().getName());
            currentTV.setText(new StringBuilder().append(weatherResponse.getTemperatureList().get(0).getWeather().getTemp().toString()).append(getString(R.string.faren_unit)));
        }
    }

    @Override
    public void onFailure(Call<WeatherResponse> call, Throwable t) {
        //dismiss dialog
        //show failure message
        showMessage("Failed Network Request");
    }
}
