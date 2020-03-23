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
import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.adapters.AllertAdapter;
import it.chiarani.meteotrentino.adapters.ItemClickListener;
import it.chiarani.meteotrentino.api.ProtezioneCivileAPI;
import it.chiarani.meteotrentino.api.RetrofitAPI;
import it.chiarani.meteotrentino.databinding.FragmentAllertBinding;
import it.chiarani.meteotrentino.models.AllertItem;

public class AllertFragment extends Fragment implements ItemClickListener {

    private FragmentAllertBinding binding;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private List<AllertItem> allertItems;

    public AllertFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_allert, container, false);
        View view = binding.getRoot();

        allertItems = new ArrayList<>();

        RetrofitAPI protezioneCivileAPI = ProtezioneCivileAPI.getInstance();

        binding.fragmentAllertBack.setOnClickListener( v->  popBackStack(getFragmentManager()));

        mDisposable.add(protezioneCivileAPI.getAllert()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    Document doc = Jsoup.parse(model.string());

                    Elements links = doc.select("ul li span a");
                    Elements dates = doc.select("ul li span span");

                    int counter = 0;
                    for (Element link : links) {
                        try {
                            String onclick_attr = link.attr("onclick");
                            String sub_onclick = onclick_attr.substring(13);
                            String sub_link = sub_onclick.split(",")[0];
                            String document_link = sub_link.substring(0, sub_link.length()-1);
                            String final_link = "http://avvisi.protezionecivile.tn.it" + document_link;
                            final_link = "https://docs.google.com/gview?url=" + final_link + "&embedded=true";
                            String date_pt1 = dates.get(counter).text().split(" ")[1];
                            String date_pt2 = dates.get(counter).text().split(" ")[2];
                            String date_pt3 = dates.get(counter).text().split(" ")[3];

                            // add element to list
                            allertItems.add(new AllertItem(link.text(), date_pt1 ,date_pt2, date_pt3, final_link));
                        } catch (Exception ex) {
                            // nothing
                        }

                        counter++;
                        if(counter >= links.size())
                            break;
                    }

                    LinearLayoutManager linearLayoutManagerTags = new LinearLayoutManager(getActivity().getApplicationContext());
                    linearLayoutManagerTags.setOrientation(RecyclerView.VERTICAL);
                    binding.fragmentAllertRv.setLayoutManager(linearLayoutManagerTags);
                    AllertAdapter mAdapter = new AllertAdapter(allertItems, this::onItemClick);
                    binding.fragmentAllertAnimLoad.setVisibility(View.INVISIBLE);
                    binding.fragmentAllertRv.setAdapter(mAdapter);

                    binding.fragmentAllertRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (dy > 0 && binding.fragmentAllertEmergencyButton.getVisibility() == View.VISIBLE) {
                                binding.fragmentAllertEmergencyButton.setVisibility(View.INVISIBLE);
                            } else if (dy < 0 && binding.fragmentAllertEmergencyButton.getVisibility() != View.VISIBLE) {
                                binding.fragmentAllertEmergencyButton.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }, throwable -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Ooops", Toast.LENGTH_LONG).show();
                }));
        return view;
    }


    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(allertItems.get(position).getLink()));
        startActivity(i);
    }

    private void popBackStack(FragmentManager manager){
        mDisposable.dispose();
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
