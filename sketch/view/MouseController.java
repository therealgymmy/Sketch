package sketch.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;

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

    private View       view_;
    private Controller controller_;

    private State   state_   = State.DRAW;
    private Point2D curLoc_;

    private Cursor drawCursor_;
    private Cursor eraseCursor_;
    private Cursor selectionCursor_;
    private Cursor animateCursor_;
    private Cursor playbackCursor_;

    MouseController (View view, Controller controller) {
        super();

        view_       = view;
        controller_ = controller;

        layoutView();
    }

    public void layoutView () {
        // Initialize all the cursors
        Toolkit toolkit = Toolkit.getDefaultToolkit();

        // Draw cursor
        Image image1 = toolkit.getImage("sketch/resources/draw.gif");
        Point hotspot1 = new Point(0, 0);
        drawCursor_
            = toolkit.createCustomCursor(image1, hotspot1, "Draw");

        // Erase cursor
        Image image2 = toolkit.getImage("sketch/resources/erase.png");
        Point hotspot2 = new Point(0, 31);
        eraseCursor_
            = toolkit.createCustomCursor(image2, hotspot2, "Erase");

        // Select cursor
        selectionCursor_ = new Cursor(Cursor.HAND_CURSOR);

        // Animate cursor
        animateCursor_ = new Cursor(Cursor.DEFAULT_CURSOR);

        // Playback cursor
        playbackCursor_ = new Cursor(Cursor.DEFAULT_CURSOR);

        view_.setCursor(drawCursor_);
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

    public void updateCursor () {
        switch (state_) {
            case DRAW:
                view_.setCursor(drawCursor_);
                break;
            case ERASE:
                view_.setCursor(eraseCursor_);
                break;
            case SELECTION:
                view_.setCursor(selectionCursor_);
                break;
            case ANIMATE:
                view_.setCursor(animateCursor_);
                break;
            case PLAYBACK:
                view_.setCursor(playbackCursor_);
                break;
        }
    }

    // Update state according to mouse keys
    public void updateState (MouseEvent e) {
        switch (state_) {
            case SELECTION:
                if ((e.getModifiers() &
                     InputEvent.BUTTON1_MASK) == 0) {
                    break;
                }

                if ((e.getModifiers() &
                     InputEvent.CTRL_MASK) != 0) {
                    Log.debug("Ctrl + Click pressed", 2);
                    controller_.enableFrameDefault();
                    controller_.enableAnimation();
                }
                else if ((e.getModifiers() &
                          InputEvent.ALT_MASK) != 0) {
                    Log.debug("ALT + Click pressed", 2);
                    controller_.enableFrameInsertion();
                    controller_.enableAnimation();
                }
                break;
            case ANIMATE:
                if ((e.getModifiers() &
                     InputEvent.BUTTON1_MASK) == 0) {
                    Log.debug("Ctrl lifted", 2);
                    controller_.disableAnimation();
                }
                break;
        }
    }

    @Override
    public void mousePressed (MouseEvent e) {
        Log.debug("Mouse Pressed --> X: " +
                  e.getX() + " Y: " + e.getY(), 1);

        updateState(e);

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

        updateState(e);

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
        updateState(e);
    }

    @Override
    public void mouseDragged (MouseEvent e) {
        Log.debug("Mouse Dragged --> X: " +
                  e.getX() + " Y: " + e.getY(), 1);

        updateState(e);

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
