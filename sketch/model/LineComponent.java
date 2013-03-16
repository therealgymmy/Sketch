package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class LineComponent {

    private AffineTransform    trans_ = new AffineTransform();
    private LinkedList<Line2D> lines_ = new LinkedList<Line2D>();
    private boolean            isSelected_;

    // Return a copy of this line object
    public LineComponent copy () {
        LineComponent c = new LineComponent();

        // AffineTransform
        c.trans_ = (AffineTransform)trans_.clone();

        // Copy over all the lines
        for (Line2D line : lines_) {
            line = (Line2D)line.clone();
            c.lines_.add(line);
        }

        // isSelected
        c.isSelected_ = isSelected_;

        return c;
    }

    // Find out if the other line object is the same as this one
    public boolean equal (LineComponent otherObject) {
        ListIterator<Line2D> lineItr = lines_.listIterator(0);
        ListIterator<Line2D> otherLineItr
            = otherObject.lines_.listIterator(0);

        while (lineItr.hasNext() || otherLineItr.hasNext()) {
            // If one iterator is at the end while the other isn't
            // -> No math
            if (!lineItr.hasNext() || !otherLineItr.hasNext()) {
                return false;
            }

            // Check if every single line is the same
            Line2D line      = lineItr.next();
            Line2D otherLine = otherLineItr.next();
            if (!(line.getX1() == otherLine.getX1() &&
                  line.getY1() == otherLine.getY1() &&
                  line.getX2() == otherLine.getX2() &&
                  line.getY2() == otherLine.getY2())) {
                return false;
            }
        }

        return true;
    }

    public boolean isSelected () {
        return isSelected_;
    }

    public void setIsSelected (boolean cond) {
        isSelected_ = cond;
    }

    // Default add line
    public void addLine (Point2D start, Point2D end) {
        lines_.add(new Line2D.Double(start, end));
    }

    // Clear all the lines
    public void clearLines () {
        lines_.clear();
    }

    // Checks if it intersects a point
    public boolean intersects (Point2D point) {
        for (Line2D line : lines_) {
            line = transform(line);
            if (line.contains(point)) {
                return true;
            }
        }

        return false;
    }

    // Checks if it crosses a line
    public boolean intersects (Point2D start, Point2D end) {
        Line2D otherLine = new Line2D.Double(start, end);
        for (Line2D line : lines_) {
            line = transform(line);
            if (line.intersectsLine(otherLine)) {
                return true;
            }
        }

        return false;
    }

    // Check if it crosses a line
    public boolean intersects (Line2D otherLine) {
        for (Line2D line : lines_) {
            line = transform(line);
            if (line.intersectsLine(otherLine)) {
                return true;
            }
        }

        return false;
    }

    // Check if it crosses a polygon
    public boolean intersects (Polygon poly) {
        for (Line2D line : lines_) {
            line = transform(line);
            if (poly.intersects(line.getBounds())) {
                return true;
            }
        }

        return false;
    }

    // Check if it crosses a line object
    public boolean intersects (LineComponent lineObject) {
        for (Line2D line : lines_) {
            line = transform(line);
            if (lineObject.intersects(line)) {
                return true;
            }
        }

        return false;
    }


    // Paint all the lines
    public void paint (Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        AffineTransform backupTrans = g2d.getTransform();
        g2d.setTransform(trans_);
        for (Line2D line : lines_) {
            g2d.draw(line);
        }
        g2d.setTransform(backupTrans);
    }

    // Move from start to end and
    // create a new AffineTransform
    public void move (Point2D start, Point2D end) {
        double dx = end.getX() - start.getX();
        double dy = end.getY() - start.getY();

        trans_.translate(dx, dy);
    }

    // Transform line to the supposed location this line object is at
    private Line2D transform (Line2D line) {
        Line2D newLine = (Line2D)line.clone();
        newLine.setLine(
                    trans_.transform(newLine.getP1(), null),
                    trans_.transform(newLine.getP2(), null)
                    );
        return newLine;
    }

}
