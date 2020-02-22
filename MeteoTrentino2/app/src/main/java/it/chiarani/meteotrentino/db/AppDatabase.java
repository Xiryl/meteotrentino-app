package it.chiarani.meteotrentino.db;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import it.chiarani.meteotrentino.api.ForecastModel.Forecast;
import it.chiarani.meteotrentino.db.converters.PrevisioneConverter;

@Database(entities = {Forecast.class}, version = 1, exportSchema = false)
@TypeConverters({PrevisioneConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    // DAO's
    public abstract ForecastDao forecastDao();

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
