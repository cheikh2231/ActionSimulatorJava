package scratch.model;

import javafx.beans.property.*;

public abstract class Action {
    private boolean canAffiche = false;
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty mesure = new SimpleIntegerProperty();

    private static BooleanProperty isValidMesureProperty = new SimpleBooleanProperty();

    public Action(String name, Integer mesure) {
        setName(name);
        setMesure(mesure);
    }

    public static BooleanProperty getIsValidMesureProperty() {
        return isValidMesureProperty;
    }

    public void setIsValidMesure(boolean isValidMesure) {
        isValidMesureProperty.set(isValidMesure);
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

    public abstract BooleanProperty isValidMesure();
}
