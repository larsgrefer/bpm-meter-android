package de.larsgrefer.bpm_meter.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import de.fhconfig.android.library.injection.annotation.Attribute;
import de.fhconfig.android.library.injection.annotation.XmlLayout;
import de.fhconfig.android.library.injection.annotation.XmlMenu;
import de.fhconfig.android.library.injection.annotation.XmlView;
import de.fhconfig.android.library.ui.injection.InjectionActionBarActivity;
import de.larsgrefer.bpm_meter.R;
import de.larsgrefer.bpm_meter.base.BpmMeter;
import de.larsgrefer.bpm_meter.base.MeasureType;

@XmlLayout(R.layout.main)
@XmlMenu(R.menu.menu)
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
				bpmMeter.setMeasureType((MeasureType) measureTypeSpinner.getSelectedItem());
				update();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				bpmMeter.setMeasureType(MeasureType.MT4_4);
				measureTypeSpinner.setSelection(0, true);
			}
		});
        TapTypeArrayAdapter tapTypeArrayAdapter = new TapTypeArrayAdapter(this, android.R.layout.simple_spinner_item, TapType.values());
        tapTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tapTypeSpinner.setAdapter(tapTypeArrayAdapter);
		tapTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				bpmMeter.setTapType(((TapType) tapTypeSpinner.getSelectedItem()).toTapType());
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

	@Attribute(id = android.R.attr.textColorHint, type = Attribute.Type.COLOR)
	int textColorHint;

    public void updateButtonText(){

        AbsoluteSizeSpan ass1 = new AbsoluteSizeSpan(100, true);
        AbsoluteSizeSpan ass2 = new AbsoluteSizeSpan(22, true);
		ForegroundColorSpan fcs2 = new ForegroundColorSpan(textColorHint);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        String line1 = getString(R.string.button_tap);
        String line2 = getResources().getString(R.string.once_per, getResources().getString(TapType.fromTapType(bpmMeter.getTapType()).getSingularNameResId()));

        spannableStringBuilder.append(line1);
        spannableStringBuilder.setSpan(ass1, 0, line1.length(), 0);
        spannableStringBuilder.append("\n");
        spannableStringBuilder.append(line2);
        spannableStringBuilder.setSpan(ass2, line1.length() + 1, line1.length() + 1 + line2.length(), 0);
		spannableStringBuilder.setSpan(fcs2, line1.length() + 1, line1.length() + 1 + line2.length(), 0);


        tapButton.setText(spannableStringBuilder);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/larsgrefer/bpm-meter-android"));
		menu.findItem(R.id.action_github).setIntent(githubIntent);

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
						.setTitle(R.string.about);
				builder.create().show();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	DecimalFormat xpmDecimalFormat = new DecimalFormat("0.##");
	DecimalFormat xdDecimalFormat = new DecimalFormat("0.#####");

	public void update() {

		updateButtonText();

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

    private class TapTypeArrayAdapter extends ArrayAdapter<TapType>{

        public TapTypeArrayAdapter(Context context, int resource) {
            super(context, resource);
        }

        public TapTypeArrayAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public TapTypeArrayAdapter(Context context, int resource, TapType[] objects) {
            super(context, resource, objects);
        }

        public TapTypeArrayAdapter(Context context, int resource, int textViewResourceId, TapType[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public TapTypeArrayAdapter(Context context, int resource, List<TapType> objects) {
            super(context, resource, objects);
        }

        public TapTypeArrayAdapter(Context context, int resource, int textViewResourceId, List<TapType> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            TapType item = getItem(position);

            if(view instanceof TextView){
                TextView textView = (TextView) view;

                textView.setText(getResources().getString(item.getPluralNameResId()));
            }
            return view;
        }

		@Override
		public View getDropDownView(int position, View convertView, ViewGroup parent) {
			View view = super.getDropDownView(position, convertView, parent);

			TapType item = getItem(position);

			if(view instanceof TextView){
				TextView textView = (TextView) view;

				textView.setText(getResources().getString(item.getPluralNameResId()));
			}

			return view;
		}
	}
}