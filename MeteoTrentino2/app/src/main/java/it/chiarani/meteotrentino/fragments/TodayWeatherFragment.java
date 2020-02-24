package it.chiarani.meteotrentino.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentino.AppExecutors;
import it.chiarani.meteotrentino.MeteoTrentinoApp;
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.adapters.SevenDayWeatherAdapter;
import it.chiarani.meteotrentino.adapters.SlotWeatherAdapter;
import it.chiarani.meteotrentino.api.ForecastModel.Forecast;
import it.chiarani.meteotrentino.databinding.FragmentSevenDaysWeatherBinding;
import it.chiarani.meteotrentino.databinding.FragmentTodayWeatherBinding;
import it.chiarani.meteotrentino.db.AppDatabase;
import it.chiarani.meteotrentino.utils.DayConverter;
import it.chiarani.meteotrentino.views.HomeActivity;

public class TodayWeatherFragment extends Fragment {

    FragmentTodayWeatherBinding binding;
    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private SlotWeatherAdapter mAdapter;
    public TodayWeatherFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_today_weather, container, false);
        View view = binding.getRoot();

        mAppDatabase = ((MeteoTrentinoApp)getActivity().getApplication()).getRepository().getDatabase();

        mDisposable.add(mAppDatabase.forecastDao().getAsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecasts -> {

                    Forecast tmpForecast = forecasts.get(forecasts.size() - 1);
                    binding.fragmentTodayWeatherTxtLocation.setText(tmpForecast.getPrevisione().get(0).getLocalita());
                    binding.fragmentTodayWeatherTxtForecast.setText(tmpForecast.getPrevisione().get(0).getGiorni().get(0).getDescIcona());
                    binding.fragmentTodayWeatherTxtDay.setText(DayConverter.ExtractDayFromDate(tmpForecast.getPrevisione().get(0).getGiorni().get(0).getGiorno()));

                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.HORIZONTAL);
                    binding.fragmentSevenDaysWeatherRv.setLayoutManager(linearLayoutManagerTags);
                    mAdapter = new SlotWeatherAdapter(tmpForecast.getPrevisione().get(0).getGiorni().get(0).getFasce());
                    binding.fragmentSevenDaysWeatherRv.setAdapter(mAdapter);
                }, throwable -> {
                    Toast.makeText(this.getActivity().getApplicationContext(), "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                })
        );

        // Inflate the layout for this fragment
        return view;
    }
}
