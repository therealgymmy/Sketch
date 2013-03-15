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

    // Set up the record action
    private void setupRecordAction () {
        recordAction_ = new ActionListener () {
            @Override
            public void actionPerformed (ActionEvent e) {
                Log.debug("Recording action...", 1);

                // Save one frame of the actions of the objects
                saveFrame();
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
                    Frame frame = frames_.get(playFrameIndex_);
                    ++playFrameIndex_;
                    loadFrame(frame);
                    controller_.updateView();
                }
                else {
                    stopPlay();
                }
            }
        };
    }

    // Save one frame of actions of the objects
    public void saveFrame () {
        Frame frame = new Frame();
        LinkedList<LineComponent> objects = model_.getLineObjects();
        for (LineComponent lineObj : objects) {
            LineComponent newLineObj = lineObj.copy();
            frame.objects_.add(newLineObj);
        }
        frames_.add(frame);
    }

    // Load one fram of actions of the objects
    public void loadFrame (Frame frame) {
        Frame f = frame.copy();
        model_.setLineObjects(f.objects_);
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

        playFrameIndex_ = 0;
        playTimer_.stop();
    }

}
