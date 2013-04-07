package sketch.model;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sketch.common.*;

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

    public void clear () {
        lines_.clear();
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
            line = transform(line);
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

    // --- Serialization --- //
    
    // -> serliaze all data
    public void serialize (Element root, Document doc) {
        Element tag = doc.createElement("line_component");
        root.appendChild(tag);

        // serialize all lines
        for (Line2D line : lines_) {
            Element lineTag = doc.createElement("line2d");
            tag.appendChild(lineTag);

            // start Location
            Element startTag = doc.createElement("start");
            Element x_startTag = doc.createElement("x");
            Element y_startTag = doc.createElement("y");

            x_startTag.appendChild(doc.createTextNode(
                        Integer.toString((int)line.getX1())));
            y_startTag.appendChild(doc.createTextNode(
                        Integer.toString((int)line.getY1())));

            startTag.appendChild(x_startTag);
            startTag.appendChild(y_startTag);
            lineTag.appendChild(startTag);

            // end location
            Element endTag = doc.createElement("end");
            Element x_endTag = doc.createElement("x");
            Element y_endTag = doc.createElement("y");

            x_endTag.appendChild(doc.createTextNode(
                        Integer.toString((int)line.getX2())));
            y_endTag.appendChild(doc.createTextNode(
                        Integer.toString((int)line.getY2())));

            endTag.appendChild(x_endTag);
            endTag.appendChild(y_endTag);
            lineTag.appendChild(endTag);
        }
    }

    // -> deserialize all data
    public void deserialize (Element element) {
        NodeList list = element.getElementsByTagName("line2d");
        for (int i = 0; i < list.getLength(); ++i) {
            Element line2dTag = (Element)list.item(i);

            Element startTag =
                (Element)line2dTag.getElementsByTagName("start").item(0);
            Element x_startTag =
                (Element)startTag.getElementsByTagName("x").item(0);
            Element y_startTag =
                (Element)startTag.getElementsByTagName("y").item(0);

            Point2D start = new Point2D.Double(
                    extractDouble(x_startTag),
                    extractDouble(y_startTag));

            Element endTag =
                (Element)line2dTag.getElementsByTagName("end").item(0);
            Element x_endTag =
                (Element)endTag.getElementsByTagName("x").item(0);
            Element y_endTag =
                (Element)endTag.getElementsByTagName("y").item(0);

            Point2D end = new Point2D.Double(
                    extractDouble(x_endTag),
                    extractDouble(y_endTag));

            lines_.add(new Line2D.Double(start, end));
        }
    }

    // -> extract double out of an element
    public double extractDouble (Element element) {
        return Double.parseDouble(element.getChildNodes().item(0).getNodeValue());
    }

}
