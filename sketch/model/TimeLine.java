package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;

// Keeps track of history
public class TimeLine {

    private Model      model_;
    private Controller controller_;

    private javax.swing.Timer recordTimer_;
    private ActionListener    recordAction_;
    private int               recordInterval_ = 33;

    private javax.swing.Timer playTimer_;
    private ActionListener    playAction_;
    private int               playInterval_   = 33;
    private int               playFrameIndex_ = 0;

    private class Frame {
        private LinkedList<LineComponent> objects_
            = new LinkedList<LineComponent>();

        Frame copy () {
            Frame c = new Frame();

            for (LineComponent lineObj: objects_) {
                LineComponent obj = lineObj.copy();
                c.objects_.add(obj);
            }

            return c;
        }
    }

    private ArrayList<Frame> frames_ = new ArrayList<Frame>();

    public TimeLine (Model model, Controller controller) {
        model_      = model;
        controller_ = controller;

        setupRecordAction();
        resetRecordTimer();

        setupPlayAction();
        resetPlayTimer();
    }

    // Remove this object from this point in time and onwards
    public void removePartial (LineComponent object) {
        ListIterator<Frame> frameItr
            = frames_.listIterator(playFrameIndex_);

        while (frameItr.hasNext()) {
            Frame frame = frameItr.next();

            LinkedList<LineComponent> toRemove
                = new LinkedList<LineComponent>();

            for (LineComponent obj : frame.objects_) {
                if (obj.equal(object)) {
                    toRemove.add(obj);
                }
            }

            // Delete all the line objects that match
            for (LineComponent r : toRemove) {
                frame.objects_.remove(r);
            }
        }
    }

    // Set up the record action
    private void setupRecordAction () {
        recordAction_ = new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                Log.debug("Recording action...", 1);

                // Save one frame of the actions of the objects
                saveFrame();

                controller_.updateViewSetting();
            }
        };
    }

    // Set up the play action
    private void setupPlayAction () {
        playAction_ = new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                Log.debug("Playing records...", 1);

                // Load up next frame and update the view
                if (playFrameIndex_ < frames_.size()) {
                    loadFrame(playFrameIndex_);
                    ++playFrameIndex_;
                    controller_.updateViewSetting();
                    controller_.updateView();
                }
                else {
                    stopPlay();
                    playFrameIndex_ = 0;
                    controller_.updateViewSetting();
                    controller_.updateView();
                }
            }
        };
    }

    // Find out if the playTimer is running
    public boolean isPlaying () {
        return playTimer_.isRunning();
    }

    // Get current frame index
    public int getPlayFrameIndex () {
        return playFrameIndex_;
    }

    // Get number of frames
    public int getFrameLength () {
        return frames_.size();
    }

    // Set playFrameIndex
    public void setPlayFrameIndex (int index) {
        playFrameIndex_ = index;
    }

    // Save one frame of actions of the objects
    public void saveFrame () {
        Frame frame = new Frame();
        LinkedList<LineComponent> objects = model_.getLineObjects();
        for (LineComponent lineObj : objects) {
            LineComponent newLineObj = lineObj.copy();
            frame.objects_.add(newLineObj);
        }

        ListIterator frameItr = frames_.listIterator(playFrameIndex_);
        if (frameItr.hasNext()) {
            frameItr.add(frame);
        }
        else {
            frames_.add(frame);
        }

        ++playFrameIndex_;
    }

    // Load one fram of actions of the objects
    public void loadFrame (int index) {
        Frame frame = frames_.get(index);
        model_.setLineObjects(frame.objects_);
    }

    // Reset the timer according to record interval and action
    public void resetRecordTimer () {
        recordTimer_ = new javax.swing.Timer(recordInterval_,
                                             recordAction_);
    }

    // Reset the timer according to play interval and action
    public void resetPlayTimer () {
        playTimer_ = new javax.swing.Timer(playInterval_,
                                           playAction_);
    }

    // Start record acton
    public void startRecord () {
        Log.debug("Record started!", 2);

        recordTimer_.start();
    }

    // Stop record acton
    public void stopRecord () {
        Log.debug("Record Completed!", 2);

        recordTimer_.stop();
    }

    // Start play acton
    public void startPlay () {
        Log.debug("Playback started!", 2);

        playTimer_.start();
    }

    // Stop play acton
    public void stopPlay () {
        Log.debug("Playback completed!", 2);

        playTimer_.stop();
    }

}
