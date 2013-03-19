package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;

public class Model extends Object {

    private Controller controller_;

    private LineComponent curLineObject_;
    private Polygon       selection_;

    private final LineObjectCollection objects_
        = new LineObjectCollection();

    private final TimeLineControl timeControl_;

    public Model (Controller controller) {
        super();

        // Set up connection with controller
        controller_ = controller;
        controller_.setModel(this);

        // initialize members
        curLineObject_ = new LineComponent();
        selection_     = new Polygon();
        timeControl_   = new TimeLineControl(this, controller);
    }

    // Clear everything
    public void clearAll () {
        curLineObject_.clear();
        objects_.clear();
        timeControl_.resetRecordTimer();
        timeControl_.resetPlayTimer();
        TimeLine.setFrameIndex(0);
        TimeLine.setFrameLength(1);
    }

    // Change color
    public void changeColor (Color color) {
        objects_.changeSelectionColor(color);
        LineObject.DEFAULT_COLOR = color;
    }

    // Get the selection path
    public Polygon getSelection () {
        return selection_;
    }

    // Get all the line objects
    public LineObjectCollection getLineObjects () {
        return objects_;
    }

    // Erase a line that the point it draws touches
    public void eraseLine (Point2D point) {
        // Do nothing
    }

    // Erase a line that the line it draws crosses
    public void eraseLine (Point2D start, Point2D end) {
        Line2D eraser = new Line2D.Double(start, end);

        objects_.erase(eraser);

        controller_.updateView();
    }

    // Add a new lineObject to the linked list
    public void addNewObject () {
        Log.debug("Created a new line object", 2);

        objects_.add(curLineObject_);

        controller_.updateView();
    }

    // Add a new line
    public void addLine (Point2D start, Point2D end) {
        curLineObject_.addLine(start, end);

        controller_.updateView();
    }

    // Create a new lineObject
    public void finalizeNewObject () {
        Log.debug("Finalized a new line object", 2);

        curLineObject_ = new LineComponent();

        controller_.updateView();
    }

    // Clear all previously selected line objects
    public void addNewSelection () {
        Log.debug("Started a new selection", 2);

        objects_.clearSelection();

        controller_.updateView();
    }

    // Enclose a selection on line objects
    // and create a new selection for next time
    public void encloseSelection () {
        Log.debug("Closed the selection", 2);

        objects_.encloseSelection(selection_);

        selection_ = new Polygon();

        controller_.updateView();
    }

    // Extend a selection
    public void extendSelection (Point2D start, Point2D end) {
        selection_.addPoint((int)end.getX(), (int)end.getY());

        controller_.updateView();
    }

    // Move selected objects around
    public void move (Point2D start, Point2D end) {
        objects_.moveSelection(start, end);
        
        controller_.updateView();
    }

    // Rotate selected objects around
    public void rotate (Point2D start, Point2D end, Point2D ancor) {
        objects_.rotateSelection(start, end, ancor);

        controller_.updateView();
    }

    // Move and rotate at the same time
    public void smart_motion (Point2D start,
                              Point2D end,
                              Point2D ancor) {
        objects_.smartMoveSelection(start, end, ancor);

        controller_.updateView();
    }

    // --- TimeLine Operations --- //

    // => insert a copy of current frames before next frame
    public void insertCurrentFrame () {
        objects_.insertCurrentFrame();

        controller_.updateViewSetting();
        controller_.updateView();
    }

    // => populate frame until the end with the current transform
    public void populateNewTimeLine () {
        objects_.populateNewTimeLine();

        controller_.updateView();
    }

    // => load frames for those unselected objects
    public void loadCurrentFrameForUnselected () {
        objects_.loadCurrentFrameForUnselected();

        controller_.updateView();
    }

    // -> load frames at the specified index
    // -> update current frame index
    public void loadFrame (int index) {
        TimeLine.setFrameIndex(index);
        objects_.loadFrame(index);
        controller_.updateView();
    }

    // --- TimeLineControl Operations --- //

    // => turn on the timeline recorder
    public void enableRecord () {
        timeControl_.startRecord();
    }

    // => turn off the timeline recorder
    public void disableRecord () {
        timeControl_.stopRecord();
    }

    // => find out if playing
    public boolean isPlaying () {
        return timeControl_.isPlaying();
    }

    // => enable playback
    public void enablePlayback () {
        timeControl_.startPlay();
    }

    // => disable playback
    public void disablePlayback () {
        timeControl_.stopPlay();
    }

}
