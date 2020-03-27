package it.chiarani.meteotrentino.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
import it.chiarani.meteotrentino.utils.FragmentLauncher;
import it.chiarani.meteotrentino.utils.IconConverter;
import it.chiarani.meteotrentino.utils.Localities;
import it.chiarani.meteotrentino.utils.Utils;
import it.chiarani.meteotrentino.views.HomeActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TodayWeatherFragment extends Fragment implements ItemClickListener, SuggestionItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FragmentTodayWeatherBinding binding;

    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;

    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private SlotWeatherAdapter mAdapter;
    private Previsione mForecast;
    private OpenWeatherDataForecast mOpenForecast;
    private List<String> suggestions = Localities.getLocationAsList();
    private ArrayList<String> filteredSuggestions = new ArrayList<>(suggestions);
    private DrawerLayout dwLayout;
    private NavigationView navigationView;

    public TodayWeatherFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_today_weather, container, false);
        View view = binding.getRoot();

        getExecutors();

        bindMeteoTrentinoData();

        bindOpenWeatherData();

        navigationView = getActivity().findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        bindSearchBar();

        binding.fragmentTodayImg7days.setOnClickListener( v -> FragmentLauncher.launch(new SevenDaysWeatherFragment(), getFragmentManager()));

        binding.fragmentTodayImgWeather.setOnClickListener( v -> launchWeatherDetailFragment(0,0));

        return view;
    }


    private void getExecutors() {
        mAppDatabase = ((MeteoTrentinoApp)getActivity().getApplication()).getRepository().getDatabase();
        mAppExecutors = ((MeteoTrentinoApp)getActivity().getApplication()).getRepository().getAppExecutors();
    }

    private void bindMeteoTrentinoData() {
        mDisposable.add(mAppDatabase.forecastDao().getAsList()
                .subscribeOn(Schedulers.io())
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecasts -> {

                    MeteoTrentinoForecast tmpMeteoTrentinoForecast = forecasts.get(forecasts.size() - 1);

                    mForecast = tmpMeteoTrentinoForecast.getPrevisione().get(0);

                    // set location
                    String location = mForecast.getLocalita();
                    if(location.length() >= 10) {
                        location = location.substring(0,9)+"..";
                    }
                    binding.fragmentTodayWeatherTxtLocation.setText(location);
                    binding.fragmentTodayWeatherTxtLocation.setOnClickListener(v-> Toast.makeText(getActivity().getApplicationContext(), mForecast.getLocalita(), Toast.LENGTH_LONG).show());

                    // set data
                    binding.fragmentTodayWeatherTxtForecast.setText(mForecast.getGiorni().get(0).getDescIcona());
                    binding.fragmentTodayWeatherTxtDay.setText(DayConverter.ExtractDayFromDate(mForecast.getGiorni().get(0).getGiorno()));
                    binding.fragmentTodayImgWeather.setBackgroundResource(IconConverter.getIconFromId(mForecast.getGiorni().get(0).getIdIcona()));
                    binding.fragmentTodayWeatherTxtRainPercentage.setText(String.format("%s %%", descConverter(mForecast.getGiorni().get(0).getFasce().get(0).getDescPrecProb())));

                    // set recyclerview
                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.HORIZONTAL);
                    binding.fragmentTodayWeatherRv.setLayoutManager(linearLayoutManagerTags);

                    List<Fascia> fasceForAdapter = new ArrayList<>();
                    fasceForAdapter.addAll(mForecast.getGiorni().get(0).getFasce());
                    fasceForAdapter.addAll(mForecast.getGiorni().get(1).getFasce());

                    mAdapter = new SlotWeatherAdapter(fasceForAdapter, this::onItemClick);
                    binding.fragmentTodayWeatherRv.setAdapter(mAdapter);

                    // set animation
                    Animations.doBounceAnimation(binding.fragmentTodayWeatherRv);

                }, throwable -> {
                    Toast.makeText(this.getActivity().getApplicationContext(), "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                })
        );
    }

    private void bindOpenWeatherData() {
        mDisposable.add(mAppDatabase.openWeatherDataForecastDao().getAsList()
                .subscribeOn(Schedulers.io())
                .take(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecasts -> {

                    mOpenForecast = forecasts.get(forecasts.size()-1);

                    double temperature = Utils.getCelsiusFromFahrenheit(mOpenForecast.getMain().getTemp());
                    double windspeed = Utils.getKmhFromMs(mOpenForecast.getWind().getSpeed());
                    double humidity = mOpenForecast.getMain().getHumidity();

                    binding.fragmentTodayWeatherTxtTemperature.setText(String.format("%.0f°", temperature));
                    binding.fragmentTodayWeatherTxtWindSpeed.setText(String.format("%.0f km/h", windspeed));
                    binding.fragmentTodayWeatherTxtHumidityPercentage.setText(String.format("%.0f%%", humidity));

                    Date date = new Date((long)mOpenForecast.getSys().getSunrise()*1000);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);

                    binding.fragmentWeatherDetailTxtSunrise.setText(String.format("%s:%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

                    date = new Date((long)mOpenForecast.getSys().getSunset()*1000);
                    calendar.setTime(date);
                    binding.fragmentWeatherDetailTxtSunset.setText(String.format("%s:%s", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)));

                }, throwable -> {
                    Toast.makeText(this.getActivity().getApplicationContext(), "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();
                })
        );
    }

    private void bindSearchBar() {
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        CustomSuggestionsAdapter customSuggestionsAdapter = new CustomSuggestionsAdapter(layoutInflater, this::onSuggestionItemClick);
        binding.searchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);
        customSuggestionsAdapter.setSuggestions(suggestions);
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

        dwLayout = (DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        binding.searchBar.bringToFront();
        binding.searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        dwLayout.openDrawer(GravityCompat.START);
                        break;
                }
            }
        });
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

        launchWeatherDetailFragment(day, newPos);
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

    private void launchWeatherDetailFragment(int day, int slot) {
        WeatherDetailFragment bottomSheetDialogFragment = new WeatherDetailFragment(mForecast, mOpenForecast, day, slot);
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), "bottom_nav_sheet_dialog");
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu_allerte: FragmentLauncher.launch(new AllertFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
            case R.id.nav_menu_radar: FragmentLauncher.launch(new RadarFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
            case R.id.nav_menu_neve: FragmentLauncher.launch(new AvalancheFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
            case R.id.nav_menu_bollettino: FragmentLauncher.launch(new ProbabilisticFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
            case R.id.nav_menu_dati_staz: FragmentLauncher.launch(new StationDataFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
            case R.id.nav_menu_dighe: FragmentLauncher.launch(new DamsAndRiverFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
            case R.id.nav_menu_webcam: FragmentLauncher.launch(new WebcamFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
            case R.id.nav_menu_impostaz: FragmentLauncher.launch(new SettingsFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
            case R.id.nav_menu_faq: FragmentLauncher.launch(new FaqFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
            case R.id.nav_menu_supporto: FragmentLauncher.launch(new DonationFragment(), getFragmentManager()); dwLayout.closeDrawer(GravityCompat.START); break;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDisposable.dispose();
    }
}
