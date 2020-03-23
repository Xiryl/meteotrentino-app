package it.chiarani.meteotrentino.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import it.chiarani.meteotrentino.R;

public class FragmentLauncher {
    public static void launch(Fragment newFragment, FragmentManager fragmentManager) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack if needed
            transaction.replace(R.id.activity_home_frame_layout, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
    }
}
