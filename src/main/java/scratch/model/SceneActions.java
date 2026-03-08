package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SceneActions {

    private ObservableList<Line> lines= FXCollections.observableArrayList();
    private Cursor cursor;
    private ActionStylo currentActionStylo;

    public boolean getCurrentActionStylo() {
        return currentActionStylo.getEtatStyloAbaisser();
    }

    public void setCurrentActionStylo(boolean currentActionStyloEtat) {
        currentActionStylo.setEtatStyloAbaisser(currentActionStyloEtat);
    }

    public SceneActions(Cursor cursor,ActionStylo actionStylo) {
        this.cursor = cursor;
        this.currentActionStylo = actionStylo;
    }

    public void addLine(Line line){
        this.lines.add(line);
    }

    public void changeEtatStylo(){
        setCurrentActionStylo(!getCurrentActionStylo());
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
        newX = Math.max(0, Math.min(newX, 500));
        newY = Math.max(0, Math.min(newY, 500));

        Point newPoints = new Point(newX, newY);
        System.out.println(oldPoints.getX() + " " + oldPoints.getX() +"  \n  " + newPoints.getX() + " " + newPoints.getY());
        cursor.setPosition(newPoints);
        System.out.println(getLines());
        // dessine sssi stylo abaisse
        if(getCurrentActionStylo()) {
            Line line = new Line(oldPoints, newPoints);
            addLine(line);
        }
    }

    public void turn(int angle){
        cursor.turn(angle);
    }

    public ObservableList<Line> getLines() {
        return FXCollections.unmodifiableObservableList(lines);
    }

    public Cursor getCursor() {
        return cursor;
    }
}
