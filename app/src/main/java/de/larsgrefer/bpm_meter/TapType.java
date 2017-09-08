package de.larsgrefer.bpm_meter;

import android.support.annotation.StringRes;

/**
 * @author Lars Grefer
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
    public int getPluralNameResId() {
        return pluralNameResId;
    }
}
