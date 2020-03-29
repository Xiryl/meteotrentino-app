package it.chiarani.meteotrentinoapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;

@Dao
public interface OpenWeatherDataForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OpenWeatherDataForecast forecast);

    @Query("SELECT * FROM OpenWeatherDataForecast")
    Flowable<List<OpenWeatherDataForecast>> getAsList();

    @Query("DELETE FROM OpenWeatherDataForecast")
    void clear();

    @Delete
    void delete(List<OpenWeatherDataForecast> forecasts);
}