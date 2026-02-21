package scratch.model;

import javafx.beans.property.BooleanProperty;

public class ActionTourner extends Action{


    public ActionTourner(String name ,int mesure) {
        super("Tourner à " + name,mesure);
    }

    public ActionTourner(String name) {
        super("Tourner à " + name,90);
    }


    @Override
    void deplacer(Cursor cursor) {
        cursor.tourner(getMesure());
    }


    @Override
    public Action copy() {
        return null;
    }

    public String toString(){
        return getName();
    }

    @Override
    public boolean isValidMesure(int mesure) {
        return mesure>0 && mesure<=180;
    }
}
