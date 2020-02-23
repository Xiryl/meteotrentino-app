package it.chiarani.meteotrentino.views;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

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

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorWhite));

    }

    private void configureTabLayout() {
        adapter = new TabAdapter(getSupportFragmentManager());

        adapter.addFragment(new TodayWeatherFragment(), "Oggi");
        adapter.addFragment(new SevenDaysWeatherFragment(), "7 Giorni");
        binding.activityHomeViewPager.setAdapter(adapter);
        binding.activityHomeTabLayout.setupWithViewPager(binding.activityHomeViewPager);
    }
}
