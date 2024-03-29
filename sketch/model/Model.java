package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;

public class Model extends    Object
                   implements IModel {

    private Controller controller_;

    private TimeLine                  timeline_;
    private LineComponent             curLineObject_;
    private LinkedList<LineComponent> lineObjects_;
    private Polygon                   selection_;
    private LinkedList<LineComponent> selectedObjects_;

    public Model (Controller controller) {
        super();

        // Set up connection with controller
        controller_ = controller;
        controller_.setModel(this);

        // initialize members
        timeline_        = new TimeLine(this, controller_);
        lineObjects_     = new LinkedList<LineComponent>();
        curLineObject_   = new LineComponent();
        selection_       = new Polygon();
        selectedObjects_ = new LinkedList<LineComponent>();
    }

    public void startAnimation () {
        timeline_.startRecord();
    }

    public void stopAnimation () {
        timeline_.stopRecord();
    }

    public void enablePlayback () {
        timeline_.startPlay();
    }

    public void pausePlayback () {
        timeline_.stopPlay();
    }

    public void setLineObjects (LinkedList<LineComponent> objects) {
        lineObjects_ = objects;
    }

    public int getFrameLength () {
        return timeline_.getFrameLength();
    }

    public int getPlayFrameIndex () {
        return timeline_.getPlayFrameIndex();
    }

    public boolean isPlaying () {
        return timeline_.isPlaying();
    }

    // Get the selection path
    @Override
    public Polygon getSelection () {
        return selection_;
    }

    // Get all the line objects
    @Override
    public LinkedList<LineComponent> getLineObjects () {
        return lineObjects_;
    }

    // Add a new line
    @Override
    public void addLine (Point2D start, Point2D end) {
        curLineObject_.addLine(start, end);
    }

    // Erase a line that the point it draws touches
    @Override
    public void eraseLine (Point2D point) {
        LinkedList<LineComponent> toRemove
            = new LinkedList<LineComponent>();

        // Record the line objects that intersects the point
        for (LineComponent lineObject : lineObjects_) {
            if (lineObject.intersects(point)) {
                toRemove.add(lineObject);
            }
        }

        // Delete all the line objects that match
        for (LineComponent r : toRemove) {
            Log.debug("Removed a line object", 2);

            // Remove this object from this point in time and onwards
            timeline_.removePartial(r);
        }
    }

    // Erase a line that the line it draws crosses
    @Override
    public void eraseLine (Point2D start, Point2D end) {
        LinkedList<LineComponent> toRemove
            = new LinkedList<LineComponent>();

        // Record the line objects that intersects the point
        for (LineComponent lineObject : lineObjects_) {
            if (lineObject.intersects(start, end)) {
                toRemove.add(lineObject);
            }
        }

        // Delete all the line objects that match
        for (LineComponent r : toRemove) {
            Log.debug("Removed a line object", 2);

            // Remove this object from this point in time and onwards
            timeline_.removePartial(r);
        }
    }

    // Add a new lineObject to the linked list
    @Override
    public void addNewObject () {
        Log.debug("Created a new line object", 2);

        lineObjects_.add(curLineObject_);
    }

    // Create a new lineObject
    @Override
    public void finalizeNewObject () {
        Log.debug("Finalized a new line object", 2);

        // Add this object from this point in time and onwards
        timeline_.addPartial(curLineObject_);

        curLineObject_ = new LineComponent();
    }

    // Clear all previously selected line objects
    @Override
    public void addNewSelection () {
        Log.debug("Started a new selection", 2);

        selectedObjects_ = new LinkedList<LineComponent>();

        for (LineComponent lineObject : lineObjects_) {
            lineObject.setIsSelected(false);
        }
    }

    // Enclose a selection on line objects
    // and create a new selection for next time
    @Override
    public void encloseSelection () {
        Log.debug("Closed the selection", 2);

        for (LineComponent lineObject : lineObjects_) {
            if (lineObject.intersects(selection_)) {
                lineObject.setIsSelected(true);
                selectedObjects_.add(lineObject);
            }
        }

        selection_ = new Polygon();
    }

    // Compare previous selected objects to current objects
    public void updateSelection () {
        ListIterator<LineComponent> objItr
            = lineObjects_.listIterator(0);

        while (objItr.hasNext()) {

            ListIterator<LineComponent> objItr_s
                = selectedObjects_.listIterator(0);

            LineComponent obj   = objItr.next();
            while (objItr_s.hasNext()) {
                LineComponent obj_s = objItr_s.next();
                if (obj.equal(obj_s)) {
                    obj.setIsSelected(true);
                }
            }
        }
    }

    // Extend a selection
    @Override
    public void extendSelection (Point2D start, Point2D end) {
        selection_.addPoint((int)end.getX(), (int)end.getY());
    }

    // Move selected objects around
    @Override
    public void move (Point2D start, Point2D end) {
        for (LineComponent lineObject : lineObjects_) {
            if (lineObject.isSelected()) {
                lineObject.move(start, end);
            }
        }
    }

    // Set to a particular frame
    @Override
    public void updateFrameIndex (int index) {
        timeline_.setPlayFrameIndex(index);
        timeline_.loadFrame(index);
        updateSelection();
        controller_.updateView();
    }

    // Enable the recordTimer to default frame policy
    public void enableFrameDefault () {
        timeline_.resetRecordTimer();
    }

    // Enable the recordTimer to insert new frames
    public void enableFrameInsertion () {
        timeline_.resetInsertTimer();
    }

}
