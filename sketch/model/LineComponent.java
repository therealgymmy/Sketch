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
    private boolean            isSelected_ = false;

    public LineComponent copy () {
        LineComponent c = new LineComponent();

        for (Line2D line : lines_) {
            Line2D l = (Line2D)line.clone();
            c.lines_.add(l);
        }

        return c;
    }

    public boolean isSelected () {
        return isSelected_;
    }

    public void setIsSelected (boolean cond) {
        isSelected_ = cond;
    }

    public void setTransform (AffineTransform trans) {
        trans_ = trans;
    }

    public LinkedList<Line2D> getLines () {
        return lines_;
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

    // Check if contained within a polygon
    public boolean isContainedIn (Polygon poly) {
        for (Line2D line : lines_) {
            if (!poly.contains(line.getP1()) ||
                !poly.contains(line.getP2())) {
                return false;
            }
        }
        return true;
    }

    // Move from start to end
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
