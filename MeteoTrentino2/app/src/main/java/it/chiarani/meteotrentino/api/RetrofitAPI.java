package it.chiarani.meteotrentino.api;

import java.util.List;

import io.reactivex.Observable;
import it.chiarani.meteotrentino.api.AvalancheModel.AvalancheModel;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;
import it.chiarani.meteotrentino.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("previsioneOpenDataLocalita")
    Observable<MeteoTrentinoForecast> getMeteoTrentinoForecast(
            @Query("localita") String location
    );

    @GET("weather")
    Observable<OpenWeatherDataForecast> getOpenWeatherDataForecast(
            @Query("appid") String api_key,
            @Query("lat") String lat,
            @Query("lon") String lng
    );

    @GET("elencoavvisi.aspx")
    Observable<ResponseBody> getAllert();

    @GET("{date}/avalanche_report.json")
    Observable<List<AvalancheModel>> getAvalancheReport(
            @Path("date") String date
    );
}
