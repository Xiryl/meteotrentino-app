package it.chiarani.meteotrentinoapp.api;

import java.util.Arrays;

import it.chiarani.meteotrentinoapp.config.Config;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MeteoTrentinoStationsAPI {
    private static volatile RetrofitAPI INSTANCE;

    public static RetrofitAPI getInstance() {
        if (INSTANCE == null) {
            synchronized (RetrofitAPI.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Retrofit.Builder()
                            .baseUrl(Config.METEOTRENTINO_STATION_LIST)
                            .addConverterFactory(SimpleXmlConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(new OkHttpClient())
                            .build()
                            .create(RetrofitAPI.class);
                }
            }
        }
        return INSTANCE;
    }

    private static OkHttpClient cc() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.COMPATIBLE_TLS))
                .build();

        return client;
    }
}
