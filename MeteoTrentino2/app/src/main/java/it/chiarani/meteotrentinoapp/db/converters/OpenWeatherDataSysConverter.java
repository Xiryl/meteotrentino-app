package it.chiarani.meteotrentinoapp.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataSys;

public class OpenWeatherDataSysConverter {
    @TypeConverter
    public static OpenWeatherDataSys fromString(String value) {
        Type listType = new TypeToken<OpenWeatherDataSys>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromOpenWeatherDataSys(OpenWeatherDataSys data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
