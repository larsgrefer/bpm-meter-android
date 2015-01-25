package de.larsgrefer.bpm_meter.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.DecimalFormat;
import java.text.ParseException;

import de.larsgrefer.android.library.Logger;
import de.larsgrefer.android.library.injection.annotation.XmlLayout;
import de.larsgrefer.android.library.injection.annotation.XmlView;
import de.larsgrefer.android.library.ui.InjectionActionBarActivity;
import de.larsgrefer.bpm_meter.BpmMeter;
import de.larsgrefer.bpm_meter.MeasureType;
import de.larsgrefer.bpm_meter.R;
import de.larsgrefer.bpm_meter.TapType;

@XmlLayout(R.layout.main)
public class BpmMeterActivity extends InjectionActionBarActivity {
	BpmMeter bpmMeter;

	@XmlView(R.id.button_tap)
	Button tapButton;

	@XmlView(R.id.text_beats_per_minute)
	EditText beatsPerMinuteText;

	@XmlView(R.id.text_beat_duration)
	EditText beatDurationText;

	@XmlView(R.id.text_measures_per_minute)
	EditText measuresPerMinuteText;

	@XmlView(R.id.text_measure_duration)
	EditText measureDurationText;

	@XmlView(R.id.spinner_measure_type)
	Spinner measureTypeSpinner;

	@XmlView(R.id.spinner_tap_type)
	Spinner tapTypeSpinner;

	final Logger log = new Logger(this);

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		bpmMeter = new BpmMeter();

		tapButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bpmMeter.tap();
				update();
			}
		});
		tapButton.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				bpmMeter.reset();
				update();
				return true;
			}
		});
		ArrayAdapter<MeasureType> measureTypeArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, MeasureType.values());
		measureTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		measureTypeSpinner.setAdapter(measureTypeArrayAdapter);
		measureTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				MeasureType measureType = (MeasureType) measureTypeSpinner.getSelectedItem();
				bpmMeter.setMeasureType(measureType);
				update();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				bpmMeter.setMeasureType(MeasureType.MT4_4);
				measureTypeSpinner.setSelection(0, true);
			}
		});
		tapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				bpmMeter.setTapType((position == 0) ? TapType.MEASURES : TapType.BEATS);
				update();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		beatsPerMinuteText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				bpmMeter.setBeatsPerMinute(getDoubleFromTextView(v));
				update();
				return true;
			}
		});
		measuresPerMinuteText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				bpmMeter.setMeasuresPerMinute(getDoubleFromTextView(v));
				update();
				return true;
			}
		});
		beatDurationText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				tapTypeSpinner.setSelection(1, true);
				bpmMeter.setBeatDuration(getDoubleFromTextView(v));
				update();
				return true;
			}
		});
		measureDurationText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				measureTypeSpinner.setSelection(0, true);
				bpmMeter.setMeasureDuration(getDoubleFromTextView(v));
				update();
				return true;
			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);

		Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/larsgrefer/bpm-meter-android"));
		menu.findItem(R.id.action_github).setIntent(githubIntent);

		super.onCreateOptionsMenu(menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.action_reset:
				bpmMeter.reset();
				this.update();
				return true;
			case R.id.action_about:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setPositiveButton(android.R.string.ok, null)
						.setNeutralButton(R.string.action_github_long, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/larsgrefer/bpm-meter-android")));
							}
						})
						.setCancelable(true)
						.setMessage(R.string.about_message)
						.setTitle(R.string.about_title);
				builder.create().show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	DecimalFormat xpmDecimalFormat = new DecimalFormat("0.##");
	DecimalFormat xdDecimalFormat = new DecimalFormat("0.#####");

	public void update() {

		beatsPerMinuteText.setText(xpmDecimalFormat.format(bpmMeter.getBeatsPerMinute()));
		measuresPerMinuteText.setText(xpmDecimalFormat.format(bpmMeter.getMeasuresPerMinute()));

		beatDurationText.setText(xdDecimalFormat.format(bpmMeter.getBeatDuration()));
		measureDurationText.setText(xdDecimalFormat.format(bpmMeter.getMeasureDuration()));
	}

	public double getDoubleFromTextView(TextView v){
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
		return d;
	}
}