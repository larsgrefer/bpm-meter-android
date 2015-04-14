package de.larsgrefer.bpm_meter;

import android.support.annotation.StringRes;

/**
 * Created by larsgrefer on 09.01.15.
 */
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
}
