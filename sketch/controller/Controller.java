package sketch.controller;

import sketch.model.*;
import sketch.view.*;

public class Controller extends Object {
    private View view_;
    private Model model_;

    public Controller () {
        super();
    }

    public void setView (View view) {
        view_ = view;
    }

    public void setModel (Model model) {
        model_ = model;
    }

    // --- View Related Functions --- //
    public void updateView () {
        view_.updateView();
    }

    // --- Model Related Functions --- //

}
