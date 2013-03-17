package sketch.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;
import sketch.model.*;

public class Paint {

    private static float dash1[] = { 10.0f };

    private static Stroke selectedStroke
        = new BasicStroke(2.0f,
                          BasicStroke.CAP_BUTT,
                          BasicStroke.JOIN_MITER,
                          10.0f, null, 0.0f);

    private static Stroke selectionStroke
        = new BasicStroke(5.0f,
                          BasicStroke.CAP_BUTT,
                          BasicStroke.JOIN_MITER,
                          10.0f, dash1, 0.0f);



    // Draw all the line objects
    public static void paint (Graphics2D           g2d,
                              LineObjectCollection collection) {
        ArrayList<LineObject> objects = collection.getObjects();

        Stroke backupStroke = g2d.getStroke();
        for (LineObject object : objects) {
            if (object.isSelected()) {
                g2d.setStroke(selectedStroke);
            }
            paint(g2d, object);
            g2d.setStroke(backupStroke);
        }
    }

    // Draw a line object
    // Only draw if the object is visible
    public static void paint (Graphics2D g2d,
                              LineObject object) {
        if (!object.isVisible()) {
            return;
        }

        AffineTransform backupTrans = g2d.getTransform();
        AffineTransform trans = object.getCurrentTransform();
        
        g2d.setTransform(trans);
        paint(g2d, object.getLineComponent());
        g2d.setTransform(backupTrans);
    }

    // Draw a line component
    public static void paint (Graphics2D    g2d,
                              LineComponent component) {
        for (Line2D line : component.getLines()) {
            g2d.draw(line);
        }
    }

    // Draw the selection path
    public static void paintSelection (Graphics2D g2d,
                                       Polygon  poly) {
        Stroke backupStroke = g2d.getStroke();

        g2d.setStroke(selectionStroke);
        g2d.draw(poly);
        g2d.setStroke(backupStroke);
    }

}