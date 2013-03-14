package sketch.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;

public class View extends    JComponent
                  implements IView,
                             Drawable {

    private Controller controller_;

    private MouseController mController_;
    private Toolbar         toolbar_;

    private LineComponent             curLineObject_;
    private LinkedList<LineComponent> lineObjects_;
    

    public View (Controller controller) {
        super();

        // Set up connection with controller
        controller_ = controller;
        controller_.setView(this);

        // Set up view and event controls
        layoutView();
        registerControllers();

        // initialize members
        lineObjects_   = new LinkedList<LineComponent>();
        curLineObject_ = new LineComponent();
    }

    // Initialize view layout and view components
    private void layoutView () {
        setSize(800, 600);
        setLayout(new SpringLayout());

        // Initialize components
        toolbar_       = new Toolbar(this);
    }

    // Register event controllers for mouse clicks and motion
    private void registerControllers () {
        mController_ = new MouseController(this);
        addMouseListener(mController_);
        addMouseMotionListener(mController_);
    }

    // Ask the system to repaint
    public void updateView () {
        repaint();
    }

    // Enable draw mode
    public void enableDraw () {
        mController_.setState(MouseController.State.DRAW);
    }

    // Enable erase mode
    public void enableErase () {
        mController_.setState(MouseController.State.ERASE);
    }

    // Enable selection mode
    public void enableSelection () {
        mController_.setState(MouseController.State.SELECTION);
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

        // Record the line objects that contains the point
        for (LineComponent lineObject : lineObjects_) {
            if (lineObject.contains(point)) {
                toRemove.add(lineObject);
            }
        }

        // Delete all the line objects that match
        for (LineComponent r : toRemove) {
            Log.debug("Removed a line object", 2);

            lineObjects_.remove(r);
        }
    }

    // Erase a line that the line it draws crosses
    @Override
    public void eraseLine (Point2D start, Point2D end) {
        LinkedList<LineComponent> toRemove
            = new LinkedList<LineComponent>();

        // Record the line objects that contains the point
        for (LineComponent lineObject : lineObjects_) {
            if (lineObject.contains(start, end)) {
                toRemove.add(lineObject);
            }
        }

        // Delete all the line objects that match
        for (LineComponent r : toRemove) {
            Log.debug("Removed a line object", 2);

            lineObjects_.remove(r);
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

        curLineObject_ = new LineComponent();
    }

    // Paint the entire view
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        for (LineComponent lineObject : lineObjects_) {
            lineObject.paint(g);
        }
    }

}
