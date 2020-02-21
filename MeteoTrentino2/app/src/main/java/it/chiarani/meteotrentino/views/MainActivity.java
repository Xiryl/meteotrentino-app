package it.chiarani.meteotrentino.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import it.chiarani.meteotrentino.R;
import it.chiarani.meteotrentino.databinding.ActivityMainBinding;
import it.chiarani.meteotrentino.utils.GPSUtils;
import it.chiarani.meteotrentino.utils.Localities;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void setActivityBinding() {
        binding = DataBindingUtil.setContentView(this, getLayoutID());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isGPSGranted = GPSUtils.checkGPSPermissions(this);

        accessLocation(isGPSGranted);
    }

    private void accessLocation(boolean isGPSGranted) {
        if(isGPSGranted) {
            // access to gps location
            String loc = GPSUtils.getLocation(this);
            boolean locationExists = Localities.checkIfLocationExists(loc);
            if(locationExists) {
                Toast.makeText(this, String.format("Rilevato: %s", loc), Toast.LENGTH_SHORT).show();
                // download data
            } else {
                // use previous location
                Toast.makeText(this, String.format("%s non valido", loc), Toast.LENGTH_SHORT).show();
            }
        } else {
            GPSUtils.askGPSPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    accessLocation(true);
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
