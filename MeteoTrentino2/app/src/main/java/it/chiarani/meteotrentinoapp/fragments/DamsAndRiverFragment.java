package it.chiarani.meteotrentinoapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.adapters.BacinoDataAdapter;
import it.chiarani.meteotrentinoapp.api.BaciniModel.Bacini;
import it.chiarani.meteotrentinoapp.api.BaciniModel.ListaBacini;
import it.chiarani.meteotrentinoapp.api.BaciniModel.Stazioni;
import it.chiarani.meteotrentinoapp.api.ChiaraniJsonSchemaAPI;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.DatiOggi;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.Precipitazione;
import it.chiarani.meteotrentinoapp.api.RetrofitAPI;
import it.chiarani.meteotrentinoapp.api.RiverAndDamsAPI;
import it.chiarani.meteotrentinoapp.databinding.FragmentDamsAndRiverDataBinding;
import it.chiarani.meteotrentinoapp.utils.CustomDateFormatter;
import okhttp3.ResponseBody;

public class DamsAndRiverFragment extends Fragment {

    private FragmentDamsAndRiverDataBinding binding;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private RetrofitAPI chiaraniJsonSchemaAPI, damsAndRiverAPI;
    private ListaBacini mListaBacini;

    public DamsAndRiverFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dams_and_river_data, container, false);
        View view = binding.getRoot();

        binding.fragmentDamsAndRiverDataBack.setOnClickListener( v -> popBackStack(getFragmentManager()));

        chiaraniJsonSchemaAPI = ChiaraniJsonSchemaAPI.getInstance();
        damsAndRiverAPI = RiverAndDamsAPI.getInstance();

        mDisposable.add(chiaraniJsonSchemaAPI.getDamsAndRiverJson()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {

                    mListaBacini = model;

                    List<String> convertedBaciniNames = new ArrayList<>();

                    for(Bacini bacino : model.getBacini()) {
                       convertedBaciniNames.add(bacino.getNomeBacino());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, convertedBaciniNames);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.fragmentDamsAndRiverDataSpinnerBaciniName.setAdapter(adapter);

                    binding.fragmentDamsAndRiverDataSpinnerBaciniName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            setStationList(position);

                            ((TextView) parentView.getChildAt(0)).setTextColor(Color.parseColor("#A4A4A4"));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });


                }, throwable ->
                {
                    int x = 1;
                    // TODO: handle error
                }));
        return view;
    }

    private void retriveSensorData(String sensor, String argument) {
        mDisposable.add(damsAndRiverAPI.getRiverSensorData(sensor, argument)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    List<String> data = extractAPIData(model);

                    if (data == null) {
                        // TODO: handle error
                    }

                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.VERTICAL);
                    BacinoDataAdapter mAdapter = new BacinoDataAdapter(data);

                    binding.fragmentDamsAndRiverDataRv.setLayoutManager(linearLayoutManagerTags);
                    binding.fragmentDamsAndRiverDataRv.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                    setGraph(data);
                    binding.fragmentDamsAndRiverAnimLoad.setVisibility(View.GONE);

                }, throwable ->
                {
                    int x = 1;
                    // TODO: handle error
                }));
    }

    private List<String> extractAPIData(ResponseBody model) {
        InputStream is = model.byteStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = "";
        List<String> data = new ArrayList<>();
        try {
            reader.readLine();
            reader.readLine();
            reader.readLine();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                data.add(line);
            }
            return data;
        }catch (Exception ex) {
            return null;
        }

    }

    private void setStationList(int bacinoIndex) {
        List<String> convertedStationNames = new ArrayList<>();

        for(Stazioni stazione : mListaBacini.getBacini().get(bacinoIndex).getStazioni()) {
            if(stazione.getNomeStazione() != null || !stazione.getNomeStazione().isEmpty()) {
                convertedStationNames.add(stazione.getNomeStazione());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, convertedStationNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentDamsAndRiverDataSpinnerBaciniStation.setAdapter(adapter);

       binding.fragmentDamsAndRiverDataSpinnerBaciniStation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                setSensorsList(bacinoIndex, position);

                ((TextView) parentView.getChildAt(0)).setTextColor(Color.parseColor("#A4A4A4"));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void setSensorsList(int bacinoIndex, int stationIndex) {
        List<String> convertedSensors = new ArrayList<>();

        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getBarometro() == null || !mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getBarometro().getApiBarometro().isEmpty()) {
            convertedSensors.add("Barometro");
        }
        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getDirezioneVento() == null ||  !mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getDirezioneVento().getApiDirezioneVento().isEmpty()) {
            convertedSensors.add("Direzione Vento");
        }
        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getIdrometro() == null || !mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getIdrometro().getApiIdrometro().isEmpty()) {
            convertedSensors.add("Idrometro");
        }
        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getIgrometro() == null ||!mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getIgrometro().getApiIgrometro().isEmpty()) {
            convertedSensors.add("Igometro");
        }
        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getNivometro() == null || !mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getNivometro().getApiNivometro().isEmpty()) {
            convertedSensors.add("Nivometro");
        }
        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getPluviometro() == null ||!mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getPluviometro().getApiPluviometro().isEmpty()) {
            convertedSensors.add("Pluviometro");
        }
        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getPortata() == null ||!mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getPortata().getApiPortata().isEmpty()) {
            convertedSensors.add("Portata");
        }
        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getRadiometro() == null ||!mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getRadiometro().getApiRadiometro().isEmpty()){
            convertedSensors.add("Radiometro");
        }
        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getTemperaturaAria() == null ||!mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getTemperaturaAria().getApiTemperaturaAria().isEmpty()) {
            convertedSensors.add("Temperatura Aria");
        }
        if(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getVento() == null ||!mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getVento().getApiVento().isEmpty()) {
            convertedSensors.add("Vento");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, convertedSensors);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentDamsAndRiverDataSpinnerBaciniSensors.setAdapter(adapter);

        binding.fragmentDamsAndRiverDataSpinnerBaciniSensors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String sensor = convertedSensors.get(position);

                ((TextView) parentView.getChildAt(0)).setTextColor(Color.parseColor("#A4A4A4"));

                    switch (sensor) {
                        case "Barometro":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getBarometro().getIdSensore() + "", "0");
                            break;
                        case "Direzione Vento":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getDirezioneVento().getIdSensore() + "", "0");
                            break;
                        case "Idrometro":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getIdrometro().getIdSensore() + "", "0");
                            break;
                        case "Igometro":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getIgrometro().getIdSensore() + "", "0");
                            break;
                        case "Nivometro":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getNivometro().getIdSensore() + "", "0");
                            break;
                        case "Pluviometro":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getPluviometro().getIdSensore() + "", "0");
                            break;
                        case "Portata":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getPortata().getIdSensore() + "", "0");
                            break;
                        case "Radiometro":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getRadiometro().getIdSensore() + "", "0");
                            break;
                        case "Temperatura Aria":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getTemperaturaAria().getIdSensore() + "", "0");
                            break;
                        case "Vento":

                            retriveSensorData(mListaBacini.getBacini().get(bacinoIndex).getStazioni().get(stationIndex).getSensori().getVento().getIdSensore() + "", "0");
                            break;
                    }


                }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    private void setGraph(List<String> model) {
        List<Entry> entries = new ArrayList<>();

        for (String line : model) {
            // turn your data into Entry objects
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                String tmp = line.split(";")[0]+ ":00";
                Date date = (Date)inputFormat.parse(tmp);
                long time = date.getTime();

                String value = line.split(";")[1].replace(",", ".");
                entries.add(new Entry(time, Float.parseFloat(value)));
            }catch (Exception ex) {
                int dx = 1;
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        LineData lineData = new LineData(dataSet);

        dataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.bg_fade_blue);
        dataSet.setFillDrawable(drawable);

        binding.fragmentDamsAndRiverDataChart.getXAxis().setValueFormatter(new CustomDateFormatter());

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.fragmentDamsAndRiverDataChart.getAxisLeft().setTextColor(Color.WHITE);
            binding.fragmentDamsAndRiverDataChart.getAxisRight().setTextColor(Color.WHITE);
            binding.fragmentDamsAndRiverDataChart.getXAxis().setTextColor(Color.WHITE);
            binding.fragmentDamsAndRiverDataChart.getLegend().setTextColor(Color.WHITE);
        } else {
            binding.fragmentDamsAndRiverDataChart.getAxisLeft().setTextColor(Color.BLACK);
            binding.fragmentDamsAndRiverDataChart.getAxisRight().setTextColor(Color.BLACK);
            binding.fragmentDamsAndRiverDataChart.getXAxis().setTextColor(Color.BLACK);
            binding.fragmentDamsAndRiverDataChart.getLegend().setTextColor(Color.BLACK);
        }

        binding.fragmentDamsAndRiverDataChart.getDescription().setText("Rapp");
        binding.fragmentDamsAndRiverDataChart.getAxisLeft().setDrawGridLines(false);
        binding.fragmentDamsAndRiverDataChart.getXAxis().setDrawGridLines(false);
        binding.fragmentDamsAndRiverDataChart.getLegend().setEnabled(false);
        binding.fragmentDamsAndRiverDataChart.getXAxis().setGranularity(2000f);
        binding.fragmentDamsAndRiverDataChart.getXAxis().setLabelRotationAngle(-45);
        binding.fragmentDamsAndRiverDataChart.setData(lineData);
        binding.fragmentDamsAndRiverDataChart.invalidate(); // refresh
        binding.fragmentDamsAndRiverDataChart.refreshDrawableState();
    }

    private void popBackStack(FragmentManager manager){
        mDisposable.dispose();
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
