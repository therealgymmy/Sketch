package sketch.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import sketch.common.*;
import sketch.controller.*;

public class Toolbar extends JPanel {

    private View       view_;
    private Controller controller_;

    private JButton draw_;
    private JButton erase_;
    private JButton select_;
    private JButton color_;

    private JButton save_;
    private JButton load_;

    private Color pressedButtonColor = new Color(184, 207, 229);

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

    public void saveButtonPressed () {
        controller_.save("save.xml");
    }

    public void loadButtonPressed () {
        controller_.load("save.xml");
    }

    public void colorButtonPressed () {
        Color color = JColorChooser.showDialog(
                this,
                "Choose Color",
                null);
        if (color != null) {
            controller_.changeColor(color);
            color_.setBackground(color);
        }
    }

    public void enableDrawButton () {
        draw_.setBackground(pressedButtonColor);
        erase_.setBackground(null);
        select_.setBackground(null);
    }

    public void enableEraseButton () {
        draw_.setBackground(null);
        erase_.setBackground(pressedButtonColor);
        select_.setBackground(null);
    }

    public void enableSelectButton () {
        draw_.setBackground(null);
        erase_.setBackground(null);
        select_.setBackground(pressedButtonColor);
    }

    // Set up all the buttons in the toolbar
    public void layoutView () {
        setLayout(new BorderLayout());

        // Create toolbar
        JToolBar toolbar = new JToolBar();
        add(toolbar, BorderLayout.PAGE_START);

        // Draw button
        draw_ = new JButton("Draw");
        draw_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Draw button was clicked!");
                drawButtonPressed();
            }
        });
        toolbar.add(draw_);

        // Erase button
        erase_ = new JButton("Erase");
        erase_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Erase button was clicked!");
                eraseButtonPressed();
            }
        });
        toolbar.add(erase_);

        // Selection button
        select_ = new JButton("Select");
        select_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Selection button was clicked!");
                selectionButtonPressed();
            }
        });
        toolbar.add(select_);

        // Color button
        color_ = new JButton("     ");
        color_.setBackground(Color.BLACK);
        color_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Color Picker button was clicked!");
                colorButtonPressed();
            }
        });
        toolbar.add(color_);

        // Save button
        save_ = new JButton("Save");
        save_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Save button was clicked!");
                saveButtonPressed();
            }
        });
        toolbar.add(save_);

        // Load button
        load_ = new JButton("Load");
        load_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Load button was clicked!");
                loadButtonPressed();
            }
        });
        toolbar.add(load_);

        // Set default cursor
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(cursor);
    }

}
