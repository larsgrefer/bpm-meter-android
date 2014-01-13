package com.larsgrefer.bpmMeter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.*;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.*;
import android.widget.*;

public class settingsActivity extends PreferenceActivity 
{
    @SuppressWarnings( "deprecation" )
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if( Build.VERSION.SDK_INT >= 11)
        {
        	switch( Integer.parseInt( sp.getString( "design", "0" )))
        	{
        		case 1:
        			this.setTheme( android.R.style.Theme_Holo );
        			break;
        		case 2:
        			this.setTheme( android.R.style.Theme_Holo_Light_DarkActionBar );
        			break;
        		case 3:
        			this.setTheme( android.R.style.Theme_Holo_Light );
        			break;
        		case 0:
        		default:
        			break;
        	}
        }
        else
        {
        	switch( Integer.parseInt( sp.getString( "design", "0" )))
        	{
        		case 1:
        			this.setTheme( android.R.style.Theme_Black );
        			break;
        		case 2:
        			break;
        		case 3:
        			this.setTheme( android.R.style.Theme_Light );
        			break;
        		case 0:
        		default:
        			break;
        	}
        }   
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        if( Build.VERSION.SDK_INT >= 11)
        {
        	getActionBar().setDisplayHomeAsUpEnabled( true );
        	if( Build.VERSION.SDK_INT>=14)
        		getActionBar().setHomeButtonEnabled( true );
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
