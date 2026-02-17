package scratch.model;


import javafx.scene.control.*;

public class ActionAvancer extends Action {

    public ActionAvancer(int mesure) {
       super("Avancer de ",mesure);
    }

    public ActionAvancer() {
        super("Avancer de ",30);
    }

    @Override
    void deplacer(Cursor cursor) {
        cursor.avancer(getMesure());
    }

    @Override
    public Action copy() {
        return new ActionAvancer();
    }

    @Override
    public String toString() {
        return  getName() + (isCanAffiche() ? + getMesure() : " " );
    }
}
