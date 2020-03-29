package it.chiarani.meteotrentinoapp.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;

@Dao
public interface ForecastDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MeteoTrentinoForecast forecast);

    @Query("SELECT * FROM MeteoTrentinoForecast")
    Flowable<List<MeteoTrentinoForecast>> getAsList();

    /**
     * Clear the database
     */
    @Query("DELETE FROM MeteoTrentinoForecast")
    void clear();

    @Delete
    void delete(List<MeteoTrentinoForecast> forecasts);
}