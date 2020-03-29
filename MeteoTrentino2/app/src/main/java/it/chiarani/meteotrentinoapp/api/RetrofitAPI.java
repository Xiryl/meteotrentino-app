package it.chiarani.meteotrentinoapp.api;

import java.util.List;

import io.reactivex.Observable;
import it.chiarani.meteotrentinoapp.api.AvalancheModel.AvalancheModel;
import it.chiarani.meteotrentinoapp.api.BaciniModel.ListaBacini;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoProbabilisticModel.MeteoTrentinoProbabilisticModel;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.ArrayOfAnagrafica;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.DatiOggi;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("previsioneOpenDataLocalita")
    Observable<MeteoTrentinoForecast> getMeteoTrentinoForecast(
            @Query("localita") String location
    );

    @GET("previsioneOpenDataProbabilistico ")
    Observable<MeteoTrentinoProbabilisticModel> getMeteoTrentinoProbabilistic();

    @GET("listaStazioni")
    Observable<ArrayOfAnagrafica> getMeteoTrentinoStationList();

    @GET("bacini_nuovo.json")
    Observable<ListaBacini> getDamsAndRiverJson();

    @GET("download.php")
    Observable<ResponseBody> getRiverSensorData(
            @Query("Sensore") String sensor,
            @Query("Argomento") String argument
    );

    @GET("ultimiDatiStazione")
    Observable<DatiOggi> getMeteoTrentinoStationData(
            @Query("codice") String stationCode
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
