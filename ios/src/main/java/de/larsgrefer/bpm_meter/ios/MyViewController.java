package de.larsgrefer.bpm_meter.ios;

import de.larsgrefer.bpm_meter.base.Format;
import de.larsgrefer.bpm_meter.base.MeasureType;
import de.larsgrefer.bpm_meter.base.TapType;
import org.robovm.apple.foundation.NSOperationQueue;
import org.robovm.apple.foundation.NSString;
import org.robovm.apple.foundation.NSTimer;
import org.robovm.apple.uikit.*;
import org.robovm.objc.Selector;
import org.robovm.objc.annotation.CustomClass;
import org.robovm.objc.annotation.IBAction;
import org.robovm.objc.annotation.IBOutlet;

import de.larsgrefer.bpm_meter.base.BpmMeter;

import java.util.Timer;
import java.util.TimerTask;

@CustomClass("MyViewController")
public class MyViewController extends UIViewController {
    private UITextField tpm;
    private UITextField tpmSec;
    private UITextField bpm;
    private UITextField bpmSec;

    private UILabel measures;
    private UILabel beats;
    private UILabel tpmLabel;
    private UILabel tpmSecLabel;
    private UILabel bpmLabel;
    private UILabel bpmSecLabel;

    private UISegmentedControl speed;
    private UISegmentedControl bpmTpmSelector;

    private UIButton tapButton;

    private BpmMeter bpmMeter;
    private long down;
    private Timer timer;


    private void init(){
        this.bpmMeter = new BpmMeter();
        this.bpmMeter.setMeasureType(MeasureType.MT4_4);
        this.bpmMeter.setTapType(TapType.BEATS);
        this.timer = new Timer();

        this.bpmTpmSelector.setTitle(NSString.getLocalizedString("beats"), 0);
        this.bpmTpmSelector.setTitle(NSString.getLocalizedString("measures"), 1);

        this.measures.setText(NSString.getLocalizedString("measures"));
        this.beats.setText(NSString.getLocalizedString("beats"));
        this.tpmLabel.setText(NSString.getLocalizedString("tpm"));
        this.tpmSecLabel.setText(NSString.getLocalizedString("sec"));
        this.bpmLabel.setText(NSString.getLocalizedString("bpm"));
        this.bpmSecLabel.setText(NSString.getLocalizedString("sec"));
        this.tapButton.setTitle(NSString.getLocalizedString("tap"), UIControlState.Normal);
    }

    @Override
    public void viewDidLoad(){
        super.viewDidLoad();
        init();
    }

    @IBOutlet
    public void setTpm(UITextField tpm) { this.tpm = tpm; }

    @IBOutlet
    public void setTpmSec(UITextField tpmSec) { this.tpmSec = tpmSec; }

    @IBOutlet
    public void setBpm(UITextField bpm) { this.bpm = bpm; }

    @IBOutlet
    public void setBpmSec(UITextField bpmSec) { this.bpmSec = bpmSec; }

    @IBOutlet
    public void setSpeed(UISegmentedControl speed) { this.speed = speed; }

    @IBOutlet
    public void setBpmTpmSelector(UISegmentedControl bpmTpmSelector) { this.bpmTpmSelector = bpmTpmSelector; }

    @IBOutlet
    public void setTapButton(UIButton tapButton) {this.tapButton = tapButton;}

    @IBOutlet
    public void setMeasures(UILabel measures) {
        this.measures = measures;
    }

    @IBOutlet
    public void setBeats(UILabel beats) {
        this.beats = beats;
    }

    @IBOutlet
    public void setTpmLabel(UILabel tpmLabel) {
        this.tpmLabel = tpmLabel;
    }

    @IBOutlet
    public void setTpmSecLabel(UILabel tpmSecLabel) {
        this.tpmSecLabel = tpmSecLabel;
    }

    @IBOutlet
    public void setBpmLabel(UILabel bpmLabel) {
        this.bpmLabel = bpmLabel;
    }

    @IBOutlet
    public void setBpmSecLabel(UILabel bpmSecLabel) {
        this.bpmSecLabel = bpmSecLabel;
    }

    @IBAction
    public void tapButtonDown() {
        down = System.currentTimeMillis();
        timer.schedule(new MyTimerTask(), 1000);
    }

    @IBAction
    public void tapButtonUp(){
        long diff = System.currentTimeMillis() - down;
        if(diff < 1000)
        {
            bpmMeter.tap();
            timer.cancel();
            timer = new Timer();
            updateUi();
        }
    }

    @IBAction
    public void bpmChanged() {
        bpmMeter.setBeatsPerMinute(Format.fromString(bpm.getText()));
        updateUi();
    }

    @IBAction
    public void tpmChanged() {
        bpmMeter.setMeasuresPerMinute(Format.fromString(tpm.getText()));
        updateUi();
    }

    @IBAction
    public void bpmSecChanged() {
        bpmMeter.setBeatDuration(Format.fromString(bpmSec.getText()));
        updateUi();
    }

    @IBAction
    public void tpmSecChanged() {
        bpmMeter.setMeasureDuration(Format.fromString(tpmSec.getText()));
        updateUi();
    }

    @IBAction
    public void speedChanged() {
        long selectedSegment = speed.getSelectedSegment();
        switch ((int)selectedSegment) {
            case 0:
                bpmMeter.setMeasureType(MeasureType.MT4_4);
                break;
            case 1:
                bpmMeter.setMeasureType(MeasureType.MT3_4);
                break;
            case 2:
                bpmMeter.setMeasureType(MeasureType.MT2_4);
                break;
        }
        updateUi();
    }

    @IBAction
    public void bpmTpmChanged() {
        long selectedSegment = bpmTpmSelector.getSelectedSegment();
        switch ((int)selectedSegment){
            case 0:
                bpmMeter.setTapType(TapType.BEATS);
                break;
            case 1:
                bpmMeter.setTapType(TapType.MEASURES);
        }
        updateUi();
    }

    private void updateUi()
    {
        bpm.setText(Format.perMinuteFormat(bpmMeter.getBeatsPerMinute()));
        bpmSec.setText(Format.durationFormat(bpmMeter.getBeatDuration()));
        tpm.setText(Format.perMinuteFormat(bpmMeter.getMeasuresPerMinute()));
        tpmSec.setText(Format.durationFormat(bpmMeter.getMeasureDuration()));
    }

    private class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            bpmMeter.reset();
            NSOperationQueue.getMainQueue().addOperation(new Runnable() {
                @Override
                public void run() {
                    updateUi();
                }
            });
        }
    }
}
