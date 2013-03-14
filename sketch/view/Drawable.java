package sketch.view;

import java.awt.geom.*;

public interface Drawable extends IView {

    public void addLine   (Point2D start, Point2D end);
    public void eraseLine (Point2D point);
    public void eraseLine (Point2D start, Point2D end);
    public void addNewObject ();
    public void finalizeNewObject ();

}
