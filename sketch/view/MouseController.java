package sketch.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;

// Mouse Controller responsible for dragging/clicking
public class MouseController extends MouseInputAdapter {

    // Describes the state the user interaction is in
    public enum State {
        DRAW,
        ERASE,
        SELECTION,

        DRAG,
        ROTATE,

        RECORD_DRAG,
        RECORD_ROTATE,

        PLAYBACK,
    }

    private View       view_;
    private Controller controller_;

    private State   state_   = State.DRAW;
    private Point2D curLoc_;
    private Point2D ancorLoc_;      // for rotation

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

    public Point2D getAncor () {
        return ancorLoc_;
    }

    public State getState () {
        return state_;
    }

    public boolean isRecording () {
        return state_ == State.RECORD_DRAG ||
               state_ == State.RECORD_ROTATE;
    }

    public void setRecordDrag (boolean cond) {
        if (cond && state_ == State.SELECTION) {
            state_ = State.RECORD_DRAG;
            Log.debug("Drag Recording enabled", 2);
        }
        else if (!cond && state_ == State.RECORD_DRAG) {
            state_ = State.SELECTION;
            Log.debug("Drag Recording disabled", 2);
        }
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
            case DRAG:
            case ROTATE:
                view_.setCursor(selectionCursor_);
                break;
            case RECORD_DRAG:
            case RECORD_ROTATE:
                view_.setCursor(animateCursor_);
                break;
            case PLAYBACK:
                view_.setCursor(playbackCursor_);
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
            case DRAG:    // Drag without the timer
            case RECORD_DRAG:
                drag_mousePressed(e);
                break;
            case ROTATE:
            case RECORD_ROTATE:
                rotate_mousePressed(e);
                break;
        }
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
            case DRAG:
            case RECORD_DRAG:
                break;
            case ROTATE:
            case RECORD_ROTATE:
                rotate_mouseReleased(e);
                break;
        }
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
            case DRAG:    // Drag without the timer
            case RECORD_DRAG:
                drag_mouseDragged(e);
                break;
            case ROTATE:
            case RECORD_ROTATE:
                rotate_mouseDragged(e);
                break;
        }
    }

    // Update state according to mouse keys
    public void updateState (MouseEvent e) {
        switch (state_) {
            case SELECTION:
                if ((e.getModifiers() &
                     InputEvent.CTRL_MASK) != 0 &&
                    (e.getModifiers() &
                     InputEvent.BUTTON1_MASK) != 0) {
                    Log.debug("Ctrl + Left Click pressed", 2);
                    controller_.enableRecordDrag();
                }
                else if ((e.getModifiers() &
                          InputEvent.CTRL_MASK) != 0 &&
                         (e.getModifiers() &
                          InputEvent.BUTTON3_MASK) != 0) {
                    Log.debug("Ctrl + Right Click pressed", 2);
                    controller_.enableRecordRotate();
                }
                else if ((e.getModifiers() &
                          InputEvent.SHIFT_MASK) != 0 &&
                         (e.getModifiers() &
                          InputEvent.BUTTON1_MASK) != 0) {
                    Log.debug("SHIFT + Left Click pressed", 2);
                    view_.enableDrag();
                }
                else if ((e.getModifiers() &
                          InputEvent.SHIFT_MASK) != 0 &&
                         (e.getModifiers() &
                          InputEvent.BUTTON3_MASK) != 0) {
                    view_.enableRotate();
                    Log.debug("SHIFT + Right Click pressed", 2);
                }
                break;
            case RECORD_DRAG:
                if ((e.getModifiers() &
                     InputEvent.BUTTON1_MASK) == 0) {
                    Log.debug("Ctrl + Left Click lifted", 2);
                    controller_.disableRecord();
                }
                break;
            case RECORD_ROTATE:
                if ((e.getModifiers() &
                     InputEvent.BUTTON3_MASK) == 0) {
                    Log.debug("Ctrl + Right Click lifted", 2);
                    controller_.disableRecord();
                }
                break;
            case DRAG:
                if ((e.getModifiers() &
                     InputEvent.BUTTON1_MASK) == 0) {
                    Log.debug("Shift + Left Click lifted", 2);
                    controller_.disableRecord();
                }
                break;
            case ROTATE:
                if ((e.getModifiers() &
                     InputEvent.BUTTON3_MASK) == 0) {
                    Log.debug("Shift + Right Click lifted", 2);
                    controller_.disableRecord();
                }
                break;
        }
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
    public void drag_mousePressed (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());
        curLoc_ = newLoc;
    }

    // Move the selected objects
    // and update the timeline
    public void drag_mouseDragged (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());

        controller_.move(curLoc_, newLoc);

        curLoc_ = newLoc;
    }

    // Update current mouse location
    // and update ancor location
    public void rotate_mousePressed (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());
        curLoc_   = newLoc;
        ancorLoc_ = newLoc;
    }

    // Rotate the selected objects
    // and update the timeline and the current mouse location
    public void rotate_mouseDragged (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());

        controller_.rotate(newLoc, curLoc_, ancorLoc_);

        curLoc_ = newLoc;
    }

    // End of a rotate cycle
    // Update current mouse location
    public void rotate_mouseReleased (MouseEvent e) {
        Point2D newLoc = new Point2D.Double();
        newLoc.setLocation(e.getX(), e.getY());
        curLoc_ = newLoc;
    }

}
