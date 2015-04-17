package de.larsgrefer.bpm_meter.base;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class BpmMeter {
	private TapType tapType;
	private MeasureType measureType;
	private LinkedList<Long> taps;

	private double beatsPerMinute;
	private double beatDuration;
	private double measuresPerMinute;
	private double measureDuration;

	public BpmMeter() {
		measureType = MeasureType.MT4_4;
		tapType = TapType.MEASURES;

		taps = new LinkedList<>();
	}

	public void tap() {
		taps.add(System.currentTimeMillis());

		update();
	}

	private List<Double> getDeltaList() {
		if (taps.isEmpty())
			return null;
		else {
			LinkedList<Double> deltaList = new LinkedList<>();

			long lastTap = taps.getFirst();
			ListIterator<Long> iterator = taps.listIterator(1);

			while (iterator.hasNext()) {
				long tap = iterator.next();
				deltaList.add((tap - lastTap) / 1000d);
				lastTap = tap;
			}

			return deltaList;
		}
	}

	private double getAverageDelta() {
		List<Double> deltaList = getDeltaList();
		if (deltaList == null)
			return 0;
		else if (deltaList.isEmpty())
			return Double.POSITIVE_INFINITY;
		else {
			double sum = 0;
			int count = 0;
			for (double delta : deltaList) {
				sum += delta;
				count++;
			}
			return sum / (double) count;
		}
	}

	public void reset() {
		taps.clear();
		update();
	}

	void update() {

		double delta = getAverageDelta();

		switch (tapType) {
			case BEATS:
				setBeatDuration(delta);
				break;
			case MEASURES:
				setMeasureDuration(delta);
				break;
		}

	}

	public TapType getTapType() {
		return tapType;
	}

	public void setTapType(TapType tapType) {
		this.tapType = tapType;
	}

	public MeasureType getMeasureType() {
		return measureType;
	}

	public void setMeasureType(MeasureType measureType) {
		this.measureType = measureType;
	}

	public double getBeatsPerMinute() {
		return beatsPerMinute;
	}

	public void setBeatsPerMinute(double bpm) {
		beatsPerMinute = bpm;
		beatDuration = BPM.durationFromXpm(bpm);
		measuresPerMinute = bpm / measureType.getBeatsPerMeasure();
		measureDuration = getBeatDuration() * measureType.getBeatsPerMeasure();
	}

	public double getBeatDuration() {
		return beatDuration;
	}

	public void setBeatDuration(double duration) {
		beatDuration = duration;
		beatsPerMinute = BPM.xpmFromDuration(duration);
		measureDuration = duration * measureType.getBeatsPerMeasure();
		measuresPerMinute = getBeatsPerMinute() / measureType.getBeatsPerMeasure();
	}

	public double getMeasuresPerMinute() {
		return measuresPerMinute;
	}

	public void setMeasuresPerMinute(double mpm) {
		measuresPerMinute = mpm;
		measureDuration = BPM.durationFromXpm(mpm);
		beatDuration = getMeasureDuration() / measureType.getBeatsPerMeasure();
		beatsPerMinute = mpm * measureType.getBeatsPerMeasure();
	}

	public double getMeasureDuration() {
		return measureDuration;
	}

	public void setMeasureDuration(double duration) {
		measureDuration = duration;
		measuresPerMinute = BPM.xpmFromDuration(duration);
		beatDuration = duration / measureType.getBeatsPerMeasure();
		beatsPerMinute = getMeasuresPerMinute() * measureType.getBeatsPerMeasure();
	}
}
