package it.chiarani.meteotrentinoapp.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataCoords;

public class OpenWeatherDataCoordsConverter {
    @TypeConverter
    public static OpenWeatherDataCoords fromString(String value) {
        Type listType = new TypeToken<OpenWeatherDataCoords>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromOpenWeatherDataCoords(OpenWeatherDataCoords data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
