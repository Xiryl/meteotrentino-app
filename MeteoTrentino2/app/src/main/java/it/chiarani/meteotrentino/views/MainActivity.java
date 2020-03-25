package it.chiarani.meteotrentino.views;

import android.annotation.SuppressLint;
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
import it.chiarani.meteotrentino.api.MeteoTrentinoStationsAPI;
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
    private RetrofitAPI meteoTrentinoAPI, openWeatherDataAPI;

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

        boolean isGPSGranted = GPSUtils.checkGPSPermissions(this);

        meteoTrentinoAPI = MeteoTrentinoAPI.getInstance();
        openWeatherDataAPI = OpenWeatherDataAPI.getInstance();

        mAppExecutors = ((MeteoTrentinoApp)getApplication()).getRepository().getAppExecutors();
        mAppDatabase = ((MeteoTrentinoApp)getApplication()).getRepository().getDatabase();

        accessLocation(isGPSGranted);
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

            if (mLocation[0].isEmpty()) {
                callWithLastPositionIfExists();
            }

            else {
                boolean locationExists = Localities.checkIfLocationExists(mLocation[0]);
                if(locationExists) {
                    Toast.makeText(this, String.format("Rilevato: %s", mLocation[0]), Toast.LENGTH_SHORT).show();
                    retriveDataAndNext(mLocation, meteoTrentinoAPI, openWeatherDataAPI);
                } else {
                    // use previous location
                    Toast.makeText(this, String.format("%s non valido", mLocation[0]), Toast.LENGTH_SHORT).show();
                }
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
                    binding.activityMainAnim.setAnimation(R.raw.anim_err);
                    binding.activityMainAnim.playAnimation();
                    binding.activityMainDescr.setText("Non riesco a collegarmi al GPS...");
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private void retriveDataAndNext(String[] mLocation, RetrofitAPI meteoTrentinoAPI, RetrofitAPI openWeatherDataAPI) {
        mDisposable.add(meteoTrentinoAPI.getMeteoTrentinoForecast(mLocation[0])
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                        }
                    }
                    Toast.makeText(this, "GPS non attivo, carico posizione default: TRENTO", Toast.LENGTH_LONG).show();
                    String[] tmpLocation = {"TRENTO", "46.071322", "11.120295"};
                    retriveDataAndNext(tmpLocation, meteoTrentinoAPI, openWeatherDataAPI);
                }, throwable -> {
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
