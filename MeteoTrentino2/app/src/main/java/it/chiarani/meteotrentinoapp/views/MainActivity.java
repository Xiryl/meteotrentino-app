package it.chiarani.meteotrentinoapp.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentinoapp.AppExecutors;
import it.chiarani.meteotrentinoapp.MeteoTrentinoApp;
import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoAPI;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataAPI;
import it.chiarani.meteotrentinoapp.api.RetrofitAPI;
import it.chiarani.meteotrentinoapp.config.Config;
import it.chiarani.meteotrentinoapp.databinding.ActivityMainBinding;
import it.chiarani.meteotrentinoapp.db.AppDatabase;
import it.chiarani.meteotrentinoapp.utils.GPSUtils;
import it.chiarani.meteotrentinoapp.utils.Localities;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private String[] mLocation;
    private RetrofitAPI meteoTrentinoAPI, openWeatherDataAPI;

    FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void setActivityBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutID());
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppThemeDark);
        } else {
            setTheme(R.style.AppTheme);
        }

        meteoTrentinoAPI = MeteoTrentinoAPI.getInstance();
        openWeatherDataAPI = OpenWeatherDataAPI.getInstance();

        mAppExecutors = ((MeteoTrentinoApp)getApplication()).getRepository().getAppExecutors();
        mAppDatabase = ((MeteoTrentinoApp)getApplication()).getRepository().getDatabase();

        binding.activityMainAnim.setAnimation(R.raw.anim_scan);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private boolean checkPermissions(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                1
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Granted. Start getting the location information
                getLastLocation();
            } else {
                callWithLastPositionIfExists();
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        task -> {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {
                                extractLocationAndRetrive(location);
                            }
                        }
                );
            } else {
               // GPS off
                callWithLastPositionIfExists();
            }
        } else {
            requestPermissions();
        }
    }

    private void extractLocationAndRetrive(Location location) {
        double lng = location.getLongitude();
        double lat = location.getLatitude();
        Geocoder gcd = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            callWithLastPositionIfExists();
        }
        if (addresses != null && addresses.size() > 0) {
            String[] loc =  new String[]{addresses.get(0).getLocality(), lat + "", lng + ""};
            retriveDataAndNext(loc, meteoTrentinoAPI, openWeatherDataAPI);
        } else {
            callWithLastPositionIfExists();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData(){

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            if(mLastLocation != null) {
                extractLocationAndRetrive(mLastLocation);
            }
        }
    };

    /*========*/

    @SuppressLint("CheckResult")
    private void retriveDataAndNext(String[] mLocation, RetrofitAPI meteoTrentinoAPI, RetrofitAPI openWeatherDataAPI) {
        //binding.activityMainAnim.setAnimation(R.raw.anim_scan);

        mDisposable.add(meteoTrentinoAPI.getMeteoTrentinoForecast(mLocation[0])
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError( err -> {
                    binding.activityMainAnim.setAnimation(R.raw.anim_no_network);
                    binding.activityMainAnim.playAnimation();
                    binding.activityMainDescr.setText("Oops. Controlla la rete e riprova.");
                })
                .subscribe(model -> {
                    mAppExecutors.diskIO().execute(() -> mAppDatabase.forecastDao().insert(model));
                    openWeatherDataAPI.getOpenWeatherDataForecast(Config.OPENWEATHERDATA_API_KEY, mLocation[1], mLocation[2])
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(openApiModel -> {
                                mAppExecutors.diskIO().execute(() -> mAppDatabase.openWeatherDataForecastDao().insert(openApiModel));
                                this.startActivity(new Intent(this, HomeActivity.class));
                                this.finish();
                            });
                }, throwable -> {
                    throwable.printStackTrace();
                    if(throwable instanceof java.net.UnknownHostException) {
                        binding.activityMainAnim.setAnimation(R.raw.anim_no_network);
                        binding.activityMainAnim.playAnimation();
                        binding.activityMainDescr.setText("Oops. Controlla la rete e riprova.");
                    } else {
                        binding.activityMainAnim.setAnimation(R.raw.anim_err);
                        binding.activityMainAnim.playAnimation();
                        binding.activityMainDescr.setText("Non riesco a collegarmi al server...");
                    }
                }));
    }

    private void callWithLastPositionIfExists() {
        mDisposable.add(mAppDatabase.forecastDao().getAsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .take(1)
                .subscribe(model -> {
                    if(model.size() >= 1) {
                        String[] mLocation = Localities.getLatLngFromLocation(model.get(model.size() -1).getPrevisione().get(0).getLocalita());
                        if(!mLocation[0].isEmpty()) {
                            Toast.makeText(this, "GPS non attivo, carico ultima posizione: " + mLocation[0], Toast.LENGTH_LONG).show();
                            retriveDataAndNext(mLocation, meteoTrentinoAPI, openWeatherDataAPI);
                            return;
                        } else {
                            Toast.makeText(this, "GPS non attivo, carico posizione default: TRENTO", Toast.LENGTH_LONG).show();
                            String[] tmpLocation = {"TRENTO", "46.071322", "11.120295"};
                            retriveDataAndNext(tmpLocation, meteoTrentinoAPI, openWeatherDataAPI);
                            return;
                        }
                    }
                    Toast.makeText(this, "GPS non attivo, carico posizione default: TRENTO", Toast.LENGTH_LONG).show();
                    String[] tmpLocation = {"TRENTO", "46.071322", "11.120295"};
                    retriveDataAndNext(tmpLocation, meteoTrentinoAPI, openWeatherDataAPI);
                }, throwable -> {
                    throwable.printStackTrace();
                    if(throwable instanceof java.net.UnknownHostException) {
                        binding.activityMainAnim.setAnimation(R.raw.anim_no_network);
                        binding.activityMainAnim.playAnimation();
                        binding.activityMainDescr.setText("Oops. Controlla la rete e riprova.");
                    } else {
                        binding.activityMainAnim.setAnimation(R.raw.anim_err);
                        binding.activityMainAnim.playAnimation();
                        binding.activityMainDescr.setText("Non riesco a collegarmi al server...");
                    }
                }));
    }
}


