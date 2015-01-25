package de.larsgrefer.bpm_meter;

/**
 * Created by larsgrefer on 09.01.15.
 */
public enum MeasureType {

	MT4_4(4, "4/4"),
	MT3_4(3, "3/4"),
	MT2_4(2, "2/4");

	private int beatsPerMeasure;
	private String displayName;

	MeasureType(int beatsPerMeasure, String name) {
		this.beatsPerMeasure = beatsPerMeasure;
		this.displayName = name;
	}

	public int getBeatsPerMeasure() {
		return beatsPerMeasure;
	}

	@Override
	public String toString() {
		return displayName;
	}
}
