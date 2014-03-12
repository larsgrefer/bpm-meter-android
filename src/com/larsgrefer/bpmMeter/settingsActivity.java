package com.larsgrefer.bpmMeter;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.MenuItem;

public class settingsActivity extends PreferenceActivity {
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (Build.VERSION.SDK_INT >= 11) {
			switch (Integer.parseInt(sp.getString("design", "0"))) {
			
			case 2:
				this.setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat_Light_DarkActionBar);
				break;
			case 3:
				this.setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat_Light);
				break;
			case 0:
			case 1:
			default:
				this.setTheme(android.support.v7.appcompat.R.style.Theme_AppCompat);
				break;
			}
		} else {
			switch (Integer.parseInt(sp.getString("design", "0"))) {

			case 2:
			case 3:
				this.setTheme(android.R.style.Theme_Light);
				break;
			case 0:
			case 1:
			default:
				this.setTheme(android.R.style.Theme_Black);
				break;
			}
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
