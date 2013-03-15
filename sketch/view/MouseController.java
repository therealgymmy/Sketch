package sketch.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.model.*;

// Mouse Controller responsible for dragging/clicking
public class MouseController extends MouseInputAdapter
                             implements KeyListener {

    // Describes the state the user interaction is in
    public enum State {
        DRAW,
        ERASE,
        SELECTION,
        ANIMATE,
        PLAYBACK,
    }

    private IView  view_;
    private IModel controller_;

    private State   state_   = State.DRAW;
    private Point2D curLoc_;

    MouseController (IView view, IModel controller) {
        super();

        view_       = view;
        controller_ = controller;
    }

    public boolean getAnimate () {
        return state_ == State.ANIMATE;
    }

    public State getState () {
        return state_;
    }

    public void setState (State state) {
        state_ = state;
    }

    @Override
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
                selection_mousePressed(e);
                break;
            case ANIMATE:
                animate_mousePressed(e);
                break;
        }

        view_.updateView();
    }

    @Override
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
                selection_mouseReleased(e);
                break;
            case ANIMATE:
                break;
        }

        view_.updateView();
    }

    @Override
    public void mouseMoved (MouseEvent e) {
    }

    @Override
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
                selection_mouseDragged(e);
                break;
            case ANIMATE:
                animate_mouseDragged(e);
                break;
        }

        view_.updateView();
    }

    @Override
    public void keyPressed (KeyEvent e) {
        Log.debug("Key Pressed --> " + e, 2);
    }

    @Override
    public void keyTyped (KeyEvent e) {
        Log.debug("Key Typed --> " + e, 2);
    }

    @Override
    public void keyReleased (KeyEvent e) {
        Log.debug("Key Released --> " + e, 2);
    }

    // Update current mouse location
    // and add new line object
    public void draw_mousePressed (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());
        curLoc_ = newLoc;
        controller_.addNewObject();
    }

    // Form a complete object out of the lines drawn
    // between mouse press and release
    public void draw_mouseReleased (MouseEvent e) {
        controller_.finalizeNewObject();
    }

    // Draw a line between previous and new mouse locations,
    // and update current mouse location
    public void draw_mouseDragged (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());

        // Draw a new line
        controller_.addLine(curLoc_, newLoc);

        curLoc_ = newLoc;
    }

    // Erase a line that the point it draws touches
    public void erase_mousePressed (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());
        curLoc_ = newLoc;

        // Pass in a point
        controller_.eraseLine(curLoc_);
    }

    // Erase a line that the line it draws crosses
    public void erase_mouseDragged (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());

        // Pass in start and end of a line
        controller_.eraseLine(curLoc_, newLoc);

        curLoc_ = newLoc;
    }

    // Update current mouse location
    // and prepare for selection
    public void selection_mousePressed (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());
        curLoc_ = newLoc;
        controller_.addNewSelection();
    }

    // Finalize a selection
    public void selection_mouseReleased (MouseEvent e) {
        controller_.encloseSelection();
    }

    // Extend the selection path
    // and update current mouse location
    public void selection_mouseDragged (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());

        // Draw a new line
        controller_.extendSelection(curLoc_, newLoc);

        curLoc_ = newLoc;
    }

    // Update current mouse location
    // and prepare for animation
    public void animate_mousePressed (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());
        curLoc_ = newLoc;
    }

    // Move the selected objects
    // and update the timeline
    public void animate_mouseDragged (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());

        controller_.move(curLoc_, newLoc);

        curLoc_ = newLoc;
    }

}
