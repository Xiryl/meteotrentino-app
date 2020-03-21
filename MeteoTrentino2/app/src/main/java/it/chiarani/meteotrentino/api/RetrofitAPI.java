package it.chiarani.meteotrentino.api;

import io.reactivex.Observable;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;
import it.chiarani.meteotrentino.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("previsioneOpenDataLocalita")
    Observable<MeteoTrentinoForecast> getMeteoTrentinoForecast(@Query("localita") String location);

    @GET("weather")
    Observable<OpenWeatherDataForecast> getOpenWeatherDataForecast(@Query("appid") String api_key, @Query("lat") String lat, @Query("lon") String lng);
}
