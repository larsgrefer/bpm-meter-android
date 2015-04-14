package de.larsgrefer.bpm_meter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.PluralsRes;

import de.fhconfig.android.library.Logger;

/**
 * Created by larsgrefer on 09.01.15.
 */
public enum TapType {
	BEATS(R.plurals.beat),
    MEASURES(R.plurals.measure);

    @PluralsRes
    private int namePluralsResId;

    TapType(@PluralsRes int namePluralsResId) {
        this.namePluralsResId = namePluralsResId;
    }

    @PluralsRes
    public int getNamePluralsResId() {
        return namePluralsResId;
    }
}
