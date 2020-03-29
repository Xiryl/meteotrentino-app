package it.chiarani.meteotrentinoapp.fragments;

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
import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.adapters.AllertAdapter;
import it.chiarani.meteotrentinoapp.adapters.ItemClickListener;
import it.chiarani.meteotrentinoapp.api.ProtezioneCivileAPI;
import it.chiarani.meteotrentinoapp.api.RetrofitAPI;
import it.chiarani.meteotrentinoapp.databinding.FragmentAllertBinding;
import it.chiarani.meteotrentinoapp.databinding.FragmentFaqBinding;
import it.chiarani.meteotrentinoapp.models.AllertItem;

public class FaqFragment extends Fragment {

    private FragmentFaqBinding binding;
    public FaqFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_faq, container, false);
        View view = binding.getRoot();

        binding.fragmentFaqBack.setOnClickListener( v->  popBackStack(getFragmentManager()));

        return view;
    }


    private void popBackStack(FragmentManager manager){
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}