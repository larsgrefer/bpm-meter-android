package com.larsgrefer.bpmMeter;

import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class ThemeHelper implements OnSharedPreferenceChangeListener {

	private static ThemeHelper me = null;

	private ThemeHelper() {
		childs = new LinkedList<>();
	}

	public static ThemeHelper getInstance() {
		if (me == null)
			me = new ThemeHelper();
		return me;
	}

	private List<Context> childs;

	public void register(Context con) {
		if (!childs.contains(con)) {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(con);
			sp.registerOnSharedPreferenceChangeListener(this);
			childs.add(con);
			setTheme(con, sp);
		}
	}

	public static void setTheme(Context con, SharedPreferences sp) {
		switch (Integer.parseInt(sp.getString("design", "0"))) {
		case 0:
			con.setTheme(R.style.bpmMeter_Dark);
			break;
		case 1:
			con.setTheme(R.style.bpmMeter_Light_DarkActionBar);
			break;
		case 2:
			con.setTheme(R.style.bpmMeter_Light);
			break;
		default:
			con.setTheme(R.style.bpmMeter_Dark);
			break;
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals("design")) {
			for (Context con : childs) {
				setTheme(con,
						PreferenceManager.getDefaultSharedPreferences(con));
				if (con instanceof Activity) {
					if (Build.VERSION.SDK_INT >= 11) {
						((Activity) con).recreate();
					}
				}
			}
		}
	}

}
