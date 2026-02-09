package scratch.model;


import javafx.scene.control.*;

public class ActionAvancer extends Action{
    private int distance ;
    private boolean canAffiche = false;

    public ActionAvancer(int distance) {
        this.distance = distance;
        canAffiche = true;
    }

    public ActionAvancer() {
        this.distance = 30;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    void deplacer(Cursor cursor) {
        cursor.avancer(distance);
    }

    @Override
    public String toString() {
        return  "Avancer de " + (canAffiche ? + getDistance() : " " );
    }
}
