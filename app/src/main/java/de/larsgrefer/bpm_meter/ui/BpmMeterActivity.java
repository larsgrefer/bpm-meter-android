package de.larsgrefer.bpm_meter.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.Menu;
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

import de.larsgrefer.bpm_meter.BpmMeter;
import de.larsgrefer.bpm_meter.MeasureType;
import de.larsgrefer.bpm_meter.R;
import de.larsgrefer.bpm_meter.TapType;
import io.freefair.android.injection.annotation.InjectAttribute;
import io.freefair.android.injection.annotation.InjectView;
import io.freefair.android.injection.annotation.XmlLayout;
import io.freefair.android.injection.annotation.XmlMenu;
import io.freefair.android.injection.app.InjectionAppCompatActivity;

import static io.freefair.android.injection.annotation.AttributeType.COLOR;

/**
 * @author Lars Grefer
 */
@XmlLayout(R.layout.main)
@XmlMenu(R.menu.menu)
public class BpmMeterActivity extends InjectionAppCompatActivity {

    private BpmMeter bpmMeter = new BpmMeter();

    @InjectView(R.id.button_tap)
    private Button tapButton;

    @InjectView(R.id.text_beats_per_minute)
    private EditText beatsPerMinuteText;

    @InjectView(R.id.text_beat_duration)
    private EditText beatDurationText;

    @InjectView(R.id.text_measures_per_minute)
    private EditText measuresPerMinuteText;

    @InjectView(R.id.text_measure_duration)
    private EditText measureDurationText;

    @InjectView(R.id.spinner_measure_type)
    private Spinner measureTypeSpinner;

    @InjectView(R.id.spinner_tap_type)
    private Spinner tapTypeSpinner;

    @InjectView(R.id.floating_action_button)
    private FloatingActionButton fab;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                bpmMeter.reset();
                update();
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
                bpmMeter.setTapType((TapType) tapTypeSpinner.getSelectedItem());
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

    @InjectAttribute(id = android.R.attr.textColorHint, type = COLOR)
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
