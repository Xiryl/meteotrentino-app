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

import it.chiarani.meteotrentinoapp.R;
import it.chiarani.meteotrentinoapp.databinding.FragmentAllertBinding;
import it.chiarani.meteotrentinoapp.databinding.FragmentDonationBinding;

public class DonationFragment extends Fragment {

    private FragmentDonationBinding binding;

    public DonationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_donation, container, false);
        View view = binding.getRoot();

        binding.fragmentDonationBack.setOnClickListener( v->  popBackStack(getFragmentManager()));

        binding.fragmentDonationBtn.setOnClickListener( v-> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.paypal.me/fabiochiarani"));
            startActivity(i);
        });
        return view;
    }

    private void popBackStack(FragmentManager manager){
        FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
        manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
