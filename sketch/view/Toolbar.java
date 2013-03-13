package sketch.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import sketch.view.*;

public class Toolbar extends JPanel {

    private View   view_;

    public Toolbar (View view) {
        view_ = view;

        layoutView();
    }

    // Set up all the buttons in the toolbar
    public void layoutView () {
        JButton button = null;

        // Create toolbar
        JToolBar toolbar = new JToolBar();
        add(toolbar, BorderLayout.PAGE_START);

        // First button
        button = new JButton("Button 1");
        button.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                System.out.println("Button 1 was clicked!");
            }
        });
        toolbar.add(button);

        // Second button
        button = new JButton("Button 2");
        button.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                System.out.println("Button 2 was clicked!");
            }
        });
        toolbar.add(button);

        // Third button
        button = new JButton("Button 3");
        button.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                System.out.println("Button 3 was clicked!");
            }
        });
        toolbar.add(button);

        view_.add(this);
    }

}
