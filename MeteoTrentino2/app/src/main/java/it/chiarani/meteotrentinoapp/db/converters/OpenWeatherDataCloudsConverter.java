package it.chiarani.meteotrentinoapp.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataClouds;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataMain;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataWind;

public class OpenWeatherDataCloudsConverter {
    @TypeConverter
    public static OpenWeatherDataClouds fromString(String value) {
        Type listType = new TypeToken<OpenWeatherDataClouds>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromOpenWeatherDataClouds(OpenWeatherDataClouds data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
