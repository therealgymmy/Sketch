package sketch.model;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public interface IModel {

    // Get resources
    //public LinkedList<LineComponent> getLineObjects ();
    public Polygon getSelection ();

    // Draw/Erase
    public void addLine   (Point2D start, Point2D end);
    public void eraseLine (Point2D point);
    public void eraseLine (Point2D start, Point2D end);
    public void addNewObject ();
    public void finalizeNewObject ();
    
    // Selection
    public void addNewSelection  ();
    public void encloseSelection ();
    public void extendSelection  (Point2D start, Point2D end);

    // Animate
    public void move (Point2D start, Point2D end);

}
