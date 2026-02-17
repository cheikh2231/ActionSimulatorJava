package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Action {
    private boolean canAffiche = false;
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty mesure = new SimpleIntegerProperty();

    public Action(String name, Integer mesure) {
        setName(name);
        setMesure(mesure);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getMesure() {
        return mesure.get();
    }

    public IntegerProperty mesureProperty() {
        return mesure;
    }

    public void setMesure(int mesure) {
        this.mesure.setValue(mesure);
    }

    abstract void deplacer(Cursor cursor);


    public abstract Action copy();
}
