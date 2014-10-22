package com.larsgrefer.bpmMeter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.DecimalFormat;
import java.text.ParseException;

public class BpmMeterActivity extends ActionBarActivity implements OnClickListener, OnEditorActionListener, OnLongClickListener {
	BpmMeter bpmMeter;



	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

		setSupportActionBar(toolbar);


		bpmMeter = new BpmMeter();
		findViewById(R.id.button_tap).setOnClickListener(this);
		findViewById(R.id.button_tap).setOnLongClickListener(this);
		((EditText) findViewById(R.id.text_bpm))
				.setOnEditorActionListener(this);
		((EditText) findViewById(R.id.text_tpm))
				.setOnEditorActionListener(this);
		((EditText) findViewById(R.id.text_bd)).setOnEditorActionListener(this);
		((EditText) findViewById(R.id.text_td)).setOnEditorActionListener(this);

		updateLayout();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		menu.findItem(R.id.menu_facebook).setIntent(getOpenFacebookIntent(getApplicationContext()));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.menu_reset:
				bpmMeter.reset();
				this.update();
				return true;
			case R.id.menu_about:
				TypedValue aboutIcon = new TypedValue();
				getTheme().resolveAttribute(R.attr.ic_dialog_about, aboutIcon, true);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setNeutralButton("OK", null)
						.setPositiveButton(R.string.about_facebook, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (which == AlertDialog.BUTTON_POSITIVE)
									startActivity(getOpenFacebookIntent(getApplicationContext()));
							}
						})
						.setMessage(R.string.about_message)
						.setTitle(R.string.about_title)
						.setIcon(aboutIcon.resourceId);
				builder.create().show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.button_tap)
			bpmMeter.tap();
		else if (v.getId() == R.id.button_reset)
			bpmMeter.reset();
		this.update();
	}

	public void update() {
		DecimalFormat df = new DecimalFormat("0.00");

		((EditText) findViewById(R.id.text_bpm)).setText(df.format(bpmMeter.bpm));
		((EditText) findViewById(R.id.text_tpm)).setText(df.format(bpmMeter.tpm));

		df = new DecimalFormat("0.00000");

		((EditText) findViewById(R.id.text_bd)).setText(df.format(bpmMeter.bd));
		((EditText) findViewById(R.id.text_td)).setText(df.format(bpmMeter.td));

		int i = ((Spinner) findViewById(R.id.spinner_taktart))
						.getSelectedItemPosition();
		bpmMeter.taktart = (byte) ((i * -1) + 4);

		i = ((Spinner) findViewById(R.id.spinner_tapart))
					.getSelectedItemPosition();
		bpmMeter.takte = (i == 0);

		updateLayout();
	}

	public void updateLayout() {
		Button b = ((Button) findViewById(R.id.button_tap));
		if (b.getMeasuredHeight() < b.getMeasuredWidth()) {
			float f = b.getMeasuredHeight()
							  / getResources().getDisplayMetrics().density;
			float g = f * 0.5f;
			if (g > 0)
				b.setTextSize(g);
		} else {
			float f = b.getMeasuredHeight()
							  / getResources().getDisplayMetrics().density;
			float g = f * 0.4f;
			if (g > 0)
				b.setTextSize(g);
		}
	}

	public boolean onEditorAction(TextView v, int ai, KeyEvent e) {
		String s = ((EditText) v).getText().toString();
		DecimalFormat df = new DecimalFormat();
		double d = 0;
		try {
			s = s.replace(",", ".");
			d = df.parse(s).doubleValue();
		} catch (ParseException e1) {
			try {
				s = s.replace(".", ",");
				d = df.parse(s).doubleValue();
			} catch (ParseException e2) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.message_parseError)
						.setCancelable(false)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setNeutralButton(R.string.button_parseError, null);
				AlertDialog alert = builder.create();
				alert.show();
			}
		}

		if (v.getId() == R.id.text_bpm) {
			bpmMeter.setBPM(d);
			update();
			return true;
		} else if (v.getId() == R.id.text_tpm) {
			bpmMeter.setTPM(d);
			update();
			return true;
		} else if (v.getId() == R.id.text_bd) {
			((Spinner) findViewById(R.id.spinner_tapart)).setSelection(1, true);
			bpmMeter.setBD(d);
			update();
			return true;
		} else if (v.getId() == R.id.text_td) {
			((Spinner) findViewById(R.id.spinner_tapart)).setSelection(0, true);
			bpmMeter.setTD(d);
			update();
			return true;
		}

		return false;
	}

	public boolean onLongClick(View v) {
		if (v.getId() == R.id.button_tap) {
			bpmMeter.reset();
			update();
			return true;
		}
		return false;
	}

	public static Intent getOpenFacebookIntent(Context c) {
		try {
			c.getPackageManager().getPackageInfo("com.facebook.katana", 0);
			return new Intent(Intent.ACTION_VIEW,
									 Uri.parse("fb://profile/213518895431506"));
		} catch (NameNotFoundException e) {
			return new Intent(Intent.ACTION_VIEW,
									 Uri.parse("https://www.facebook.com/bpmMeter"));
		}
	}
}