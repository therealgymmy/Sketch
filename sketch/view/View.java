package sketch.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.controller.*;

public class View extends JComponent {

    private Controller controller_;

    public View (Controller controller) {
        super();

        // Set up connection with controller
        controller_ = controller;
        controller_.setView(this);

        // Set up view and event controls
        layoutView();
        registerControllers();
    }

    // Initialize view layout and view components
    private void layoutView () {
        setSize(800, 600);
        setLayout(new SpringLayout());

        // Initialize components
        Toolbar toolbar = new Toolbar(this);
    }

    // Register event controllers for mouse clicks and motion
    private void registerControllers () {
        MouseInputListener mil = new MouseController(this);
        addMouseListener(mil);
        addMouseMotionListener(mil);
    }

    // Ask the system to repaint
    public void updateView () {
        repaint();
    }

    // Paint the entire view
    public void paintComponent (Graphics g) {
        super.paintComponent(g);
    }

}
