package sketch.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import sketch.common.*;
import sketch.controller.*;

public class Slider extends JPanel
                    implements ChangeListener {

    private View       view_;
    private Controller controller_;

    private JButton playButton_;
    private JButton pauseButton_;
    private JButton insertButton_;
    private JButton clearButton_;

    private JSlider slider_;
    private int     curIndex_ = 0;

    public Slider (View view, Controller controller) {
        view_       = view;
        controller_ = controller;

        layoutView();

        enablePlayButton();
    }

    // Set up all components
    public void layoutView () {
        setLayout(new BorderLayout());

        // Create slider toolbar
        JToolBar toolbar = new JToolBar();
        add(toolbar, BorderLayout.PAGE_START);

        // Play button
        playButton_ = new JButton("Play");
        playButton_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Play button was clicked!", 2);
                controller_.playAnimation();
            }
        });
        toolbar.add(playButton_);

        // Pause button
        pauseButton_ = new JButton("Pause");
        pauseButton_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Pause button was clicked!", 2);
                controller_.pauseAnimation();
            }
        });
        toolbar.add(pauseButton_);

        // Insert button
        insertButton_ = new JButton("Insert");
        insertButton_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Insert button was clicked!", 2);
                controller_.insertCurrentFrame();
            }
        });
        toolbar.add(insertButton_);

        // Clear button
        clearButton_ = new JButton("Clear");
        clearButton_.addActionListener(new ActionListener () {
            public void actionPerformed (ActionEvent e) {
                Log.debug("Clear button was clicked!", 2);
            }
        });
        toolbar.add(clearButton_);

        // Create JSlider
        slider_ = new JSlider(JSlider.HORIZONTAL,
                              0, 0, 0);
        slider_.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 0, 20));
        slider_.addChangeListener(this);
        toolbar.add(slider_);

        // Set default cursor
        Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(cursor);
    }

    // Enable playback button
    public void enablePlayButton () {
        pauseButton_.setEnabled(false);
        playButton_.setEnabled(true);
    }

    // Enable pause button
    public void enablePauseButton () {
        playButton_.setEnabled(false);
        pauseButton_.setEnabled(true);
    }

    // Change the maximum value of the slider
    public void setSliderLength (int length) {
        slider_.setMaximum(length);
    }

    // Change slider pointer locaton
    public void setPointerPosition (int pos) {
        slider_.setValue(pos);
    }

    public void stateChanged (ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        int index = (int)source.getValue();
        if (index != curIndex_) {
            Log.debug("index = " + index, 2);

            curIndex_ = index;

            if (view_.getState()
                    != MouseController.State.RECORD) {
                Log.debug("Load Frame at index " + index, 2);
                controller_.loadFrame(index);
            }
        }
    }

}
