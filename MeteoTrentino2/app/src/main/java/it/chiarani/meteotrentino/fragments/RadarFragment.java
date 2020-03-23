package it.chiarani.meteotrentino.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

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
import it.chiarani.meteotrentino.config.Config;
import it.chiarani.meteotrentino.databinding.FragmentAllertBinding;
import it.chiarani.meteotrentino.databinding.FragmentRadarBinding;
import it.chiarani.meteotrentino.models.AllertItem;

public class RadarFragment extends Fragment {

    private FragmentRadarBinding binding;

    public RadarFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_radar, container, false);
        View view = binding.getRoot();

        binding.fragmentRadarBack.setOnClickListener(v -> popBackStack(getFragmentManager()));

        // infrarossi
        Glide.with(this)
                .load(Config.IMG_INFR_EUROPE)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        if(e != null) {
                            binding.fragmentAllertAnimLoadInfrarossoEuropa.setVisibility(View.VISIBLE);
                            binding.fragmentRadarImgInfrarossoEuropa.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        binding.fragmentAllertAnimLoadInfrarossoEuropa.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(binding.fragmentRadarImgInfrarossoEuropa);

        // radar
        Glide.with(this)
                .load(Config.IMG_RADAR)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        if(e != null) {
                            binding.fragmentAllertAnimLoadRadar.setVisibility(View.VISIBLE);
                            binding.fragmentRadarImgRadar.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        binding.fragmentAllertAnimLoadRadar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(binding.fragmentRadarImgRadar);


        // zona alpina
        Glide.with(this)
                .load(Config.IMG_ZONA_ALPINA)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        if(e != null) {
                            binding.fragmentAllertAnimLoadZonaAlpina.setVisibility(View.VISIBLE);
                            binding.fragmentRadarImgZonaAlpina.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        binding.fragmentAllertAnimLoadZonaAlpina.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(binding.fragmentRadarImgZonaAlpina);

        // radar neve
        Glide.with(this)
                .load(Config.IMG_RADAR_NEVE)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        if(e != null) {
                            binding.fragmentAllertAnimLoadNevicate.setVisibility(View.VISIBLE);
                            binding.fragmentRadarImgNevicate.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        binding.fragmentAllertAnimLoadNevicate.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(binding.fragmentRadarImgNevicate);

        // immagine europea
        Glide.with(this)
                .load(Config.IMG_EUROPA)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        if(e != null) {
                            binding.fragmentAllertAnimLoadEuropa.setVisibility(View.VISIBLE);
                            binding.fragmentRadarImgEuropa.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        binding.fragmentAllertAnimLoadEuropa.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(binding.fragmentRadarImgEuropa);

        return view;
    }


    private void popBackStack(FragmentManager manager){
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
