package it.chiarani.meteotrentinoapp.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.config.Config;
import it.chiarani.meteotrentinoapp.config.WebcamCSVList;
import it.chiarani.meteotrentinoapp.databinding.FragmentRadarBinding;
import it.chiarani.meteotrentinoapp.databinding.FragmentWebcamBinding;

public class WebcamFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentWebcamBinding binding;

    public WebcamFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_webcam, container, false);
        View view = binding.getRoot();

        binding.fragmentWebcamDataBack.setOnClickListener(v -> popBackStack(getFragmentManager()));


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), R.layout.spinner_item, WebcamCSVList.getWebcamNames());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.fragmentWebcamDataSpinnerWebcams.setAdapter(adapter);
        binding.fragmentWebcamDataSpinnerWebcams.setOnItemSelectedListener(this);

        return view;
    }


    private void popBackStack(FragmentManager manager){
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#A4A4A4"));
        String uri = WebcamCSVList.getWebcamUrl(position);
        Glide.with(this)
                .load(uri)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        Toast.makeText(getContext(), "Webcam non attiva.", Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(binding.fragmentWebcamDataSpinnerImgWebcam);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
