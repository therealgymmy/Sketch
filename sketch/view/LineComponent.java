package sketch.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class LineComponent {

private final LinkedList<Line2D> lines = new LinkedList<Line2D>();

// Default add line
public void addLine (Point2D start, Point2D end) {
    lines.add(new Line2D.Double(start, end));
}

// Clear all the lines
public void clearLines () {
    lines.clear();
}

// Checks if it contains a point
public boolean contains (Point2D point) {
    for (Line2D line : lines) {
        if (line.contains(point)) {
            return true;
        }
    }

    return false;
}

// Checks if it crosses a line
public boolean contains (Point2D start, Point2D end) {
    Line2D otherLine = new Line2D.Double(start, end);
    for (Line2D line : lines) {
        if (line.intersectsLine(otherLine)) {
            return true;
        }
    }

    return false;
}


// Paint all the lines
public void paint (Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    for (Line2D line : lines) {
        g2.draw(line);
    }
}

}
