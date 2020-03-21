package it.chiarani.meteotrentino.db.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.Previsione;

/**
 * convert all classes to json
 * Guide: https://android.jlelse.eu/room-persistence-library-typeconverters-and-database-migration-3a7d68837d6c
 */
public class PrevisioneConverter {

    @TypeConverter
    public String fromPrevisioneList(List<Previsione> previsione) {
        if (previsione == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Previsione>>() {}.getType();
        String json = gson.toJson(previsione, type);
        return json;
    }

    @TypeConverter
    public List<Previsione> toPrevisioneList(String previsione) {
        if (previsione == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Previsione>>() {}.getType();
        List<Previsione> countryLangList = gson.fromJson(previsione, type);
        return countryLangList;
    }
}
