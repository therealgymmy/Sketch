package sketch.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;
import sketch.model.*;

public class View extends    JPanel
                  implements IView {

    private Controller controller_;

    private MouseController mController_;
    private Toolbar         toolbar_;
    private Slider          slider_;

    public View (Controller controller) {
        super();

        // Set up connection with controller
        controller_ = controller;
        controller_.setView(this);

        // Set up view and event controls
        layoutView();
        registerControllers();

        // Set focus
        requestFocusInWindow();
    }

    // Initialize view layout and view components
    private void layoutView () {
        setSize(800, 600);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Initialize components
        toolbar_ = new Toolbar(this, controller_);
        slider_  = new Slider(this, controller_);

        // Add components to view
        add(toolbar_, BorderLayout.PAGE_START);
        add(slider_,  BorderLayout.PAGE_END);
    }

    // Register event controllers for mouse clicks and motion
    private void registerControllers () {
        mController_ = new MouseController(this, controller_);
        addMouseListener(mController_);
        addMouseMotionListener(mController_);
    }

    // Ask the system to repaint
    @Override
    public void updateView () {
        repaint();
    }

    // Paint the entire view
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        // Draw all lines
        LineObjectCollection lineObjects
            = controller_.getLineObjects();

        Paint.paint(g2d, this, lineObjects);

        // Draw the selection path
        Polygon selectionPath = controller_.getSelection();
        Paint.paintSelection(g2d, selectionPath);

        // Draw ancor point if in rotate mode
        if (mController_.getState() == MouseController.State.ROTATE) {
            Paint.paintAncor(g2d, mController_.getAncor());
        }
    }

    // --- State --- //

    // => return the state the view is in
    public MouseController.State getState () {
        return mController_.getState();
    }

    // => return if recording
    public boolean isRecording () {
        return mController_.isRecording();
    }

    // Enable draw mode
    @Override
    public void enableDraw () {
        mController_.setState(MouseController.State.DRAW);
        mController_.updateCursor();
    }

    // Enable erase mode
    @Override
    public void enableErase () {
        mController_.setState(MouseController.State.ERASE);
        mController_.updateCursor();
    }

    // Enable selection mode
    @Override
    public void enableSelection () {
        mController_.setState(MouseController.State.SELECTION);
        mController_.updateCursor();
    }

    // Enable drag mode
    public void enableDrag () {
        mController_.setState(MouseController.State.DRAG);
        mController_.updateCursor();
    }

    // Enable rotate mode
    public void enableRotate () {
        mController_.setState(MouseController.State.ROTATE);
        mController_.updateCursor();
    }

    // Enable drag record mode
    public void enableRecordDrag () {
        mController_.setState(MouseController.State.RECORD_DRAG);
        mController_.updateCursor();
    }

    // Enable rotate record mode
    public void enableRecordRotate () {
        mController_.setState(MouseController.State.RECORD_ROTATE);
        mController_.updateCursor();
    }

    // => enable playback
    public void enablePlayback () {
        mController_.setState(MouseController.State.PLAYBACK);
        mController_.updateCursor();
    }

    // --- Slider --- //

    // => set the length of the slider,
    //    which reflects the length of the frames
    public void setSliderLength (int length) {
        slider_.setSliderLength(length);
    }

    // => set the position of the knob on the slider
    public void setSliderPointerPosition (int pos) {
        slider_.setPointerPosition(pos);
    }

    // => turn on play button
    public void enablePlayButton () {
        slider_.enablePlayButton();
    }

    // => turn on pause button
    public void enablePauseButton () {
        slider_.enablePauseButton();
    }

}
