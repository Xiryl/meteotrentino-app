package it.chiarani.meteotrentinoapp.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentinoapp.AppExecutors;
import it.chiarani.meteotrentinoapp.MeteoTrentinoApp;
import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.adapters.ItemClickListener;
import it.chiarani.meteotrentinoapp.adapters.SevenDayWeatherAdapter;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;
import it.chiarani.meteotrentinoapp.databinding.FragmentSevenDaysWeatherBinding;
import it.chiarani.meteotrentinoapp.db.AppDatabase;
import it.chiarani.meteotrentinoapp.utils.DayConverter;

public class SevenDaysWeatherFragment extends Fragment implements ItemClickListener {

    FragmentSevenDaysWeatherBinding binding;
    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private SevenDayWeatherAdapter mAdapter;
    private MeteoTrentinoForecast mForecast;
    private OpenWeatherDataForecast mOpenForecast;

    public SevenDaysWeatherFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_seven_days_weather, container, false);
        View view = binding.getRoot();

        mAppDatabase = ((MeteoTrentinoApp)getActivity().getApplication()).getRepository().getDatabase();

        mDisposable.add(mAppDatabase.forecastDao().getAsList()
                .take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecasts -> {

                    MeteoTrentinoForecast tmpMeteoTrentinoForecast = forecasts.get(forecasts.size() - 1);
                    mForecast = tmpMeteoTrentinoForecast;
                    String localita = tmpMeteoTrentinoForecast.getPrevisione().get(0).getLocalita();
                    if(localita.length() >= 16) {
                        localita = localita.substring(0,15)+"..";
                    }

                    binding.fragmentSevenDaysWeatherTxtLocation.setText(localita);
                    binding.fragmentSevenDaysWeatherTxtForecast.setText(tmpMeteoTrentinoForecast.getPrevisione().get(0).getGiorni().get(0).getDescIcona());
                    binding.fragmentSevenDaysWeatherTxtDay.setText(DayConverter.ExtractDayFromDate(tmpMeteoTrentinoForecast.getPrevisione().get(0).getGiorni().get(0).getGiorno()));

                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.VERTICAL);
                    binding.fragmentSevenDaysWeatherRv.setLayoutManager(linearLayoutManagerTags);
                    mAdapter = new SevenDayWeatherAdapter(tmpMeteoTrentinoForecast.getPrevisione().get(0).getGiorni(), this::onItemClick);
                    binding.fragmentSevenDaysWeatherRv.setAdapter(mAdapter);
                }, throwable -> {
                    Toast.makeText(this.getActivity().getApplicationContext(), "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                })
        );

        mDisposable.add(mAppDatabase.openWeatherDataForecastDao().getAsList()
                .take(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecasts -> {
                    mOpenForecast = forecasts.get(forecasts.size() -1);
                }, throwable -> {
                    Toast.makeText(this.getActivity().getApplicationContext(), "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                })
        );

        binding.fragmentTodayWeatherTxtBack.setOnClickListener(v->{
            popBackStack(getFragmentManager());
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClick(int position) {
        WeatherDetailFragment bottomSheetDialogFragment = new WeatherDetailFragment(mForecast.getPrevisione().get(0), mOpenForecast, position, 0);
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "bottom_nav_sheet_dialog");
    }

    private void popBackStack(FragmentManager manager){
        mDisposable.dispose();
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onResume() {
        super.onResume();
        int x = 1;
    }
}
