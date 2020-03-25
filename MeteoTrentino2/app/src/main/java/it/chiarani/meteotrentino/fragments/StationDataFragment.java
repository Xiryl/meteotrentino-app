package it.chiarani.meteotrentino.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.adapters.StationDataRainAdapter;
import it.chiarani.meteotrentino.adapters.StationDataTemperatureAdapter;
import it.chiarani.meteotrentino.api.MeteoTrentinoStationsAPI;
import it.chiarani.meteotrentino.api.MeteoTrentinoStationsModel.Anagrafica;
import it.chiarani.meteotrentino.api.MeteoTrentinoStationsModel.DatiOggi;
import it.chiarani.meteotrentino.api.MeteoTrentinoStationsModel.TemperaturaAria;
import it.chiarani.meteotrentino.api.RetrofitAPI;
import it.chiarani.meteotrentino.databinding.FragmentStationDataBinding;

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
            } else {
                Collections.reverse(mDatiOggi.getTemperature().temperaturaAria());
                StationDataRainAdapter mAdapter = new StationDataRainAdapter(mDatiOggi.getPrecipitazioni().getPrecipitazione());
                binding.fragmentStationDataRv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
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
                    binding.fragmentStationDataRv.setVisibility(View.VISIBLE);

                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.VERTICAL);
                    binding.fragmentStationDataRv.setLayoutManager(linearLayoutManagerTags);

                    long x = 0;
                    List<Entry> entries = new ArrayList<Entry>();
                    for (TemperaturaAria data : model.getTemperature().temperaturaAria()) {
                        // turn your data into Entry objects
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = (Date)formatter.parse(data.getData());
                        entries.add(new Entry(x++, Float.parseFloat(data.getTemperatura())));
                    }

                    LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to dataset
                    LineData lineData = new LineData(dataSet);
                    binding.fragmentStationDataChart.setData(lineData);
                    binding.fragmentStationDataChart.invalidate(); // refresh


                    Collections.reverse(model.getTemperature().temperaturaAria());
                    Collections.reverse(model.getPrecipitazioni().getPrecipitazione());

                    binding.fragmentStationDataLastTemperatureValue.setText(model.getTemperature().temperaturaAria().get(0).getTemperatura() + " °C");
                    binding.fragmentStationDataLastRainValue.setText(model.getPrecipitazioni().getPrecipitazione().get(0).getPioggia() + " mm");

                    StationDataTemperatureAdapter mAdapter = new StationDataTemperatureAdapter(model.getTemperature().temperaturaAria());
                    binding.fragmentStationDataRv.setAdapter(mAdapter);





                }, throwable ->
                {
                    binding.fragmentStationDataLastRainValue.setText("--");
                    binding.fragmentStationDataLastTemperatureValue.setText("--");
                    binding.fragmentStationDataRadioTemperatura.setEnabled(false);
                    binding.fragmentStationDataRadioPioggia.setEnabled(false);
                    binding.fragmentStationDataRv.setVisibility(View.GONE);
                    Toast.makeText(getActivity().getApplicationContext(), "Stazione non attiva", Toast.LENGTH_LONG).show();
                    binding.fragmentStationDataAnimLoad.setVisibility(View.VISIBLE);
                }));
    }

    private void popBackStack(FragmentManager manager){
        mDisposable.dispose();
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
