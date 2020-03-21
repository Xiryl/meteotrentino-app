package it.chiarani.meteotrentino.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentino.AppExecutors;
import it.chiarani.meteotrentino.MeteoTrentinoApp;
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.adapters.CustomSuggestionsAdapter;
import it.chiarani.meteotrentino.adapters.ItemClickListener;
import it.chiarani.meteotrentino.adapters.SlotWeatherAdapter;
import it.chiarani.meteotrentino.adapters.SuggestionItemClickListener;
import it.chiarani.meteotrentino.api.MeteoTrentinoAPI;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.Fascia;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;
import it.chiarani.meteotrentino.api.MeteoTrentinoForecastModel.Previsione;
import it.chiarani.meteotrentino.api.OpenWeatherDataAPI;
import it.chiarani.meteotrentino.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;
import it.chiarani.meteotrentino.api.RetrofitAPI;
import it.chiarani.meteotrentino.config.Config;
import it.chiarani.meteotrentino.databinding.FragmentTodayWeatherBinding;
import it.chiarani.meteotrentino.db.AppDatabase;
import it.chiarani.meteotrentino.utils.Animations;
import it.chiarani.meteotrentino.utils.DayConverter;
import it.chiarani.meteotrentino.utils.IconConverter;
import it.chiarani.meteotrentino.utils.Localities;
import it.chiarani.meteotrentino.views.HomeActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TodayWeatherFragment extends Fragment implements ItemClickListener, MaterialSearchBar.OnSearchActionListener, SuggestionItemClickListener {

    FragmentTodayWeatherBinding binding;
    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private SlotWeatherAdapter mAdapter;
    private Previsione mForecast;
    private OpenWeatherDataForecast mOpenForecast;
    List<String> suggestions = Localities.getLocationAsList();
    ArrayList<String> filteredSuggestions = new ArrayList<>(suggestions);

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
        mAppExecutors = ((MeteoTrentinoApp)getActivity().getApplication()).getRepository().getAppExecutors();

        mDisposable.add(mAppDatabase.forecastDao().getAsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecasts -> {

                    MeteoTrentinoForecast tmpMeteoTrentinoForecast = forecasts.get(forecasts.size() - 1);
                    mForecast = tmpMeteoTrentinoForecast.getPrevisione().get(0);
                    String localita = tmpMeteoTrentinoForecast.getPrevisione().get(0).getLocalita();
                    if(localita.length() >= 10) {
                        localita = localita.substring(0,9)+"..";
                    }
                    binding.fragmentTodayWeatherTxtLocation.setText(localita);
                    binding.fragmentTodayWeatherTxtLocation.setOnClickListener(v->{
                        Toast.makeText(getActivity().getApplicationContext(), tmpMeteoTrentinoForecast.getPrevisione().get(0).getLocalita(), Toast.LENGTH_LONG).show();
                    });
                    binding.fragmentTodayWeatherTxtForecast.setText(tmpMeteoTrentinoForecast.getPrevisione().get(0).getGiorni().get(0).getDescIcona());
                    binding.fragmentTodayWeatherTxtDay.setText(DayConverter.ExtractDayFromDate(tmpMeteoTrentinoForecast.getPrevisione().get(0).getGiorni().get(0).getGiorno()));
                    binding.fragmentTodayImgWeather.setBackgroundResource(IconConverter.getIconFromId(tmpMeteoTrentinoForecast.getPrevisione().get(0).getGiorni().get(0).getIdIcona()));
                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.HORIZONTAL);
                    binding.fragmentSevenDaysWeatherRv.setLayoutManager(linearLayoutManagerTags);
                    List<Fascia> fasceForAdapter = new ArrayList<>();
                    fasceForAdapter.addAll(tmpMeteoTrentinoForecast.getPrevisione().get(0).getGiorni().get(0).getFasce());
                    fasceForAdapter.addAll(tmpMeteoTrentinoForecast.getPrevisione().get(0).getGiorni().get(1).getFasce());
                    mAdapter = new SlotWeatherAdapter(fasceForAdapter, this::onItemClick);
                    binding.fragmentSevenDaysWeatherRv.setAdapter(mAdapter);

                    Animations.doBounceAnimation(binding.fragmentSevenDaysWeatherRv);
                }, throwable -> {
                    Toast.makeText(this.getActivity().getApplicationContext(), "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                })
        );

        mDisposable.add(mAppDatabase.openWeatherDataForecastDao().getAsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecasts -> {

                    OpenWeatherDataForecast tmpForecast = forecasts.get(forecasts.size()-1);
                    mOpenForecast = tmpForecast;
                    double temperature = tmpForecast.getMain().getTemp();
                    temperature = temperature - 273.15;
                    double windspeed = tmpForecast.getWind().getSpeed() * 3.6;
                    double humidity = tmpForecast.getMain().getHumidity();
                    binding.fragmentTodayWeatherTxtTemperature.setText(String.format("%.0f°", temperature));
                    binding.fragmentTodayWeatherTxtWindSpeed.setText(String.format("%.0f km/h", windspeed));
                    binding.fragmentTodayWeatherTxtHumidityPercentage.setText(String.format("%.0f%%", humidity));

                    Date date = new Date((long)tmpForecast.getSys().getSunrise()*1000);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    binding.fragmentWeatherDetailTxtSunrise.setText(String.format("%s:%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

                    date = new Date((long)tmpForecast.getSys().getSunset()*1000);
                    calendar.setTime(date);
                    binding.fragmentWeatherDetailTxtSunset.setText(String.format("%s:%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

                    }, throwable -> {
                    Toast.makeText(this.getActivity().getApplicationContext(), "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                })
        );

        binding.fragmentTodayImg7days.setOnClickListener( v -> {
            Fragment newFragment = new SevenDaysWeatherFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.activity_home_frame_layout, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });


        binding.fragmentTodayImgWeather.setOnClickListener( v -> {
            WeatherDetailFragment bottomSheetDialogFragment = new WeatherDetailFragment(mForecast, mOpenForecast, 0, 0);
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "bottom_nav_sheet_dialog");
        });

        binding.searchBar.bringToFront();
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        CustomSuggestionsAdapter customSuggestionsAdapter = new CustomSuggestionsAdapter(layoutInflater, this::onSuggestionItemClick);

        binding.searchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);
        customSuggestionsAdapter.setSuggestions(suggestions);
        binding.searchBar.setOnSearchActionListener(this);
        binding.searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filteredSuggestions.clear();
                for(String tmpCsv : suggestions){
                    String tmp = tmpCsv.split(";")[0];
                    if(tmp.toLowerCase().contains(s.toString().toLowerCase())) {
                        filteredSuggestions.add(tmpCsv);
                    }
                }

                customSuggestionsAdapter.setSuggestions(filteredSuggestions);
                customSuggestionsAdapter.notifyDataSetChanged();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClick(int position) {
        int day = 0;
        int newPos = position;

        int countFirstDay = mForecast.getGiorni().get(0).getFasce().size();
        if(position >= countFirstDay) {
            day = 1;
            newPos = position - countFirstDay;
        }

        WeatherDetailFragment bottomSheetDialogFragment = new WeatherDetailFragment(mForecast, mOpenForecast, day, newPos);
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "bottom_nav_sheet_dialog");
    }

    @Override
    public void onSuggestionItemClick(int position) {
        String location = filteredSuggestions.get(position).split(";")[0];
        String lat = filteredSuggestions.get(position).split(";")[3];
        String lng = filteredSuggestions.get(position).split(";")[4];
        Toast.makeText(getActivity().getApplicationContext(), "Location:" + location, Toast.LENGTH_LONG).show();


        RetrofitAPI meteoTrentinoAPI = MeteoTrentinoAPI.getInstance();
        RetrofitAPI openWeatherDataAPI = OpenWeatherDataAPI.getInstance();

         mDisposable.add(meteoTrentinoAPI.getMeteoTrentinoForecast(location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(model -> {
                    mAppExecutors.diskIO().execute(() -> mAppDatabase.forecastDao().insert(model));
                    return openWeatherDataAPI.getOpenWeatherDataForecast(Config.OPENWEATHERDATA_API_KEY, lat, lng)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                })
                .subscribe(model -> {
                    mAppExecutors.diskIO().execute(() -> mAppDatabase.openWeatherDataForecastDao().insert(model));
                    this.startActivity(new Intent(getActivity().getApplicationContext(), HomeActivity.class));
                }, err -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Oops, qualcosa è andato storto" + err, Toast.LENGTH_SHORT).show();
                }));
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}
