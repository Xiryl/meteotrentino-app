package it.chiarani.meteotrentinoapp.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataMain;

public class OpenWeatherDataMainConverter {
    @TypeConverter
    public static OpenWeatherDataMain fromString(String value) {
        Type listType = new TypeToken<OpenWeatherDataMain>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromOpenWeatherDataMain(OpenWeatherDataMain data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
