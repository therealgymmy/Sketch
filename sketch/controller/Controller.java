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

    // --- Both View and Controller Related Functions ---/

    // Enable animate recording
    public void enableAnimation () {
        view_.enableAnimation();
        model_.startAnimation();
    }

    // Disable animate recording
    public void disableAnimation () {
        view_.enableSelection();
        model_.stopAnimation();
    }

    // Playback previous recorded animation
    public void playAnimation () {
        if (!model_.isPlaying()) {
            model_.enablePlayback();
            view_.enablePlayback();
        }
    }

    // Pause Playback
    public void pauseAnimation () {
        if (model_.isPlaying()) {
            model_.pausePlayback();
            view_.enablePlayButton();
        }
    }

    // --- View Related Functions --- //
    // Refresh
    @Override
    public void updateView () {
        if (model_.getFrameLength() > 0) {
            view_.updateView();
        }
    }

    // Update view settings
    public void updateViewSetting () {
        if (model_.getFrameLength() > 0) {
            // Update slider length
            int size = model_.getFrameLength();
            view_.setSliderLength(size - 1);

            // Update slider pointer position
            int pos = model_.getPlayFrameIndex();
            view_.setSliderPointerPosition(pos);

            // Update play button status
            if (model_.isPlaying()) {
                view_.enablePauseButton();
            }
            else {
                view_.enablePlayButton();
            }
        }
        else {
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

    // Enable playback
    @Override
    public void enablePlayback () {
        view_.enablePlayback();
    }

    // --- Model Related Functions --- //
    // Get the selection path
    @Override
    public Polygon getSelection () {
        return model_.getSelection();
    }

    // Get all the line objects
    @Override
    public LinkedList<LineComponent> getLineObjects () {
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

    @Override
    public void updateFrameIndex (int index) {
        model_.updateFrameIndex(index);
    }

}
