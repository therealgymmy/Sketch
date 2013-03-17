package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class LineObjectCollection {

    private final ArrayList<LineObject> objects_
        = new ArrayList<LineObject>();

    public ArrayList<LineObject> getObjects () {
        return objects_;
    }

    // --- Add/Remove --- //

    // => add a new LineComponent
    // => set up start/end index
    public void add (LineComponent line) {
        LineObject object = new LineObject(line);

        object.initTimeLine();

        objects_.add(object);
    }

    // => erase any line object that intersects with the eraser
    // => mark the end index for those objects
    // => may remove the object if erase() returns true
    public void erase (Line2D eraser) {
        ArrayList<LineObject> toRemove = new ArrayList<LineObject>();

        for (LineObject object : objects_) {
            if (object.isVisible() &&
                object.intersects(eraser)) {
                if (object.erase()) {
                    toRemove.add(object);
                }
            }
        }

        for (LineObject r : toRemove) {
            objects_.remove(r);
        }
    }


    // --- Selection/Move --- //

    // => select objects that are enclosed
    //    note the objects must be visible to be selected
    public void encloseSelection (Polygon poly) {
        for (LineObject lineObject : objects_) {
            if (lineObject.isVisible() &&
                lineObject.intersects(poly)) {
                lineObject.select();
            }
        }
    }

    // => unselect every object
    public void clearSelection () {
        for (LineObject lineObject : objects_) {
            lineObject.unselect();
        }
    }

    // => move selected objects
    public void moveSelection (Point2D start, Point2D end) {
        for (LineObject lineObject : objects_) {
            if (lineObject.isSelected()) {
                lineObject.move(start, end);
            }
        }
    }

    // --- TimeLine Operations --- //

    // => insert a copy of current frames before next frame
    public void insertCurrentFrame () {
        for (LineObject lineObject : objects_) {
            lineObject.insertCurrentFrame();
        }

        // increment the frame length
        TimeLine.incFrameLength();
    }

    // => populate frame until the end with the current transform
    public void populateNewTimeLine () {
        for (LineObject lineObject : objects_) {
            lineObject.populateNewTimeLine();
        }
    }

    // => load frames for those unselected objects
    public void loadCurrentFrameForUnselected () {
        for (LineObject lineObject : objects_) {
            if (lineObject.isVisible() &&
                !lineObject.isSelected()) {
                lineObject.loadCurrentFrame();
            }
        }
    }

    // -> load frames at the specified index
    public void loadFrame (int index) {
        for (LineObject lineObject : objects_) {
            if (lineObject.isVisible()) {
                lineObject.loadFrame(index);
            }
        }
    }

}
