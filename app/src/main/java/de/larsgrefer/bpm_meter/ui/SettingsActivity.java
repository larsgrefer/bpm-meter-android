package de.larsgrefer.bpm_meter.ui;

import android.os.Bundle;
import android.preference.Preference;

import de.larsgrefer.bpm_meter.BuildConfig;
import de.larsgrefer.bpm_meter.R;
import io.freefair.android.appcompatPreference.AppCompatPreferenceActivity;

/**
 * @author Lars Grefer
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

        Preference versionPreference = findPreference("pref_version");
        versionPreference.setSummary(BuildConfig.VERSION_NAME + "\n" + BuildConfig.BUILD_TYPE);
    }
}
