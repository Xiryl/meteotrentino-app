package it.chiarani.meteotrentino.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import it.chiarani.meteotrentino.api.OpenWeatherDataForecastModel.OpenWeatherDataCoords;

public class OpenWeatherDataCoordsConverter {
    @TypeConverter
    public static OpenWeatherDataCoords fromString(String value) {
        Type listType = new TypeToken<OpenWeatherDataCoords>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromOpenWeatherDataCoords(OpenWeatherDataCoords weatherForWeek) {
        Gson gson = new Gson();
        return gson.toJson(weatherForWeek);
    }
}