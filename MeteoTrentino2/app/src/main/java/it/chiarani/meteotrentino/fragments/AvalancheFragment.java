package it.chiarani.meteotrentino.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import it.chiarani.meteotrentino.AppExecutors;
import it.chiarani.meteotrentino.MeteoTrentinoApp;
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.adapters.AvalancheAdapter;
import it.chiarani.meteotrentino.api.AvalancheAPI;
import it.chiarani.meteotrentino.api.RetrofitAPI;
import it.chiarani.meteotrentino.databinding.FragmentAvalancheBinding;
import it.chiarani.meteotrentino.db.AppDatabase;

public class AvalancheFragment extends Fragment {

    private FragmentAvalancheBinding binding;

    private AppExecutors mAppExecutors;
    private AppDatabase mAppDatabase;
    private boolean zoomOut =  false;
    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public AvalancheFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_avalanche, container, false);
        View view = binding.getRoot();

        binding.fragmentAvalancheBack.setOnClickListener(v -> popBackStack(getFragmentManager()));

        getExecutors();

        RetrofitAPI avalancheAPI = AvalancheAPI.getInstance();

        Calendar calendar = Calendar.getInstance();

        String month = "";
        if (calendar.get(Calendar.MONTH) < 10) {
            month = "0" + (calendar.get(Calendar.MONTH)+1);
        } else {
            month = "" + (calendar.get(Calendar.MONTH)+1);
        }

        String todayDate = String.format("%s-%s-%s", calendar.get(Calendar.YEAR), month, calendar.get(Calendar.DAY_OF_MONTH));

        mDisposable.add(avalancheAPI.getAvalancheReport(todayDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    setMainImage("https://avalanche.report/albina_files/"+todayDate+"/fd_albina_map.jpg", binding.fragmentAvalancheMainImg);


                    // set recyclerview
                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.VERTICAL);
                    binding.fragmentAvalancheRv.setLayoutManager(linearLayoutManagerTags);
                    binding.fragmentAvalancheAnimLoad.setVisibility(View.GONE);
                    AvalancheAdapter mAdapter = new AvalancheAdapter(model, todayDate);
                    binding.fragmentAvalancheRv.setAdapter(mAdapter);
                    binding.fragmentAvalancheBtnDownload.setOnClickListener( v -> {
                        String uri = "https://avalanche.report/albina_files/"+todayDate+"/"+todayDate+"_it.pdf";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(uri));
                        startActivity(i);
                    });

                    binding.fragmentAvalancheMainImg.setOnClickListener( v -> {
                        if(zoomOut) {
                            binding.fragmentAvalancheMainImg.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
                            binding.fragmentAvalancheMainImg.setAdjustViewBounds(true);
                            zoomOut =false;
                        }else{
                            binding.fragmentAvalancheMainImg.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
                            binding.fragmentAvalancheMainImg.setScaleType(ImageView.ScaleType.FIT_XY);
                            zoomOut = true;
                        }
                    });
                }, throwable -> {
                    // TODO: handle error
                }));

        return view;
    }

    private void setMainImage(String url, ImageView imageView) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(imageView);
    }

    private void getExecutors() {
        mAppDatabase = ((MeteoTrentinoApp)getActivity().getApplication()).getRepository().getDatabase();
        mAppExecutors = ((MeteoTrentinoApp)getActivity().getApplication()).getRepository().getAppExecutors();
    }

    private void popBackStack(FragmentManager manager){
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }


}
