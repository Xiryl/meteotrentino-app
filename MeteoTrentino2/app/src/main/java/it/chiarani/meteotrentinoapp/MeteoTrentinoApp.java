package it.chiarani.meteotrentinoapp;

import android.app.Application;

public class MeteoTrentinoApp extends Application {
    private AppExecutors mAppExecutors;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppExecutors = new AppExecutors();
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(getApplicationContext(), mAppExecutors);
    }
}
