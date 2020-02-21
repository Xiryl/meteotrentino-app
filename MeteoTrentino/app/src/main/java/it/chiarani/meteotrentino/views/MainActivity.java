package it.chiarani.meteotrentino.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import it.chiarani.meteotrentino.R;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutID() {
        return 0;
    }

    @Override
    protected void setActivityBinding() {

    }

    @Override
    protected void setViewModel() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
