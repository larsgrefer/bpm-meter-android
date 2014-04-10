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

@SuppressLint("NewApi")
public class settingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		sp.registerOnSharedPreferenceChangeListener(this);

		setTheme(sp);
		
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
		
		if (Build.VERSION.SDK_INT >= 11) {
			getActionBar().setDisplayOptions(
					ActionBar.DISPLAY_HOME_AS_UP
					|ActionBar.DISPLAY_SHOW_TITLE
					|ActionBar.DISPLAY_SHOW_HOME);
			if (Build.VERSION.SDK_INT >= 14)
				getActionBar().setHomeButtonEnabled(true);
		}
		intiSummaries();
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
	    
		if(key.equals("design")){
			if(Build.VERSION.SDK_INT >= 11)
			{
				this.recreate();
			}
		}
		
		Preference pref = findPreference(key);

	    updateSummary(pref);
	}
	
	private void intiSummaries()
	{
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
	
	private void setTheme(SharedPreferences sp)
	{
		switch (Integer.parseInt(sp.getString("design", "0"))) {
		case 0:
			this.setTheme(R.style.bpmMeter_Dark);
			getApplication().setTheme(R.style.bpmMeter_Dark);
			break;
		case 1:
			this.setTheme(R.style.bpmMeter_Light_DarkActionBar);
			getApplication().setTheme(R.style.bpmMeter_Light_DarkActionBar);
			break;
		case 2:
			this.setTheme(R.style.bpmMeter_Light);
			getApplication().setTheme(R.style.bpmMeter_Light);
			break;
		default:
			this.setTheme(R.style.bpmMeter_Dark);
			getApplication().setTheme(R.style.bpmMeter_Dark);
			break;
		}
	}

}
