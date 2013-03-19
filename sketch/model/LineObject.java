package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;

public class LineObject {

    public static Color DEFAULT_COLOR = Color.BLACK;

    // --- Core Components --- //
    private final TimeLine      time_ = new TimeLine();
    private final LineComponent line_;

    private boolean         isSelected_ = false;
    private AffineTransform transform_  = new AffineTransform();
    private Color           color_      = DEFAULT_COLOR;

    public LineObject () {
        line_ = new LineComponent();
    }

    public LineObject (LineComponent line) {
        line_ = line;
    }

    public void changeColor (Color color) {
        color_ = color;
    }

    public Color getColor () {
        return color_;
    }

    // --- Selection Operations --- //

    public boolean isSelected () { return isSelected_; }
    public void    select     () { isSelected_ = true; }
    public void    unselect   () { isSelected_ = false; }

    // --- LineComponent Operations --- //

    public LineComponent getLineComponent () {
        return line_;
    }

    public boolean intersects (Line2D otherLine) {
        return line_.intersects(otherLine);
    }

    public boolean intersects (Polygon poly) {
        return line_.intersects(poly);
    }

    public boolean isContainedIn (Polygon poly) {
        return line_.isContainedIn(poly);
    }

    // --- Transform-related Operation --- //

    // => get current transform
    public AffineTransform getCurrentTransform () {
        return transform_;
    }

    // => translate by the different of two points
    public void move (Point2D start, Point2D end) {
        try {
            start = transform_.inverseTransform(start, null);
            end   = transform_.inverseTransform(end, null);

            Point2D diff
                = new Point2D.Double(end.getX() - start.getX(),
                                     end.getY() - start.getY());
            transform_.translate(diff.getX(), diff.getY());
        }
        catch (Exception e) {
        }

        saveCurrentFrame();

        // Populate from this frame to the end
        populateNewTimeLine();

        // Reset end index to 0
        time_.setEndIndex(0);
    }

    // => translate by the difference of two points
    public void rotate (Point2D start, Point2D end, Point2D ancor) {
        Point2D oldDiff
            = new Point2D.Double(start.getX() - ancor.getX(),
                                 start.getY() - ancor.getY());
        Point2D newDiff
            = new Point2D.Double(end.getX() - ancor.getX(),
                                 end.getY() - ancor.getY());

        try {
            ancor = transform_.inverseTransform(ancor, null);

            double theta
                = Math.atan2(newDiff.getX(), newDiff.getY()) -
                  Math.atan2(oldDiff.getX(), oldDiff.getY());

            transform_.rotate(theta, ancor.getX(), ancor.getY());
        }
        catch (Exception e) {
        }

        saveCurrentFrame();

        // Populate from this frame to the end
        populateNewTimeLine();

        // Reset end index to 0
        time_.setEndIndex(0);
    }

    // => move and rotate at the same time
    public void smartMove (Point2D start, Point2D end, Point2D ancor) {
        try {
            Point2D oldDiff
                = new Point2D.Double(start.getX() - ancor.getX(),
                                     start.getY() - ancor.getY());
            Point2D newDiff
                = new Point2D.Double(end.getX() - ancor.getX(),
                                     end.getY() - ancor.getY());

            // Translate
            start = transform_.inverseTransform(start, null);
            end   = transform_.inverseTransform(end, null);

            Point2D diff
                = new Point2D.Double(start.getX() - end.getX(),
                                     start.getY() - end.getY());

            // Rotate
            ancor = transform_.inverseTransform(ancor, null);

            double theta
                = Math.atan2(newDiff.getX(), newDiff.getY()) -
                  Math.atan2(oldDiff.getX(), oldDiff.getY());

            transform_.rotate(theta, ancor.getX(), ancor.getY());


            transform_.translate(diff.getX(), diff.getY());
        }
        catch (Exception e) {
        }

        saveCurrentFrame();

        // Populate from this frame to the end
        populateNewTimeLine();

        // Reset end index to 0
        time_.setEndIndex(0);
    }

    // => save current transform to line component,
    //    so that the selection works
    public void saveCurrentTransformToLineComponent () {
        AffineTransform copy = (AffineTransform)transform_.clone();
        line_.setTransform(copy);
    }

    // => save the current transform into the current frame
    public void saveCurrentFrame () {
        AffineTransform copy = (AffineTransform)transform_.clone();

        // Saves to timeline
        time_.setCurrentTransform(copy);

        saveCurrentTransformToLineComponent();
    }

    // => load the transform at frame index
    public void loadCurrentFrame () {
        AffineTransform transform = time_.getCurrentTransform();
        transform_ = (AffineTransform)transform.clone();

        saveCurrentTransformToLineComponent();
    }

    // -> load frames at the specified index
    public void loadFrame (int index) {
        AffineTransform transform = time_.getTransform(index);
        transform_ = (AffineTransform)transform.clone();

        saveCurrentTransformToLineComponent();
    }

    // --- TimeLine Operations --- //

    // => set start index to the current frame index
    // => assume this object lasts until the end of timeline
    public void initTimeLine () {
        int frameIndex = TimeLine.getFrameIndex();

        // Set start index (leave end index to be 0)
        time_.setStartIndex(frameIndex);

        // Populate all the affine transforms until the end
        int diff = TimeLine.getFrameLength() - frameIndex;
        while (diff-- > 0) {
            time_.setTransform(frameIndex + diff,
                               new AffineTransform());
        }
    }

    // => find out if visible at this point in time
    public boolean isVisible () {
        return time_.isVisible();
    }

    // => insert a copy of current frames before next frame
    public void insertCurrentFrame () {
        // Shift the entire timeline by one
        time_.insertCurrentFrame();

        // Increment end index if not zero
        if (time_.getEndIndex() != 0) {
            time_.setEndIndex(time_.getEndIndex() + 1);
        }
    }

    // => populate frame until the end with the current transform
    public void populateNewTimeLine () {
        int frameIndex = TimeLine.getFrameIndex();

        int diff = TimeLine.getFrameLength() - frameIndex;
        while (diff-- > 0) {
            time_.setTransform(frameIndex + diff,
                               (AffineTransform)transform_.clone());
        }
    }

    // => mark the erase index
    // => returns true if the erase inde overlaps with the start index
    public boolean erase () {
        int endIndex = TimeLine.getFrameIndex();

        time_.setEndIndex(endIndex);

        if (endIndex <= time_.getStartIndex()) {
            return true;
        }
        else {
            return false;
        }
    }

}
