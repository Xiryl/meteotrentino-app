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
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.adapters.StationDataRainAdapter;
import it.chiarani.meteotrentinoapp.adapters.StationDataTemperatureAdapter;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsAPI;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.Anagrafica;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.DatiOggi;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.Precipitazione;
import it.chiarani.meteotrentinoapp.api.MeteoTrentinoStationsModel.TemperaturaAria;
import it.chiarani.meteotrentinoapp.api.RetrofitAPI;
import it.chiarani.meteotrentinoapp.databinding.FragmentStationDataBinding;
import it.chiarani.meteotrentinoapp.utils.CustomDateFormatter;

public class StationDataFragment extends Fragment {

    private FragmentStationDataBinding binding;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private RetrofitAPI meteoTrentinoAPI;
    private DatiOggi mDatiOggi;

    public StationDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_station_data, container, false);
        View view = binding.getRoot();

        binding.fragmentStationDataBack.setOnClickListener( v -> popBackStack(getFragmentManager()));

        meteoTrentinoAPI = MeteoTrentinoStationsAPI.getInstance();

        mDisposable.add(meteoTrentinoAPI.getMeteoTrentinoStationList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    List<Anagrafica> anagraficaList = model.getAnagrafica();
                    List<String> convertedAnagrafica = new ArrayList<>();

                    for(Anagrafica val : anagraficaList) {
                        convertedAnagrafica.add(String.format("%s: %s", val.getCodice(), val.getNomebreve()));
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, convertedAnagrafica);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.fragmentStationDataSpinner.setAdapter(adapter);

                    binding.fragmentStationDataSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            retriveStationData(model.getAnagrafica().get(position).getCodice());
                            ((TextView) parentView.getChildAt(0)).setTextColor(Color.parseColor("#A4A4A4"));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });

                }, throwable ->
                {
                    // TODO: handle error
                }));

        binding.fragmentStationDataRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == binding.fragmentStationDataRadioTemperatura.getId()) {
                Collections.reverse(mDatiOggi.getTemperature().temperaturaAria());
                StationDataTemperatureAdapter mAdapter = new StationDataTemperatureAdapter(mDatiOggi.getTemperature().temperaturaAria());
                binding.fragmentStationDataRv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                // setTemperatureGraph(mDatiOggi);
            } else {
                Collections.reverse(mDatiOggi.getTemperature().temperaturaAria());
                StationDataRainAdapter mAdapter = new StationDataRainAdapter(mDatiOggi.getPrecipitazioni().getPrecipitazione());
                binding.fragmentStationDataRv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
               // setRainGraph(mDatiOggi);
            }
        });

        return view;
    }

    private void retriveStationData(String code) {
        mDisposable.add(meteoTrentinoAPI.getMeteoTrentinoStationData(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    mDatiOggi = model;

                    binding.fragmentStationDataRadioTemperatura.setEnabled(true);
                    binding.fragmentStationDataRadioPioggia.setEnabled(true);
                    binding.fragmentStationDataAnimLoad.setVisibility(View.GONE);
                    binding.fragmentStationDataTitleAndamentoPioggia.setVisibility(View.VISIBLE);
                    binding.fragmentStationDataTitleAndamentoTemperatura.setVisibility(View.VISIBLE);
                    binding.fragmentStationDataRv.setVisibility(View.VISIBLE);
                    binding.fragmentStationDataChart.setVisibility(View.VISIBLE);
                    binding.fragmentStationDataChartRain.setVisibility(View.VISIBLE);

                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.VERTICAL);
                    binding.fragmentStationDataRv.setLayoutManager(linearLayoutManagerTags);

                    setTemperatureGraph(model);
                    setRainGraph(model);

                    Collections.reverse(model.getTemperature().temperaturaAria());
                    Collections.reverse(model.getPrecipitazioni().getPrecipitazione());

                    binding.fragmentStationDataLastTemperatureValue.setText(model.getTemperature().temperaturaAria().get(0).getTemperatura() + " °C");
                    binding.fragmentStationDataLastRainValue.setText(model.getPrecipitazioni().getPrecipitazione().get(0).getPioggia() + " mm");

                    StationDataTemperatureAdapter mAdapter = new StationDataTemperatureAdapter(model.getTemperature().temperaturaAria());
                    binding.fragmentStationDataRv.setAdapter(mAdapter);

                }, throwable ->
                {
                    binding.fragmentStationDataChartRain.setVisibility(View.GONE);
                    binding.fragmentStationDataTitleAndamentoPioggia.setVisibility(View.GONE);
                    binding.fragmentStationDataTitleAndamentoTemperatura.setVisibility(View.GONE);
                    binding.fragmentStationDataChart.setVisibility(View.GONE);
                    binding.fragmentStationDataLastRainValue.setText("--");
                    binding.fragmentStationDataLastTemperatureValue.setText("--");
                    binding.fragmentStationDataRadioTemperatura.setEnabled(false);
                    binding.fragmentStationDataRadioPioggia.setEnabled(false);
                    binding.fragmentStationDataRv.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), "Stazione non attiva", Toast.LENGTH_LONG).show();
                    binding.fragmentStationDataAnimLoad.setVisibility(View.VISIBLE);
                }));
    }

    private void setTemperatureGraph(DatiOggi model) {
        List<Entry> entries = new ArrayList<>();
        for (TemperaturaAria data : model.getTemperature().temperaturaAria()) {
            // turn your data into Entry objects
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date date = (Date)inputFormat.parse(data.getData());
                long time = date.getTime();

                entries.add(new Entry(time, Float.parseFloat(data.getTemperatura())));
            }catch (Exception ex) {

            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        LineData lineData = new LineData(dataSet);

        dataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.bg_fade_blue);
        dataSet.setFillDrawable(drawable);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            binding.fragmentStationDataChart.getAxisLeft().setTextColor(Color.WHITE);
            binding.fragmentStationDataChart.getAxisRight().setTextColor(Color.WHITE);
            binding.fragmentStationDataChart.getXAxis().setTextColor(Color.WHITE);
            binding.fragmentStationDataChart.getLegend().setTextColor(Color.WHITE);
        } else {
            binding.fragmentStationDataChart.getAxisLeft().setTextColor(Color.BLACK);
            binding.fragmentStationDataChart.getAxisRight().setTextColor(Color.BLACK);
            binding.fragmentStationDataChart.getXAxis().setTextColor(Color.BLACK);
            binding.fragmentStationDataChart.getLegend().setTextColor(Color.BLACK);
        }

        binding.fragmentStationDataChart.getDescription().setText("Temperature");
        binding.fragmentStationDataChart.getAxisLeft().setDrawGridLines(false);
        binding.fragmentStationDataChart.getXAxis().setDrawGridLines(false);
        binding.fragmentStationDataChart.getLegend().setEnabled(false);
        binding.fragmentStationDataChart.getXAxis().setValueFormatter(new CustomDateFormatter());
        binding.fragmentStationDataChart.getXAxis().setGranularity(2000f);
        binding.fragmentStationDataChart.getXAxis().setLabelRotationAngle(-45);
        binding.fragmentStationDataChart.setData(lineData);
        binding.fragmentStationDataChart.invalidate(); // refresh
    }

    private void setRainGraph(DatiOggi model) {
        List<Entry> entries = new ArrayList<>();
        for (Precipitazione data : model.getPrecipitazioni().getPrecipitazione()) {
            // turn your data into Entry objects
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date date = (Date)inputFormat.parse(data.getData());
                long time = date.getTime();

                entries.add(new Entry(time, Float.parseFloat(data.getPioggia())));
            }catch (Exception ex) {

            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
        LineData lineData = new LineData(dataSet);

        dataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.bg_fade_blue);
        dataSet.setFillDrawable(drawable);

        binding.fragmentStationDataChartRain.getDescription().setText("Piogge");
        binding.fragmentStationDataChartRain.getAxisLeft().setDrawGridLines(false);
        binding.fragmentStationDataChartRain.getXAxis().setDrawGridLines(false);
        binding.fragmentStationDataChartRain.getLegend().setEnabled(false);
        binding.fragmentStationDataChartRain.getXAxis().setValueFormatter(new CustomDateFormatter());
        binding.fragmentStationDataChartRain.getXAxis().setGranularity(2000f);
        binding.fragmentStationDataChartRain.getXAxis().setLabelRotationAngle(-45);
        binding.fragmentStationDataChartRain.setData(lineData);
        binding.fragmentStationDataChartRain.invalidate(); // refresh
    }

    private void popBackStack(FragmentManager manager){
        mDisposable.dispose();
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
