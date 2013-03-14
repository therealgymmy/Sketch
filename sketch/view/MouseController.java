package sketch.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;

// Mouse Controller responsible for dragging/clicking
public class MouseController extends MouseInputAdapter {

    // Describes the state the user interaction is in
    public enum State {
        DRAW,
        ERASE,
        SELECTION,
        SELECTION_DRAG
    }

    private Drawable view_;

    private State state_ = State.DRAW;
    private Point2D          curLoc_;

    MouseController (View view) {
        super();
        view_ = view;
    }

    public void setState (State state) {
        state_ = state;
    }

    public void mousePressed (MouseEvent e) {
        Log.debug("Mouse Pressed --> X: " +
                  e.getX() + " Y: " + e.getY(), 1);

        switch (state_) {
            case DRAW:
                draw_mousePressed(e);
                break;
            case ERASE:
                erase_mousePressed(e);
                break;
            case SELECTION:
                break;
            case SELECTION_DRAG:
                break;
        }

        view_.updateView();
    }

    public void mouseReleased (MouseEvent e) {
        Log.debug("Mouse Released --> X: " +
                  e.getX() + " Y: " + e.getY(), 1);

        switch (state_) {
            case DRAW:
                draw_mouseReleased(e);
                break;
            case ERASE:
                break;
            case SELECTION:
                break;
            case SELECTION_DRAG:
                break;
        }

        view_.updateView();
    }

    public void mouseMoved (MouseEvent e) {
    }

    public void mouseDragged (MouseEvent e) {
        Log.debug("Mouse Dragged --> X: " +
                  e.getX() + " Y: " + e.getY(), 1);

        switch (state_) {
            case DRAW:
                draw_mouseDragged(e);
                break;
            case ERASE:
                erase_mouseDragged(e);
                break;
            case SELECTION:
                break;
            case SELECTION_DRAG:
                break;
        }

        view_.updateView();
    }

    // Update current mouse location
    public void draw_mousePressed (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());
        curLoc_ = newLoc;
        view_.addNewObject();
    }

    // Form a complete object out of the lines drawn
    // between mouse press and release
    public void draw_mouseReleased (MouseEvent e) {
        view_.finalizeNewObject();
    }

    // Draw a line between previous and new mouse locations,
    // and update current mouse location
    public void draw_mouseDragged (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());

        // Draw a new line
        view_.addLine(curLoc_, newLoc);

        curLoc_ = newLoc;
    }

    // Erase a line that the point it draws touches
    public void erase_mousePressed (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());
        curLoc_ = newLoc;

        // Pass in a point
        view_.eraseLine(curLoc_);
    }

    // Erase a line that the line it draws crosses
    public void erase_mouseDragged (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());

        // Pass in start and end of a line
        view_.eraseLine(curLoc_, newLoc);

        curLoc_ = newLoc;
    }

}
