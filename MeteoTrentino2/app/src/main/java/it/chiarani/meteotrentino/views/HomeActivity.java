package it.chiarani.meteotrentino.views;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentino.AppExecutors;
import it.chiarani.meteotrentino.MeteoTrentinoApp;
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.adapters.CustomSuggestionsAdapter;
import it.chiarani.meteotrentino.adapters.SuggestionItemClickListener;
import it.chiarani.meteotrentino.api.MeteoTrentinoAPI;
import it.chiarani.meteotrentino.api.OpenWeatherDataAPI;
import it.chiarani.meteotrentino.api.RetrofitAPI;
import it.chiarani.meteotrentino.config.Config;
import it.chiarani.meteotrentino.databinding.ActivityHomeBinding;
import it.chiarani.meteotrentino.db.AppDatabase;
import it.chiarani.meteotrentino.fragments.TodayWeatherFragment;
import it.chiarani.meteotrentino.utils.Localities;
import it.chiarani.meteotrentino.utils.TabAdapter;

public class HomeActivity extends BaseActivity implements MaterialSearchBar.OnSearchActionListener, SuggestionItemClickListener {

    ActivityHomeBinding binding;
    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    List<String> suggestions = Localities.getLocationAsList();
    ArrayList<String> filteredSuggestions = new ArrayList<>(suggestions);

    private TabAdapter adapter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    protected void setActivityBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutID());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureTabLayout();

        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));

       /* MaterialSearchBar searchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        searchBar.bringToFront();
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        CustomSuggestionsAdapter customSuggestionsAdapter = new CustomSuggestionsAdapter(inflater, this::onSuggestionItemClick);

        searchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);
        customSuggestionsAdapter.setSuggestions(suggestions);
        searchBar.setOnSearchActionListener(this);
        searchBar.addTextChangeListener(new TextWatcher() {
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
        });*/

    }

    private void configureTabLayout() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.activity_home_frame_layout, new TodayWeatherFragment());
        transaction.commit();
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {
        String s = enabled ? "enabled" : "disabled";
        Toast.makeText(getApplicationContext(), "Search " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        Toast.makeText(getApplicationContext(), "Search ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onButtonClicked(int buttonCode) {

        Toast.makeText(getApplicationContext(), "ben " , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuggestionItemClick(int position) {
        String location = filteredSuggestions.get(position).split(";")[0];
        String lat = filteredSuggestions.get(position).split(";")[3];
        String lng = filteredSuggestions.get(position).split(";")[4];
        Toast.makeText(this, "Location:" + location, Toast.LENGTH_LONG).show();


        RetrofitAPI meteoTrentinoAPI = MeteoTrentinoAPI.getInstance();
        RetrofitAPI openWeatherDataAPI = OpenWeatherDataAPI.getInstance();

        mAppExecutors = ((MeteoTrentinoApp)getApplication()).getRepository().getAppExecutors();
        mAppDatabase = ((MeteoTrentinoApp)getApplication()).getRepository().getDatabase();

        Consumer<Throwable> throwableConsumer = throwable -> Toast.makeText(this, "Oops, qualcosa è andato storto", Toast.LENGTH_SHORT).show();

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
                    this.startActivity(new Intent(this, HomeActivity.class));
                    this.finish();
                }, throwableConsumer));
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
