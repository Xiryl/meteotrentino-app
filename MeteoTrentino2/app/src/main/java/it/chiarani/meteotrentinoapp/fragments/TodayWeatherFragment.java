package it.chiarani.meteotrentinoapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
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
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.SimpleTarget;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentinoapp.AppExecutors;
import it.chiarani.meteotrentinoapp.MeteoTrentinoApp;
import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.adapters.CustomSuggestionsAdapter;
import it.chiarani.meteotrentinoapp.adapters.ItemClickListener;
import it.chiarani.meteotrentinoapp.adapters.SlotWeatherAdapter;
import it.chiarani.meteotrentinoapp.adapters.SuggestionItemClickListener;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoAPI;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel.Fascia;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel.MeteoTrentinoForecast;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoForecastModel.Previsione;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataAPI;
import it.chiarani.meteotrentinoapp.api.OpenWeatherDataForecastModel.OpenWeatherDataForecast;
import it.chiarani.meteotrentinoapp.api.RetrofitAPI;
import it.chiarani.meteotrentinoapp.config.Config;
import it.chiarani.meteotrentinoapp.databinding.FragmentTodayWeatherBinding;
import it.chiarani.meteotrentinoapp.db.AppDatabase;
import it.chiarani.meteotrentinoapp.utils.Animations;
import it.chiarani.meteotrentinoapp.utils.DayConverter;
import it.chiarani.meteotrentinoapp.utils.FragmentLauncher;
import it.chiarani.meteotrentinoapp.utils.IconConverter;
import it.chiarani.meteotrentinoapp.utils.Localities;
import it.chiarani.meteotrentinoapp.utils.Utils;
import it.chiarani.meteotrentinoapp.views.HomeActivity;

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
    SharedPreferences getPrefs;

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

        getPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        rateApp();
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
                    String ico = mForecast.getGiorni().get(0).getFasce().get(0).getIcona().split("_")[1].substring(0, 3);
                    binding.fragmentTodayImgWeather.setAnimation(IconConverter.getAnimResFromId(Integer.parseInt(ico)));
                    binding.fragmentTodayImgWeather.loop(true);

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

                    runSpotlight();

                }, throwable -> {
                    throwable.printStackTrace();
                    Toast.makeText(this.getActivity().getApplicationContext(), "Oops, qualcosa è andato storto" + throwable, Toast.LENGTH_SHORT).show();
                })
        );
    }

    private void bindOpenWeatherData() {
        mDisposable.add(mAppDatabase.openWeatherDataForecastDao().getAsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(forecasts -> {

                    if(forecasts.isEmpty()) {
                        return;
                    }

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
                    throwable.printStackTrace();
                    Toast.makeText(this.getActivity().getApplicationContext(), "Oops, qualcosa è andato storto:" + throwable, Toast.LENGTH_SHORT).show();
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
        // Toast.makeText(getActivity().getApplicationContext(), "Location:" + location, Toast.LENGTH_LONG).show();

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
                    err.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Oops, qualcosa è andato storto (err1)", Toast.LENGTH_SHORT).show();
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
            case R.id.nav_menu_telegram: {
                String url = "https://www.t.me/meteotrentinobot";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                dwLayout.closeDrawer(GravityCompat.START);
            } break;
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

    public void runSpotlight() {
        boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
        if(!isFirstStart) {
            return;
        }
        SimpleTarget target1 = new SimpleTarget.Builder(getActivity())
                .setPoint(binding.searchBar)
                .setShape(new Circle(500f))
                .setTitle("Menu e Ricerca")
                .setDescription("Apri il menù laterale per ottenere più funzionalità. Utilizza questa barra di ricerca per cambiare la località!")
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                    }
                })
                .build();

        SimpleTarget target2 = new SimpleTarget.Builder(getActivity())
                .setPoint(binding.fragmentTodayImg7days)
                .setShape(new Circle(300f))
                .setTitle("Fino a 7 giorni")
                .setDescription("Apri la visualizzazione dei prossimi giorni.")
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                    }
                })
                .build();

        SimpleTarget target3 = new SimpleTarget.Builder(getActivity())
                .setPoint(binding.fragmentTodayWeatherRv)
                .setShape(new Circle(500f))
                .setTitle("Prossime fasce orarie")
                .setDescription("Visualizza i dati delle prossime fasce orarie tra oggi e domani. Premi su una fascia per visualizzare i dati.")
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {
                    }
                    @Override
                    public void onEnded(SimpleTarget target) {
                    }
                })
                .build();

        Spotlight.with(getActivity())
                .setOverlayColor(R.color.background)
                .setDuration(10)
                .setAnimation(new DecelerateInterpolator(1f))
                .setTargets(target1, target2, target3)
                .setClosedOnTouchedOutside(true)
                .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                    @Override
                    public void onStarted() {
                    }

                    @Override
                    public void onEnded() {
                    }
                })
                .start();

        SharedPreferences.Editor e = getPrefs.edit();

        //  Edit preference to make it false because we don't want this to run again
        e.putBoolean("firstStart", false);

        //  Apply changes
        e.apply();
    }

    private void rateApp() {
        AppRate.with(getActivity())
                .setInstallDays(0)
                .setLaunchTimes(5)
                .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                    @Override
                    public void onClickButton(int which) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://play.google.com/store/apps/details?id=it.chiarani.meteotrentinoapp&hl=it"));
                        startActivity(i);
                    }
                })
                .setRemindInterval(3)
                .setShowLaterButton(true)
                .setDebug(false)
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(getActivity());
    }
}
