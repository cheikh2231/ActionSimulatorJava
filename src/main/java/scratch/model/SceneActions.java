package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SceneActions {

    private ObservableList<Line> lines= FXCollections.observableArrayList();
    private Cursor cursor;
    private ActionPen currentActionPen;

    public boolean currentPenDownStatus() {
        return currentActionPen.getPenDowStatus();
    }

    public void setCurrentActionPenDown(boolean currentPenDownStatus) {
        currentActionPen.setPenDownStatus(currentPenDownStatus);
    }

    public SceneActions(Cursor cursor, ActionPen actionPen) {
        this.cursor = cursor;
        this.currentActionPen = actionPen;
    }

    public void addLine(Line line){
        this.lines.add(line);
    }

    public void switchPenStatus(){
        setCurrentActionPenDown(!currentPenDownStatus());
    }

    public void resetLines(){
        this.lines.clear();
    }

    public void moveForward(double distance){

        Point oldPoints = new Point(cursor.getPosition().getX(), cursor.getPosition().getY());
        cursor.moveForward(distance);

        double newX = cursor.getPosition().getX();
        double newY = cursor.getPosition().getY();

        // limites du canvas
        newX = Math.max(Cursor.TURTLE_SIZE, Math.min(newX, 500 - Cursor.TURTLE_SIZE));
        newY = Math.max(Cursor.TURTLE_SIZE, Math.min(newY, 500 - Cursor.TURTLE_SIZE));

        Point newPoints = new Point(newX, newY);

        cursor.setPosition(newPoints);

        // dessine sssi stylo abaisse
        if(currentPenDownStatus()) {
            Line line = new Line(oldPoints, newPoints);
            addLine(line);
        }
    }

    public void turn(int measure){
        cursor.turn((getCursorAngle()+measure) % 360);
    }

    public ObservableList<Line> getLines() {
        return FXCollections.unmodifiableObservableList(lines);
    }

    public Cursor getCursor() {
        return cursor;
    }

    public int getCursorAngle() {
        return cursor.getAngle();
    }
}
