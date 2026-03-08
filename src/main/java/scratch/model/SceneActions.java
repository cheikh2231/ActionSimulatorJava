package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.text.View;


public class SceneActions {

    private ObservableList<Line> lines= FXCollections.observableArrayList();
    private Cursor cursor;
    private ActionStylo actuActionStylo;

    public boolean getActuActionStyloEtat() {
        return actuActionStylo.getEtatStyloAbaisser();
    }

    public void setActuActionStylo(boolean actuActionStyloEtat) {
        actuActionStylo.setEtatStyloAbaisser(actuActionStyloEtat);
    }

    public SceneActions(Cursor cursor,ActionStylo actionStylo) {
        this.cursor = cursor;
        this.actuActionStylo=actionStylo;
    }

    public void addDessin(Line line){
        this.lines.add(line);
    }

    public void changeEtatStylo(){
        setActuActionStylo(!getActuActionStyloEtat());
    }

    public void resetLines(){
        this.lines.clear();
    }

    public void avancer(double distance){

        Point oldPoints = new Point(cursor.getPosition().getX(), cursor.getPosition().getY());

        double angle = cursor.getAngle();
        cursor.avancer(distance);

        double newX = cursor.getPosition().getX();
        double newY = cursor.getPosition().getY();

        // limites du canvas
        newX = Math.max(0, Math.min(newX, 500));
        newY = Math.max(0, Math.min(newY, 500));

        Point newPoints = new Point(newX, newY);

        cursor.setPosition(newPoints);

        if(getActuActionStyloEtat()){
            Line line = new Line(oldPoints, newPoints);
            addDessin(line);
        }
    }

    public void tourner(int angle){
        cursor.tourner(angle);
    }

    public ObservableList<Line> getLines() {
        return lines;
    }

    public Cursor getCursor() {
        return cursor;
    }
}
