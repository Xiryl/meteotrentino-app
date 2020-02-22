package it.chiarani.meteotrentino.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import it.chiarani.meteotrentino.api.ForecastModel.Forecast;

@Dao
public interface ForecastDao {
    /**
     * Insert a new device
     * @param nordicDevice device
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Forecast nordicDevice);

    /**
     * Return all devices as list
     * @return
     */
    @Query("SELECT * FROM forecast")
    Flowable<List<Forecast>> getAsList();

    /**
     * Clear the database
     */
    @Query("DELETE FROM forecast")
    void clear();

    /**
     * Delete a list of devices
     * @param nordicDevices
     */
    @Delete
    void delete(List<Forecast> nordicDevices);
}