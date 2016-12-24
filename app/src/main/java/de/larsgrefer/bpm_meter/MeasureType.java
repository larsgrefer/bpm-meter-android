package de.larsgrefer.bpm_meter;

import lombok.Getter;

@Getter
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

	@Override
	public String toString() {
		return displayName;
	}
}
