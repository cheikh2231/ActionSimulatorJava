package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class SceneActions {

    private List<Line> lines= new ArrayList<>();
    private IntegerProperty lineIndex = new SimpleIntegerProperty(-1);
    private Cursor cursor;

    public SceneActions(Cursor cursor) {
        this.cursor = cursor;
    }

    public void addDessin(Line line){
        this.lines.add(line);
    }

    public void executeDessin(){
        lineIndex.set(lineIndex.get()+1);
    }

    public int getSize(){
        return lines.size();
    }

    public void avancer(double distance){
        Point oldPoints = cursor.getPosition();
        cursor.avancer(distance);
        Point newPoints = cursor.getPosition();
        Line line = new Line(oldPoints,newPoints);
        addDessin(line);
    }

}
