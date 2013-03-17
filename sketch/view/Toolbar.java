package sketch.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import sketch.common.*;
import sketch.controller.*;

public class Toolbar extends JPanel {

    private View       view_;
    private Controller controller_;

    public Toolbar (View view, Controller controller) {
        view_       = view;
        controller_ = controller;

        layoutView();
    }

    public void drawButtonPressed () {
        view_.enableDraw();
        view_.requestFocusInWindow();
        controller_.pauseAnimation();
    }

    public void eraseButtonPressed () {
        view_.enableErase();
        view_.requestFocusInWindow();
        controller_.pauseAnimation();
    }

    public void selectionButtonPressed () {
        view_.enableSelection();
        view_.requestFocusInWindow();
        controller_.pauseAnimation();
    }

    // Set up all the buttons in the toolbar
    public void layoutView () {
        setLayout(new BorderLayout());

        JButton button = null;

        // Create toolbar
        JToolBar toolbar = new JToolBar();
        add(toolbar, BorderLayout.PAGE_START);

        // Draw button
        button = new JButton("Draw");
        button.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Draw button was clicked!");
                drawButtonPressed();
            }
        });
        toolbar.add(button);

        // Erase button
        button = new JButton("Erase");
        button.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Erase button was clicked!");
                eraseButtonPressed();
            }
        });
        toolbar.add(button);

        // Selection button
        button = new JButton("Selection");
        button.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Selection button was clicked!");
                selectionButtonPressed();
            }
        });
        toolbar.add(button);

        // Set default cursor
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(cursor);
    }

}
