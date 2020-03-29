package it.chiarani.meteotrentinoapp.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.io.Console;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentinoapp.MeteoTrentinoApp;
import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.db.AppDatabase;
import it.chiarani.meteotrentinoapp.views.MainActivity;

public class SettingsFragment extends PreferenceFragmentCompat {

    private Preference prefCleanCache, prefTelegram, prefEmail, prefWebsite, prefPrivacy;
    private SwitchPreference prefTheme;

    public SettingsFragment() {

    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.fragment_settings, rootKey);

        prefCleanCache = (Preference) findPreference("pref_key_clear_storage");
        prefTelegram = (Preference) findPreference("pref_key_contact_telegram");
        prefEmail = (Preference) findPreference("pref_key_contact_email");
        prefWebsite = (Preference) findPreference("pref_key_contact_website");
        prefTheme = findPreference("pref_key_darkmode");
        prefPrivacy = findPreference("pref_key_privacy");

        prefTheme.setOnPreferenceChangeListener((preference, newValue) -> {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = sharedPref.edit();

            if (sharedPref.getString("theme", "light").equals("light")) {
                editor.putString("theme","dark");
                prefTheme.setChecked(true);
            } else {
                editor.putString("theme","light");
                prefTheme.setChecked(false);
            }
            editor.apply();
            //
            Toast.makeText(getContext(), "Ok, riavvia l'applicazione per vedere le modifiche.", Toast.LENGTH_LONG).show();
            return false;
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String currentTheme = sharedPref.getString("theme", "light");
        if (currentTheme.equals("light")) {
           prefTheme.setChecked(false);
        } else {
            prefTheme.setChecked(true);
        }

        prefCleanCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AppDatabase mAppDatabase = ((MeteoTrentinoApp)getActivity().getApplication()).getRepository().getDatabase();

                Completable.fromAction(mAppDatabase.forecastDao()::clear)
                        .subscribeOn(Schedulers.io())
                        .subscribe();

                Completable.fromAction(mAppDatabase.openWeatherDataForecastDao()::clear)
                        .subscribeOn(Schedulers.io())
                        .subscribe();

                Toast.makeText(getContext(), "Fatto!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

                return false;
            }
        });

        prefTelegram.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String uri = "https://www.t.me/Xiryl";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uri));
                startActivity(i);
                return false;
            }
        });

        prefPrivacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String uri = "https://www.chiarani.it/meteotrentinoapp_privacy_policy.pdf";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uri));
                startActivity(i);
                return false;
            }
        });

        prefWebsite.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String uri = "https://www.chiarani.it";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(uri));
                startActivity(i);
                return false;
            }
        });

        prefEmail.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "fabio@chiarani.it"});
                email.putExtra(Intent.EXTRA_SUBJECT, "MeteoTrentino App");

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Scegli un client email:"));
                return false;
            }
        });
    }

    private void popBackStack(FragmentManager manager){
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}