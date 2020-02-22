package it.chiarani.meteotrentino.api;

import io.reactivex.Observable;
import it.chiarani.meteotrentino.api.ForecastModel.Forecast;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("previsioneOpenDataLocalita")
    Observable<Forecast> getForecast(@Query("localita") String location);
}
