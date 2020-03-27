package it.chiarani.meteotrentino.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import it.chiarani.meteotrentino.MeteoTrentinoApp;
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.adapters.AllertAdapter;
import it.chiarani.meteotrentino.adapters.DaysAdapter;
import it.chiarani.meteotrentino.adapters.ItemClickListener;
import it.chiarani.meteotrentino.adapters.ProbabilisticAdapter;
import it.chiarani.meteotrentino.adapters.ProbabilisticDescriptionAdapter;
import it.chiarani.meteotrentino.api.MeteoTrentinoAPI;
import it.chiarani.meteotrentino.api.MeteoTrentinoProbabilisticModel.Fasce;
import it.chiarani.meteotrentino.api.MeteoTrentinoProbabilisticModel.Giorni;
import it.chiarani.meteotrentino.api.MeteoTrentinoProbabilisticModel.MeteoTrentinoProbabilisticModel;
import it.chiarani.meteotrentino.api.ProtezioneCivileAPI;
import it.chiarani.meteotrentino.api.RetrofitAPI;
import it.chiarani.meteotrentino.config.Config;
import it.chiarani.meteotrentino.databinding.FragmentAllertBinding;
import it.chiarani.meteotrentino.databinding.FragmentProbabilisticBinding;
import it.chiarani.meteotrentino.db.AppDatabase;
import it.chiarani.meteotrentino.models.AllertItem;

public class ProbabilisticFragment extends Fragment implements ItemClickListener {

    private FragmentProbabilisticBinding binding;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private MeteoTrentinoProbabilisticModel meteoTrentinoProbabilisticModel;
    private List<Fasce> mProbalisisticsFasce;
    private ProbabilisticAdapter mAdapterProbabilistic;
    private ProbabilisticDescriptionAdapter mAdapterDesc;
    private DaysAdapter mAdapterDays;
    private AppDatabase mAppDatabase;

    public ProbabilisticFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_probabilistic, container, false);
        View view = binding.getRoot();

        RetrofitAPI meteoTrentinoAPI = MeteoTrentinoAPI.getInstance();
        mAppDatabase = ((MeteoTrentinoApp)getActivity().getApplication()).getRepository().getDatabase();

        binding.fragmentProbabilisticBack.setOnClickListener( v->  popBackStack(getFragmentManager()));

       // Uri.parse(URL_BOLLETTINO_PROBABILISTICO + entries.get(entries.size()-1).getIdPrevisione() + "&history=0"

        mDisposable.add(mAppDatabase.forecastDao().getAsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    binding.fragmentProbabilisticBtnDownload.setOnClickListener(v -> {
                        String uri = String.format("%s%s&history=0",
                                Config.PROB_DOWNLOAD_BULLETTIN,
                                model.get(model.size() -1).getIdPrevisione());
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(uri));
                        startActivity(i);
                    });
                }, throwable -> {
                    String uri = "www.meteotrentino.it";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(uri));
                    startActivity(i);
                }));

        mDisposable.add(meteoTrentinoAPI.getMeteoTrentinoProbabilistic()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    meteoTrentinoProbabilisticModel = model;
                    mProbalisisticsFasce = model.getGiorni().get(0).getFasce();
                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.HORIZONTAL);
                    binding.fragmentProbabilisticRv.setLayoutManager(linearLayoutManagerTags);
                    mAdapterProbabilistic = new ProbabilisticAdapter(mProbalisisticsFasce);
                    binding.fragmentProbabilisticAnimLoad.setVisibility(View.INVISIBLE);
                    binding.fragmentProbabilisticRv.setAdapter(mAdapterProbabilistic);

                    LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    binding.fragmentProbabilisticRvDescr.setLayoutManager(linearLayoutManager);
                    mAdapterDesc = new ProbabilisticDescriptionAdapter(model.getGiorni().get(0).getFasce().get(0).getFenomeni());
                    binding.fragmentProbabilisticRvDescr.setAdapter(mAdapterDesc);

                    LinearLayoutManager linearLayoutManagerDays = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerDays.setOrientation(RecyclerView.HORIZONTAL);
                    binding.fragmentProbabilisticRvDays.setLayoutManager(linearLayoutManagerDays);
                    mAdapterDays = new DaysAdapter(model.getGiorni(), this::onItemClick);
                    binding.fragmentProbabilisticRvDays.setAdapter(mAdapterDays);
                }, throwable -> {
                    if(throwable instanceof java.net.UnknownHostException) {
                        binding.fragmentProbabilisticAnimLoad.setAnimation(R.raw.anim_no_network);
                        binding.fragmentProbabilisticAnimLoad.playAnimation();
                    } else {
                        binding.fragmentProbabilisticAnimLoad.setAnimation(R.raw.anim_err);
                        binding.fragmentProbabilisticAnimLoad.playAnimation();
                    }
                }));
        return view;
    }

    @Override
    public void onItemClick(int position) {
        mProbalisisticsFasce = meteoTrentinoProbabilisticModel.getGiorni().get(position).getFasce();
        mAdapterProbabilistic = new ProbabilisticAdapter(mProbalisisticsFasce);
        binding.fragmentProbabilisticRv.setAdapter(mAdapterProbabilistic);
        mAdapterProbabilistic.notifyDataSetChanged();
    }

    private void popBackStack(FragmentManager manager){
        mDisposable.dispose();
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
