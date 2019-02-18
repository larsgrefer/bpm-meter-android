package de.larsgrefer.bpm_meter.ui;


import android.os.Bundle;

import androidx.preference.Preference;
import de.larsgrefer.bpm_meter.BuildConfig;
import de.larsgrefer.bpm_meter.R;

/**
 * @author Lars Grefer
 */
public class SettingsFragment extends androidx.preference.PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        Preference versionPreference = findPreference("pref_version");
        versionPreference.setSummary(BuildConfig.VERSION_NAME + "\n" + BuildConfig.BUILD_TYPE);
    }
}
