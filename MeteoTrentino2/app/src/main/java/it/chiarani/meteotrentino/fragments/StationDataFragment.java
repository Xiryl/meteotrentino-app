package it.chiarani.meteotrentino.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.adapters.AllertAdapter;
import it.chiarani.meteotrentino.adapters.ItemClickListener;
import it.chiarani.meteotrentino.api.ProtezioneCivileAPI;
import it.chiarani.meteotrentino.api.RetrofitAPI;
import it.chiarani.meteotrentino.databinding.FragmentAllertBinding;
import it.chiarani.meteotrentino.databinding.FragmentStationDataBinding;
import it.chiarani.meteotrentino.models.AllertItem;

public class StationDataFragment extends Fragment {

    private FragmentStationDataBinding binding;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

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


        return view;
    }

    private void popBackStack(FragmentManager manager){
        mDisposable.dispose();
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
