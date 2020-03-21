package it.chiarani.meteotrentino.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentino.AppExecutors;
import it.chiarani.meteotrentino.MeteoTrentinoApp;
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.api.MeteoTrentinoAPI;
import it.chiarani.meteotrentino.api.OpenWeatherDataAPI;
import it.chiarani.meteotrentino.api.RetrofitAPI;
import it.chiarani.meteotrentino.config.Config;
import it.chiarani.meteotrentino.databinding.ActivityMainBinding;
import it.chiarani.meteotrentino.db.AppDatabase;
import it.chiarani.meteotrentino.utils.GPSUtils;
import it.chiarani.meteotrentino.utils.Localities;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private String[] mLocation;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void setActivityBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutID());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isGPSGranted = GPSUtils.checkGPSPermissions(this);

        accessLocation(isGPSGranted);

        RetrofitAPI meteoTrentinoAPI = MeteoTrentinoAPI.getInstance();
        RetrofitAPI openWeatherDataAPI = OpenWeatherDataAPI.getInstance();

        mAppExecutors = ((MeteoTrentinoApp)getApplication()).getRepository().getAppExecutors();
        mAppDatabase = ((MeteoTrentinoApp)getApplication()).getRepository().getDatabase();


        Consumer<Throwable> throwableConsumer = throwable -> Toast.makeText(this, "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();

        mDisposable.add(meteoTrentinoAPI.getMeteoTrentinoForecast(mLocation[0])
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(model -> {
                    mAppExecutors.diskIO().execute(() -> mAppDatabase.forecastDao().insert(model));
                    return openWeatherDataAPI.getOpenWeatherDataForecast(Config.OPENWEATHERDATA_API_KEY, mLocation[1], mLocation[2])
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                })
                .subscribe(model -> {
                    mAppExecutors.diskIO().execute(() -> mAppDatabase.openWeatherDataForecastDao().insert(model));
                    this.startActivity(new Intent(this, HomeActivity.class));
                    this.finish();
                }, throwableConsumer));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void accessLocation(boolean isGPSGranted) {
        if(isGPSGranted) {
            // access to gps location
            mLocation = GPSUtils.getLocation(this);
            boolean locationExists = Localities.checkIfLocationExists(mLocation[0]);
            if(locationExists) {
                Toast.makeText(this, String.format("Rilevato: %s", mLocation[0]), Toast.LENGTH_SHORT).show();
                // download data
            } else {
                // use previous location
                Toast.makeText(this, String.format("%s non valido", mLocation[0]), Toast.LENGTH_SHORT).show();
            }
        } else {
            GPSUtils.askGPSPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accessLocation(true);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
