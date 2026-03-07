package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class SceneActions {

    private List<Line> lines= new ArrayList<>();
    private Cursor cursor;

    public SceneActions(Cursor cursor) {
        this.cursor = cursor;
    }

    public void addDessin(Line line){
        this.lines.add(line);
    }

    public void resetLines(){
        this.lines.clear();
    }

    public void avancer(double distance){
        Point oldPoints = new Point(cursor.getPosition().getX(),cursor.getPosition().getY());
        double angle = cursor.getAngle();
        cursor.avancer(distance);
        Point newPoints = new Point(cursor.getPosition().getX(),cursor.getPosition().getY());
        Line line = new Line(oldPoints,newPoints);
        addDessin(line);
    }

    public void tourner(int angle){
        cursor.tourner( angle);
    }

    public List<Line> getLines() {
        return lines;
    }

    public Cursor getCursor() {
        return cursor;
    }
}
