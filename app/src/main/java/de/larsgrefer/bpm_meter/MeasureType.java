package de.larsgrefer.bpm_meter;

import androidx.annotation.NonNull;
import lombok.Getter;

/**
 * @author Lars Grefer
 */
@Getter
public enum MeasureType {

    MT4_4(4, "4/4"),
    MT3_4(3, "3/4"),
    MT2_4(2, "2/4");

    private int beatsPerMeasure;
    @NonNull
    private String displayName;

    MeasureType(int beatsPerMeasure, @NonNull String name) {
        this.beatsPerMeasure = beatsPerMeasure;
        this.displayName = name;
    }

    @Override
    @NonNull
    public String toString() {
        return displayName;
    }
}
