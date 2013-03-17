package sketch.controller;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.model.*;
import sketch.view.*;

public class Controller extends Object
                        implements IView,
                                   IModel {

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
        }
    }

    // Pause playback
    public void pauseAnimation () {
        if (model_.isPlaying()) {
            model_.disablePlayback();
            view_.enablePlayButton();
        }
    }

    // --- View Related Functions --- //

    // Refresh
    @Override
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
        }
        else {
            view_.enablePlayButton();
        }
    }

    // Enable drawing
    @Override
    public void enableDraw () {
        view_.enableDraw();
    }

    // Enable erasing
    @Override
    public void enableErase () {
        view_.enableErase();
    }

    // Enable selection
    @Override
    public void enableSelection () {
        view_.enableSelection();
    }

    // --- Model Related Functions --- //

    // Get the selection path
    @Override
    public Polygon getSelection () {
        return model_.getSelection();
    }

    // Get all the line objects
    public LineObjectCollection getLineObjects () {
        return model_.getLineObjects();
    }

    // Add a new line
    @Override
    public void addLine (Point2D start, Point2D end) {
        model_.addLine(start, end);
    }

    // Erase a line that the point it draws touches
    @Override
    public void eraseLine (Point2D point) {
        model_.eraseLine(point);
    }

    // Erase a line that the line it draws crosses
    @Override
    public void eraseLine (Point2D start, Point2D end) {
        model_.eraseLine(start, end);
    }

    // Add a new lineObject to the linked list
    @Override
    public void addNewObject () {
        model_.addNewObject();
    }

    // Create a new lineObject
    @Override
    public void finalizeNewObject () {
        model_.finalizeNewObject();
    }

    // Create a new selection
    @Override
    public void addNewSelection () {
        model_.addNewSelection();
    }

    // Enclose a selection on line objects
    @Override
    public void encloseSelection () {
        model_.encloseSelection();
    }

    // Extend a selection
    @Override
    public void extendSelection (Point2D start, Point2D end) {
        model_.extendSelection(start, end);
    }

    // Move selected objects around
    @Override
    public void move (Point2D start, Point2D end) {
        model_.move(start, end);
    }

    // Rotate selected objects around
    public void rotate (Point2D start, Point2D end, Point2D ancor) {
        model_.rotate(start, end, ancor);
    }

    public void loadFrame (int index) {
        model_.loadFrame(index);
    }

    public void insertCurrentFrame () {
        model_.insertCurrentFrame();
    }

}
