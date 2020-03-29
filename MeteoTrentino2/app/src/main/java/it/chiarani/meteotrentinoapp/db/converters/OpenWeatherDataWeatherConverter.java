package it.chiarani.meteotrentinoapp.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataWeather;

public class OpenWeatherDataWeatherConverter {
    @TypeConverter
    public String OpenWeatherDataWeatherList(List<OpenWeatherDataWeather> previsione) {
        if (previsione == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<OpenWeatherDataWeather>>() {}.getType();
        String json = gson.toJson(previsione, type);
        return json;
    }

    @TypeConverter
    public List<OpenWeatherDataWeather> toOpenWeatherDataWeather(String previsione) {
        if (previsione == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<OpenWeatherDataWeather>>() {}.getType();
        List<OpenWeatherDataWeather> countryLangList = gson.fromJson(previsione, type);
        return countryLangList;
    }
}
