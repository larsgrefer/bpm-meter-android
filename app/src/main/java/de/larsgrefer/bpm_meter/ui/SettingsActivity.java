package de.larsgrefer.bpm_meter.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import de.larsgrefer.bpm_meter.R;

/**
 * @author Lars Grefer
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
    }

}
