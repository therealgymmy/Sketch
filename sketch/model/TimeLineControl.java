package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;

public class TimeLineControl {

    private Model      model_;
    private Controller controller_;


    private javax.swing.Timer recordTimer_;
    private ActionListener    recordAction_;
    private int               recordInterval_ = 33;

    private javax.swing.Timer playTimer_;
    private ActionListener    playAction_;
    private int               playInterval_ = 33;

    public TimeLineControl (Model model, Controller controller) {
        model_      = model;
        controller_ = controller;

        initRecordAction();
        resetRecordTimer();

        initPlayAction();
        resetPlayTimer();
    }

    // --- Record Timer --- //

    // => initialize recording action
    private void initRecordAction () {
        recordAction_ = new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                Log.debug("Recording action...", 1);

                recordFrame();

                controller_.updateViewSetting();
            }
        };
    }

    // => create a new record timer
    public void resetRecordTimer () {
        if (recordTimer_ != null) {
            recordTimer_.stop();
        }
        recordTimer_ = new javax.swing.Timer(recordInterval_,
                                             recordAction_);
    }

    // => start record acton
    public void startRecord () {
        Log.debug("Record started!", 2);

        recordTimer_.start();
    }

    // => stop record acton
    public void stopRecord () {
        Log.debug("Record stopped!", 2);

        recordTimer_.stop();
    }

    // --- Recording Operations --- //

    // => populate all the new frames with existing transforms
    // OR
    // => load the next frames for the unselected objects
    //    (selected objects will carry over their current transforms)
    public void recordFrame () {
        if (TimeLine.isLatest()) {
            Log.debug("Adding new frames...", 2);
            TimeLine.incFrameLength();
            model_.populateNewTimeLine();
            TimeLine.incFrameIndex();
        }
        else {
            Log.debug("Overwriting frames...", 2);
            TimeLine.incFrameIndex();
            model_.loadCurrentFrameForUnselected();
        }

    }

    // --- Play Timer --- //

    // => initialize playback action
    private void initPlayAction () {
        playAction_ = new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                Log.debug("Playing action...", 1);

                playback();

                controller_.updateViewSetting();
                controller_.updateView();
            }
        };
    }

    // => create a new play timer
    public void resetPlayTimer () {
        if (playTimer_ != null) {
            playTimer_.stop();
        }
        playTimer_ = new javax.swing.Timer(playInterval_,
                                           playAction_);
    }

    // => start play acton
    public void startPlay () {
        Log.debug("Playback started!", 2);

        playTimer_.start();
    }

    // => stop play acton
    public void stopPlay () {
        Log.debug("Playback stopped!", 2);

        playTimer_.stop();
    }

    // --- Playback Operations --- //

    // => play one frame
    // => increment frame index
    // => stop playTimer if at the end
    public void playback () {
        int frameIndex  = TimeLine.getFrameIndex();
        int frameLength = TimeLine.getFrameLength();

        if (frameIndex < frameLength) {
            TimeLine.incFrameIndex();
        }
        else {
            stopPlay();
            TimeLine.setFrameIndex(0);
        }
    }

    public boolean isPlaying () {
        return playTimer_.isRunning();
    }

}
