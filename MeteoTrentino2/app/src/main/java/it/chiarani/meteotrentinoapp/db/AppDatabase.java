package it.chiarani.meteotrentinoapp.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;
import it.chiarani.meteotrentinoapp.db.converters.OpenWeatherDataCloudsConverter;
import it.chiarani.meteotrentinoapp.db.converters.OpenWeatherDataCoordsConverter;
import it.chiarani.meteotrentinoapp.db.converters.OpenWeatherDataMainConverter;
import it.chiarani.meteotrentinoapp.db.converters.OpenWeatherDataSysConverter;
import it.chiarani.meteotrentinoapp.db.converters.OpenWeatherDataWeatherConverter;
import it.chiarani.meteotrentinoapp.db.converters.OpenWeatherDataWindConverter;
import it.chiarani.meteotrentinoapp.db.converters.PrevisioneConverter;

@Database(entities =
        {
            MeteoTrentinoForecast.class,
            OpenWeatherDataForecast.class
        },
        version = 3,
        exportSchema = false)
@TypeConverters(
        {
                PrevisioneConverter.class,
                OpenWeatherDataCoordsConverter.class,
                OpenWeatherDataMainConverter.class,
                OpenWeatherDataSysConverter.class,
                OpenWeatherDataWindConverter.class,
                OpenWeatherDataCloudsConverter.class,
                OpenWeatherDataWeatherConverter.class
        })
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    // DAO's
    public abstract ForecastDao forecastDao();
    public abstract OpenWeatherDataForecastDao openWeatherDataForecastDao();

    /**
     * Get a singleton istance
     * @param context
     * @return AppDatabase db istance
     */
    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
