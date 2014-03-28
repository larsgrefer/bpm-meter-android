package com.larsgrefer.bpmMeter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

@SuppressLint("NewApi")
public class settingsActivity extends PreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);

		switch (Integer.parseInt(sp.getString("design", "0"))) {
		case 0:
			this.setTheme(R.style.bpmMeter_Dark);
			break;
		case 1:
			this.setTheme(R.style.bpmMeter_Light_DarkActionBar);
			break;
		case 2:
			this.setTheme(R.style.bpmMeter_Light);
			break;
		default:
			this.setTheme(R.style.bpmMeter_Dark);
			break;
		}
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		if (Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			if (Build.VERSION.SDK_INT >= 14)
				getActionBar().setHomeButtonEnabled(true);
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
