package it.chiarani.meteotrentino.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.reactivex.disposables.CompositeDisposable;
import it.chiarani.meteotrentino.AppExecutors;
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.Fascia;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.Giorno;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.Previsione;
import it.chiarani.meteotrentino.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;
import it.chiarani.meteotrentino.databinding.FragmentWeatherDetailBinding;
import it.chiarani.meteotrentino.db.AppDatabase;
import it.chiarani.meteotrentino.utils.DayConverter;
import it.chiarani.meteotrentino.utils.IconConverter;
import okhttp3.internal.Util;


public class WeatherDetailFragment extends BottomSheetDialogFragment {

    FragmentWeatherDetailBinding binding;
    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private Previsione mforecast;
    private OpenWeatherDataForecast mOpenWForecast;
    private int mDay, mSlot;

    public WeatherDetailFragment() {
        // Required empty public constructor
    }

    public WeatherDetailFragment(Previsione meteoTrentinoForecast, OpenWeatherDataForecast openWeatherDataForecast, int day, int slot) {
        this.mforecast = meteoTrentinoForecast;
        this.mOpenWForecast = openWeatherDataForecast;
        this.mDay = day;
        this.mSlot = slot;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_weather_detail, container, false);
        View view = binding.getRoot();

        Giorno forecastDay = mforecast.getGiorni().get(mDay);
        Fascia forecastSlot = forecastDay.getFasce().get(mSlot);

        binding.fragmentWeatherDetailIcWeather.setBackgroundResource(IconConverter.getIconFromId(forecastDay.getIdIcona()));

        // header
        binding.fragmentWeatherDetailTxtDescPrevisione.setText(String.format("%s", forecastDay.getTestoGiorno()));
        binding.fragmentWeatherDetailTxtTime.setText(String.format("%s", DayConverter.ExtractDayFromDate(forecastDay.getGiorno())));
        binding.fragmentWeatherDetailTxtTimeDescr.setText(String.format("%s",forecastSlot.getFasciaPer()));

        // temperature
        binding.fragmentWeatherDetailTxtTempMax.setText(String.format("%s°", forecastDay.getTMaxGiorno()));
        binding.fragmentWeatherDetailTxtTempMin.setText(String.format("%s°", forecastDay.getTMinGiorno()));
        binding.fragmentWeatherDetailTxtZero.setText(String.format("%smt", forecastSlot.getZeroTermico()));

        // rain
        binding.fragmentWeatherDetailTxtProbPrec.setText(String.format("%s %%", descConverter(forecastSlot.getDescPrecProb())));
        binding.fragmentWeatherDetailTxtIntPrec.setText(String.format("%s", forecastSlot.getDescPrecInten()));
        binding.fragmentWeatherDetailTxtPercTemp.setText(String.format("%s %%", forecastSlot.getDescTempProb()));

        // wind
        binding.fragmentWeatherDetailTxtVentoQuota.setText(String.format("%s/%s", forecastSlot.getDescVentoDirQuota(), forecastSlot.getDescVentoIntQuota()));
        binding.fragmentWeatherDetailTxtVentoValle.setText(String.format("%s/%s", forecastSlot.getDescVentoDirValle(), forecastSlot.getDescVentoIntValle()));

        return view;
    }

    private String descConverter(String desc) {
        switch (desc.toLowerCase()) {
            case "molto bassa": return "0-25";
            case "bassa": return "25-50";
            case "media": return "50-75";
            case "alta": return "75-100";
            default: return "--";
        }
    }
}
