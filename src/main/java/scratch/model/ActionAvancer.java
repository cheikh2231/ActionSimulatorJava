package scratch.model;


import javafx.beans.property.BooleanProperty;
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
        return  getName() + getMesure();
    }

    @Override
    public BooleanProperty isValidMesure() {
        setIsValidMesure(mesureProperty().greaterThan(0).get() && mesureProperty().lessThanOrEqualTo(100).get());
        return getIsValidMesureProperty();
    }
}
