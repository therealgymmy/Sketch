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

public class View extends    JPanel
                  implements IView {

    private Controller controller_;

    private MouseController mController_;
    private Toolbar         toolbar_;

    public View (Controller controller) {
        super();

        // Set up connection with controller
        controller_ = controller;
        controller_.setView(this);

        // Set up view and event controls
        layoutView();
        registerControllers();

        // Set focus
        requestFocusInWindow();
    }

    // Initialize view layout and view components
    private void layoutView () {
        setSize(800, 600);
        setLayout(new SpringLayout());

        // Initialize components
        toolbar_ = new Toolbar(this);

        // Add components to view
        add(toolbar_);
    }

    // Register event controllers for mouse clicks and motion
    private void registerControllers () {
        mController_ = new MouseController(this, controller_);
        addMouseListener(mController_);
        addMouseMotionListener(mController_);
        setAnimationKey();
    }

    // Set up a keybinding for animation
    public void setAnimationKey () {
        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_A,
                                       InputEvent.CTRL_DOWN_MASK),
                "ctrl_a_down");
        Action down_action = new AbstractAction () {
            public void actionPerformed(ActionEvent e) {
                if (!mController_.getAnimate() &&
                    mController_.getState()
                    == MouseController.State.SELECTION) {
                    controller_.enableAnimation();
                }
                else if (mController_.getAnimate() &&
                         mController_.getState()
                         == MouseController.State.ANIMATE) {
                    controller_.disableAnimation();
                }
            }
        };
        getActionMap().put("ctrl_a_down", down_action);

        getInputMap().put(
                KeyStroke.getKeyStroke(KeyEvent.VK_P,
                                       InputEvent.CTRL_DOWN_MASK),
                "ctrl_p_down");
        Action play_action = new AbstractAction () {
            public void actionPerformed(ActionEvent e) {
                controller_.playAnimation();
            }
        };
        getActionMap().put("ctrl_p_down", play_action);
    }

    // Enable animate mode
    public void enableAnimation () {
        mController_.setState(MouseController.State.ANIMATE);
    }

    // Ask the system to repaint
    @Override
    public void updateView () {
        repaint();
    }

    // Enable draw mode
    @Override
    public void enableDraw () {
        mController_.setState(MouseController.State.DRAW);
    }

    // Enable erase mode
    @Override
    public void enableErase () {
        mController_.setState(MouseController.State.ERASE);
    }

    // Enable selection mode
    @Override
    public void enableSelection () {
        mController_.setState(MouseController.State.SELECTION);
    }

    // Paint the entire view
    @Override
    public void paintComponent (Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;

        Stroke backupStroke = g2d.getStroke();

        float dash1[] = { 10.0f };

        Stroke selectedStroke
            = new BasicStroke(2.0f,
                              BasicStroke.CAP_BUTT,
                              BasicStroke.JOIN_MITER,
                              10.0f, null, 0.0f);

        Stroke selectionStroke
            = new BasicStroke(5.0f,
                              BasicStroke.CAP_BUTT,
                              BasicStroke.JOIN_MITER,
                              10.0f, dash1, 0.0f);

        // Draw all lines
        LinkedList<LineComponent> lineObjects
            = controller_.getLineObjects();
        for (LineComponent lineObject : lineObjects) {
            if (lineObject.isSelected()) {
                g2d.setStroke(selectedStroke);
            }
            lineObject.paint(g2d);
            g2d.setStroke(backupStroke);
        }

        // Draw the selection path
        Polygon selectionPath = controller_.getSelection();
        g2d.setStroke(selectionStroke);
        g2d.draw(selectionPath);
        g2d.setStroke(backupStroke);
    }

}
