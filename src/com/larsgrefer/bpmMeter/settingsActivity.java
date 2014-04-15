package com.larsgrefer.bpmMeter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.Toast;

@SuppressLint("NewApi")
public class settingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		sp.registerOnSharedPreferenceChangeListener(this);
		ThemeHelper.getInstance().register(this);
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.settings);

		if (Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayOptions(
					ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE
							| ActionBar.DISPLAY_SHOW_HOME);
			if (Build.VERSION.SDK_INT >= 14)
				getActionBar().setHomeButtonEnabled(true);
		}
		intiSummaries();
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (Build.VERSION.SDK_INT < 11 && key.equals("design")) {

			Toast.makeText(this, R.string.settings_design_hint, Toast.LENGTH_SHORT).show();
		}
		Preference pref = findPreference(key);

		updateSummary(pref);
	}

	@SuppressWarnings("deprecation")
	private void intiSummaries() {
		updateSummary(findPreference("design"));
	}

	private void updateSummary(Preference pref) {
		if (pref instanceof ListPreference) {
			ListPreference listPref = (ListPreference) pref;
			pref.setSummary(listPref.getEntry());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
