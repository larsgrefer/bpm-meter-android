package de.larsgrefer.bpm_meter.ui;

import android.support.annotation.StringRes;

import de.larsgrefer.bpm_meter.R;

public enum TapType {
	BEATS(R.string.beat, R.string.beats),
    MEASURES(R.string.measure, R.string.measures);

    @StringRes
    private int singularNameResId;

    @StringRes
    private int pluralNameResId;

    TapType(@StringRes int singularNameResId, @StringRes int pluralNameResId) {
        this.singularNameResId = singularNameResId;
        this.pluralNameResId = pluralNameResId;
    }

    @StringRes
    public int getSingularNameResId() {
        return singularNameResId;
    }

    @StringRes
    public int getPluralNameResId(){
        return pluralNameResId;
    }

    public de.larsgrefer.bpm_meter.base.TapType toTapType() {
        if(this == MEASURES)
            return de.larsgrefer.bpm_meter.base.TapType.MEASURES;
        if(this == BEATS)
            return de.larsgrefer.bpm_meter.base.TapType.BEATS;
        return de.larsgrefer.bpm_meter.base.TapType.MEASURES;
    }

    public static TapType fromTapType(de.larsgrefer.bpm_meter.base.TapType source){
        if(source == de.larsgrefer.bpm_meter.base.TapType.MEASURES)
            return MEASURES;
        if(source == de.larsgrefer.bpm_meter.base.TapType.BEATS)
            return BEATS;
        return MEASURES;
    }
}
