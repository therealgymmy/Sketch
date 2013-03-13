package sketch.model;

import sketch.controller.*;

public class Model extends Object {

    private Controller controller_;

    public Model (Controller controller) {
        super();

        // Set up connection with controller
        controller_ = controller;
        controller_.setModel(this);
    }

}
