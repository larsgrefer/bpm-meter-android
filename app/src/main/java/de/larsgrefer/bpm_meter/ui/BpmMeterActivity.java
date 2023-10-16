package de.larsgrefer.bpm_meter.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import de.larsgrefer.bpm_meter.BpmMeter;
import de.larsgrefer.bpm_meter.MeasureType;
import de.larsgrefer.bpm_meter.R;
import de.larsgrefer.bpm_meter.TapType;

/**
 * @author Lars Grefer
 */
public class BpmMeterActivity extends AppCompatActivity {

    private BpmMeter bpmMeter = new BpmMeter();

    private Button tapButton;

    private EditText beatsPerMinuteText;
    private EditText beatDurationText;

    private EditText measuresPerMinuteText;
    private EditText measureDurationText;

    private Spinner measureTypeSpinner;
    private Spinner tapTypeSpinner;

    private FloatingActionButton fab;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        tapButton = findViewById(R.id.button_tap);
        beatsPerMinuteText = findViewById(R.id.text_beats_per_minute);
        beatDurationText = findViewById(R.id.text_beat_duration);

        measuresPerMinuteText = findViewById(R.id.text_measures_per_minute);
        measureDurationText = findViewById(R.id.text_measure_duration);

        measureTypeSpinner = findViewById(R.id.spinner_measure_type);
        tapTypeSpinner = findViewById(R.id.spinner_tap_type);

        fab = findViewById(R.id.floating_action_button);

        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{android.R.attr.textColorHint});
        textColorHint = typedArray.getColor(0, 0);

        tapButton.setOnClickListener(v -> {
            bpmMeter.tap();
            update();
        });
        tapButton.setOnLongClickListener(v -> {
            bpmMeter.reset();
            update();
            return true;
        });
        fab.setOnClickListener(v -> {
            bpmMeter.reset();
            update();
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
                bpmMeter.setTapType((TapType) tapTypeSpinner.getSelectedItem());
                update();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        beatsPerMinuteText.setOnEditorActionListener((v, actionId, event) -> {
            bpmMeter.setBeatsPerMinute(getDoubleFromTextView(v));
            update();
            return true;
        });
        measuresPerMinuteText.setOnEditorActionListener((v, actionId, event) -> {
            bpmMeter.setMeasuresPerMinute(getDoubleFromTextView(v));
            update();
            return true;
        });
        beatDurationText.setOnEditorActionListener((v, actionId, event) -> {
            tapTypeSpinner.setSelection(1, true);
            bpmMeter.setBeatDuration(getDoubleFromTextView(v));
            update();
            return true;
        });
        measureDurationText.setOnEditorActionListener((v, actionId, event) -> {
            measureTypeSpinner.setSelection(0, true);
            bpmMeter.setMeasureDuration(getDoubleFromTextView(v));
            update();
            return true;
        });

    }

    private int textColorHint;

    public void updateButtonText() {

        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.25f);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(textColorHint);

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        String line1 = getString(R.string.button_tap);
        String line2 = getResources().getString(R.string.once_per, getResources().getString(bpmMeter.getTapType().getSingularNameResId()));

        spannableStringBuilder.append(line1);
        spannableStringBuilder.append("\n");
        spannableStringBuilder.append(line2);
        spannableStringBuilder.setSpan(sizeSpan, line1.length() + 1, line1.length() + 1 + line2.length(), 0);
        spannableStringBuilder.setSpan(colorSpan, line1.length() + 1, line1.length() + 1 + line2.length(), 0);

        tapButton.setText(spannableStringBuilder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);

        Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/larsgrefer/bpm-meter-android"));
        menu.findItem(R.id.action_github).setIntent(githubIntent);

        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        menu.findItem(R.id.action_settings).setIntent(settingsIntent);

        return true;
    }

    private DecimalFormat xpmDecimalFormat = new DecimalFormat("0.##");
    private DecimalFormat xdDecimalFormat = new DecimalFormat("0.#####");

    public void update() {

        updateButtonText();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int fractionDigits = preferences.getInt("fractionDigits", 1);
        xpmDecimalFormat.setMaximumFractionDigits(fractionDigits);

        beatsPerMinuteText.setText(xpmDecimalFormat.format(bpmMeter.getBeatsPerMinute()));
        measuresPerMinuteText.setText(xpmDecimalFormat.format(bpmMeter.getMeasuresPerMinute()));

        beatDurationText.setText(xdDecimalFormat.format(bpmMeter.getBeatDuration()));
        measureDurationText.setText(xdDecimalFormat.format(bpmMeter.getMeasureDuration()));
    }

    public double getDoubleFromTextView(TextView v) {
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

    private class TapTypeArrayAdapter extends ArrayAdapter<TapType> {

        TapTypeArrayAdapter(Context context, int resource) {
            super(context, resource);
        }

        TapTypeArrayAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        TapTypeArrayAdapter(Context context, int resource, TapType[] objects) {
            super(context, resource, objects);
        }

        TapTypeArrayAdapter(Context context, int resource, int textViewResourceId, TapType[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        TapTypeArrayAdapter(Context context, int resource, List<TapType> objects) {
            super(context, resource, objects);
        }

        TapTypeArrayAdapter(Context context, int resource, int textViewResourceId, List<TapType> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);

            TapType item = getItem(position);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                if (item != null) {
                    textView.setText(getResources().getString(item.getPluralNameResId()));
                }
            }
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);

            TapType item = getItem(position);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;

                if (item != null) {
                    textView.setText(getResources().getString(item.getPluralNameResId()));
                }
            }

            return view;
        }
    }
}
