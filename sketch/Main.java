package sketch;

import javax.swing.*;

import sketch.controller.*;
import sketch.model.*;
import sketch.view.*;

public class Main {

    public static void createAndShowGUI () {
        Controller c = new Controller();
        Model      m = new Model(c);
        View       v = new View(c);

        JFrame frame = new JFrame("Sketch");
        frame.getContentPane().add(v);
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack();
        frame.setVisible(true);
    }

    public static void main (String[] args) {
        SwingUtilities.invokeLater(new Runnable () {
            public void run () {
                createAndShowGUI();
            }
        });
    }

}
