package it.chiarani.meteotrentino.views;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentino.AppExecutors;
import it.chiarani.meteotrentino.MeteoTrentinoApp;
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.api.MeteoTrentinoAPI;
import it.chiarani.meteotrentino.api.RetrofitAPI;
import it.chiarani.meteotrentino.databinding.ActivityMainBinding;
import it.chiarani.meteotrentino.db.AppDatabase;
import it.chiarani.meteotrentino.utils.GPSUtils;
import it.chiarani.meteotrentino.utils.Localities;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;
    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

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

        mAppExecutors = ((MeteoTrentinoApp)getApplication()).getRepository().getAppExecutors();
        mAppDatabase = ((MeteoTrentinoApp)getApplication()).getRepository().getDatabase();


        mDisposable.add(meteoTrentinoAPI.getForecast("ARCO")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {

                    mAppExecutors.diskIO().execute(() -> mAppDatabase.forecastDao().insert(model));

                    Toast.makeText(this, "Ok data", Toast.LENGTH_SHORT).show();

                    this.startActivity(new Intent(this, HomeActivity.class));
                }, throwable -> {
                    Toast.makeText(this, "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }

    private void accessLocation(boolean isGPSGranted) {
        if(isGPSGranted) {
            // access to gps location
            String loc = GPSUtils.getLocation(this);
            boolean locationExists = Localities.checkIfLocationExists(loc);
            if(locationExists) {
                Toast.makeText(this, String.format("Rilevato: %s", loc), Toast.LENGTH_SHORT).show();
                // download data
            } else {
                // use previous location
                Toast.makeText(this, String.format("%s non valido", loc), Toast.LENGTH_SHORT).show();
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
