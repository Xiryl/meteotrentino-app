package it.chiarani.meteotrentinoapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * Get activity layout ID
     * @return activity layout ID
     */
    protected abstract int getLayoutID();

    /**
     * Set activity binding with UI
     */
    protected abstract void setActivityBinding();


    /**
     * Override onCreate method
     * @param savedInstanceState bundle
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Why? An Activity may not have a layout. If that is the case, layoutId is zero
        if (getLayoutID() == 0) {
            return;
        }

        setActivityBinding();
    }
}
