package liza.weatherappdlc;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.net.InetAddress;

import liza.weatherappdlc.Models.WeatherResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<WeatherResponse> {

    private static final int REQUEST_PERMISSION_CODE = 101;
    TextView cityTV;
    TextView currentTV;
    protected static WeatherResponse weatherResponse;
    private Location userLocation;
    private View coordinatedLayoutView;
    private FusedLocationProviderClient mFusedLocationClient;
    private ProgressDialog dialog;

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

        dialog = ProgressDialog.show(MainActivity.this, "",
                "Loading. Please wait...", false);
        dialog.show();

        //checking internet connection
        if (!isNetworkConnected()) {
            showMessage("Not Connected to Internet!");
            dialog.dismiss();
            return;
        }
        //Getting User Location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastKnownLocation();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (weatherResponse != null) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getLastKnownLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            dialog.dismiss();
            //request permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
            //showMessage("No Permission!");

            return;
        }

        mFusedLocationClient.getLocationAvailability().addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                showMessage("location Not Available!");

            }
        }).addOnSuccessListener(this, new OnSuccessListener<LocationAvailability>() {
            @Override
            public void onSuccess(LocationAvailability locationAvailability) {

                //showMessage("location Available!");
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //request permission
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
                    dialog.dismiss();
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        showMessage("location Not found!");
                    }
                })
                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    userLocation = location;
                                    requestWeatherInfo();
                                } else {
                                    showMessage("location Invalid!");
                                    dialog.dismiss();
                                }


                            }
                        });
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case REQUEST_PERMISSION_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    showMessage("Permission Granted, Now your application can access GPS.");
                    getLastKnownLocation();
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
            dialog.dismiss();

        }
    }

    private void updateUI() {
        if(weatherResponse!=null) {
            cityTV.setText(weatherResponse.getCity().getName());
            currentTV.setText(new StringBuilder().append(weatherResponse.getTemperatureList().get(0).getWeatherMain().getTemp().toString()).append(getString(R.string.faren_unit)));
        }
    }

    @Override
    public void onFailure(Call<WeatherResponse> call, Throwable t) {
        //dismiss dialog
        //show failure message
        showMessage("Failed Network Request");
    }
}
