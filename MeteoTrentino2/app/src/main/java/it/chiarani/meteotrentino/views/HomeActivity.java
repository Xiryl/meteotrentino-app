package it.chiarani.meteotrentino.views;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.databinding.ActivityHomeBinding;
import it.chiarani.meteotrentino.fragments.SevenDaysWeatherFragment;
import it.chiarani.meteotrentino.fragments.TodayWeatherFragment;
import it.chiarani.meteotrentino.utils.TabAdapter;

public class HomeActivity extends BaseActivity {

    ActivityHomeBinding binding;

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

    }

    private void configureTabLayout() {
        adapter = new TabAdapter(getSupportFragmentManager());

        adapter.addFragment(new TodayWeatherFragment(), "info");
        adapter.addFragment(new SevenDaysWeatherFragment(), "impostazioni");
        binding.activityHomeViewPager.setAdapter(adapter);
        binding.activityHomeTabLayout.setupWithViewPager(binding.activityHomeViewPager);
    }
}
