package it.chiarani.meteotrentinoapp.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataMain;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataWind;

public class OpenWeatherDataWindConverter {
    @TypeConverter
    public static OpenWeatherDataWind fromString(String value) {
        Type listType = new TypeToken<OpenWeatherDataWind>() {}.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromOpenWeatherDataWind(OpenWeatherDataWind data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }
}
