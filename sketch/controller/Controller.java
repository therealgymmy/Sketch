package sketch.controller;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.model.*;
import sketch.view.*;

public class Controller extends Object {

    private View  view_;
    private Model model_;

    public Controller () {
        super();
    }

    public void setView (View view) {
        view_ = view;
    }

    public void setModel (Model model) {
        model_ = model;
    }

    // --- View & Model Related Functions --- //

    // Clear everything so you can start from scratch
    public void clearAll () {
        model_.clearAll();
        updateView();
        updateViewSetting();
    }

    // Enable animation drag recording
    public void enableRecordDrag () {
        view_.enableRecordDrag();
        model_.enableRecord();
    }

    // Enable animation rotate recording
    public void enableRecordRotate () {
        view_.enableRecordRotate();
        model_.enableRecord();
    }

    // Enable animation smart motion recording
    public void enableRecordSmartMotion () {
        view_.enableRecordSmartMotion();
        model_.enableRecord();
    }

    // Disable animation recording
    // And enable selection mode for view
    public void disableRecord () {
        view_.enableSelection();
        model_.disableRecord();
    }

    // Play previous recorded animation
    public void playAnimation () {
        if (!model_.isPlaying()) {
            model_.enablePlayback();
            view_.enablePlayback();
            updateViewSetting();
        }
    }

    // Pause playback
    public void pauseAnimation () {
        if (model_.isPlaying()) {
            model_.disablePlayback();
            view_.enablePlayButton();
            updateViewSetting();
        }
    }

    // --- View Related Functions --- //

    // Refresh
    public void updateView () {
        view_.updateView();
    }

    // Update view settings
    public void updateViewSetting () {
        // Update slider length
        int length = TimeLine.getFrameLength();
        view_.setSliderLength(length - 1);

        // Update slider knot position
        int pos = TimeLine.getFrameIndex();
        view_.setSliderPointerPosition(pos);

        // Update playback button setting
        if (model_.isPlaying()) {
            view_.enablePauseButton();
            view_.disableInsertButton();
        }
        else {
            view_.enablePlayButton();
            view_.enableInsertButton();
        }
    }

    // Enable drawing
    public void enableDraw () {
        view_.enableDraw();
    }

    // Enable erasing
    public void enableErase () {
        view_.enableErase();
    }

    // Enable selection
    public void enableSelection () {
        view_.enableSelection();
    }

    // --- Model Related Functions --- //

    // Change color
    public void changeColor (Color color) {
        model_.changeColor(color);
    }

    // Get the selection path
    public Polygon getSelection () {
        return model_.getSelection();
    }

    // Get all the line objects
    public LineObjectCollection getLineObjects () {
        return model_.getLineObjects();
    }

    // Add a new line
    public void addLine (Point2D start, Point2D end) {
        model_.addLine(start, end);
    }

    // Erase a line that the point it draws touches
    public void eraseLine (Point2D point) {
        model_.eraseLine(point);
    }

    // Erase a line that the line it draws crosses
    public void eraseLine (Point2D start, Point2D end) {
        model_.eraseLine(start, end);
    }

    // Add a new lineObject to the linked list
    public void addNewObject () {
        model_.addNewObject();
    }

    // Create a new lineObject
    public void finalizeNewObject () {
        model_.finalizeNewObject();
    }

    // Create a new selection
    public void addNewSelection () {
        model_.addNewSelection();
    }

    // Enclose a selection on line objects
    public void encloseSelection () {
        model_.encloseSelection();
    }

    // Extend a selection
    public void extendSelection (Point2D start, Point2D end) {
        model_.extendSelection(start, end);
    }

    // Move selected objects around
    public void move (Point2D start, Point2D end) {
        model_.move(start, end);
    }

    // Rotate selected objects around
    public void rotate (Point2D start, Point2D end, Point2D ancor) {
        model_.rotate(start, end, ancor);
    }

    // Move and rotate at the same time
    public void smart_motion (Point2D start,
                              Point2D end,
                              Point2D ancor) {
        model_.smart_motion(start, end, ancor);
    }

    public void loadFrame (int index) {
        model_.loadFrame(index);
    }

    public void insertCurrentFrame () {
        model_.insertCurrentFrame();
    }

}
