package it.chiarani.meteotrentino.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;
import it.chiarani.meteotrentino.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;

@Dao
public interface OpenWeatherDataForecastDao {
    /**
     * Insert a new device
     * @param nordicDevice device
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(OpenWeatherDataForecast nordicDevice);

    /**
     * Return all devices as list
     * @return
     */
    @Query("SELECT * FROM OpenWeatherDataForecast")
    Flowable<List<OpenWeatherDataForecast>> getAsList();

    /**
     * Clear the database
     */
    @Query("DELETE FROM OpenWeatherDataForecast")
    void clear();

    /**
     * Delete a list of devices
     * @param nordicDevices
     */
    @Delete
    void delete(List<OpenWeatherDataForecast> nordicDevices);
}