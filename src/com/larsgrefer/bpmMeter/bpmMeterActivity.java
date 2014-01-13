package com.larsgrefer.bpmMeter;

import java.text.*;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.*;
import android.widget.TextView.OnEditorActionListener;
import android.view.*;
import android.view.View.*;

public class bpmMeterActivity extends Activity implements DialogInterface.OnClickListener, OnClickListener, OnEditorActionListener, OnLongClickListener
{
    bpmMeter b;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        
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
        setContentView(R.layout.main);
        
        b = new bpmMeter();
        ( (Button)findViewById(R.id.button_tap) ).setOnClickListener( this );
        ( (Button)findViewById(R.id.button_tap) ).setOnLongClickListener( this );
        ( (Button)findViewById(R.id.button_reset) ).setOnClickListener( this );
        
        ( (EditText)findViewById( R.id.text_bpm ) ).setOnEditorActionListener( this );
        ( (EditText)findViewById( R.id.text_tpm ) ).setOnEditorActionListener( this );
        ( (EditText)findViewById( R.id.text_bd ) ).setOnEditorActionListener( this );
        ( (EditText)findViewById( R.id.text_td ) ).setOnEditorActionListener( this );
        
        updateLayout();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_reset:
                b.reset();
                this.update();
                return true;
            case R.id.menu_about:
            	AlertDialog.Builder builder = new AlertDialog.Builder(this);
            	builder.setNeutralButton( "OK", null )
            	.setPositiveButton( R.string.about_facebook, this )
            	.setMessage( R.string.about_message)
            	.setTitle( R.string.about_title )
            	.setIcon( android.R.drawable.ic_dialog_info);
            	builder.create().show();
                return false;
            case R.id.menu_settings:
            	Intent i = new Intent( this.getApplicationContext(), settingsActivity.class );
            	startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	public void onClick(View v) 
	{
		if( v.getId() == R.id.button_tap )
			b.tap();
		else if( v.getId() == R.id.button_reset)
			b.reset();
		this.update();
	}
	
	public void update()
	{
		DecimalFormat df = new DecimalFormat( "0.00" );
		
		( (EditText)findViewById( R.id.text_bpm ) ).setText( df.format(b.bpm) );
		( (EditText)findViewById( R.id.text_tpm ) ).setText( df.format(b.tpm) );
		
		df = new DecimalFormat( "0.00000" );
		
		( (EditText)findViewById( R.id.text_bd ) ).setText( df.format(b.bd) );
		( (EditText)findViewById( R.id.text_td ) ).setText( df.format(b.td) );
		
		int i = ( (Spinner)findViewById( R.id.spinner_taktart) ).getSelectedItemPosition();
		b.taktart = (byte)(( i * -1 ) + 4);
		
		i = ( (Spinner)findViewById( R.id.spinner_tapart) ).getSelectedItemPosition();
		b.takte = ( i == 0 );
		
		updateLayout();
	}

	public void updateLayout()
	{
		Button b = ( (Button)findViewById(R.id.button_tap) );
		if( b.getMeasuredHeight() < b.getMeasuredWidth() )
		{
			float f = b.getMeasuredHeight() / getResources().getDisplayMetrics().density;
			float g = f*0.5f;
			if( g > 0 )
			b.setTextSize( g );
		}
		else
		{
			float f = b.getMeasuredHeight() / getResources().getDisplayMetrics().density;
			float g = f*0.4f;
			if( g > 0 )
			b.setTextSize( g );
			
		}
		
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        if( Build.VERSION.SDK_INT >= 11 )
        {
        	( (Button)findViewById(R.id.button_reset) ).setVisibility( View.GONE );
        }
		
		if( Build.VERSION.SDK_INT < 11 || (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && sharedPref.getBoolean( "showResetInLandscape", false )) )
			( (Button)findViewById(R.id.button_reset) ).setVisibility( View.VISIBLE );
	}
	
	public boolean onEditorAction(TextView v, int ai, KeyEvent e) 
	{
		String s = ( (EditText)v ).getText().toString();
		DecimalFormat df = new DecimalFormat();
		double d = 0;
		try 
		{
			s = s.replace( ",", "." );
			d = df.parse( s ).doubleValue();
		}
		catch (ParseException e1)
		{			
			try 
			{
				s = s.replace( ".", "," );
				d = df.parse( s ).doubleValue();
			}
			catch (ParseException e2)
			{			
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage( R.string.message_parseError )
				       .setCancelable(false)
				       .setIcon( android.R.drawable.ic_dialog_alert )
				       .setNeutralButton
				       (
				    		   R.string.button_parseError,
				    		   new DialogInterface.OnClickListener() 
				    		   {
				    			   public void onClick(DialogInterface dialog, int id)
				    			   {
				                
				    			   }
			    		   });
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
		
		if( v.getId() == R.id.text_bpm )
		{
			b.setBPM( d );
			update( );
			return true;
		}
		else if( v.getId() == R.id.text_tpm )
		{
			b.setTPM( d );
			update();
			return true;
		}
		else if( v.getId() == R.id.text_bd )
		{
			( (Spinner)findViewById( R.id.spinner_tapart ) ).setSelection(1, true);
			b.setBD(d);
			update();
			return true;
		}
		else if( v.getId() == R.id.text_td )
		{
			( (Spinner)findViewById( R.id.spinner_tapart ) ).setSelection(0, true);
			b.setTD(d);
			update();
			return true;
		}
		
		return false;
	}

	
	public void onClick( DialogInterface dialog, int which ){
		if( which == AlertDialog.BUTTON_POSITIVE )
		{
			Uri webpage = Uri.parse("http://www.facebook.com/bpmMeter");
			Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
			startActivity(webIntent);
		}		
	}


	public boolean onLongClick( View v )
	{
		if( v.getId() == R.id.button_tap && PreferenceManager.getDefaultSharedPreferences( this ).getBoolean( "longPressReset", true ) )
		{
			b.reset();
			update();
			return true;
		}
		return false;
	}
}