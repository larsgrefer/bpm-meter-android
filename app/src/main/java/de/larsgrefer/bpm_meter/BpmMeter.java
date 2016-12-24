package de.larsgrefer.bpm_meter;

import android.support.annotation.Keep;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BpmMeter {
    private TapType tapType;
    private MeasureType measureType;
    private final LinkedList<Long> taps = new LinkedList<>();

    private double beatsPerMinute;
    private double beatDuration;
    private double measuresPerMinute;
    private double measureDuration;

    @Keep
    public BpmMeter() {
        measureType = MeasureType.MT4_4;
        tapType = TapType.MEASURES;
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

    private void update() {

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

    public void setBeatsPerMinute(double bpm) {
        beatsPerMinute = bpm;
        beatDuration = BPM.durationFromXpm(bpm);
        measuresPerMinute = bpm / measureType.getBeatsPerMeasure();
        measureDuration = getBeatDuration() * measureType.getBeatsPerMeasure();
    }

    public void setBeatDuration(double duration) {
        beatDuration = duration;
        beatsPerMinute = BPM.xpmFromDuration(duration);
        measureDuration = duration * measureType.getBeatsPerMeasure();
        measuresPerMinute = getBeatsPerMinute() / measureType.getBeatsPerMeasure();
    }

    public void setMeasuresPerMinute(double mpm) {
        measuresPerMinute = mpm;
        measureDuration = BPM.durationFromXpm(mpm);
        beatDuration = getMeasureDuration() / measureType.getBeatsPerMeasure();
        beatsPerMinute = mpm * measureType.getBeatsPerMeasure();
    }

    public void setMeasureDuration(double duration) {
        measureDuration = duration;
        measuresPerMinute = BPM.xpmFromDuration(duration);
        beatDuration = duration / measureType.getBeatsPerMeasure();
        beatsPerMinute = getMeasuresPerMinute() * measureType.getBeatsPerMeasure();
    }
}
