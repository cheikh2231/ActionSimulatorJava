package scratch.model;

import javafx.beans.property.*;

public abstract class Action {
    private StringProperty name = new SimpleStringProperty();
    private IntegerProperty measure = new SimpleIntegerProperty();

    public Action(String name, Integer measure) {
        setName(name);
        setMeasure(measure);
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

    public int getMeasure() {
        return measure.get();
    }

    public IntegerProperty measureProperty() {
        return measure;
    }

    public void setMeasure(Integer measure) {
        if (isValidMeasure(measure))
            this.measure.setValue(measure);
    }

    public abstract Action copy();

    public boolean isValidMeasure(int mesure) {
        return false;
    }

    public boolean isDetailable() {
        return true;
    }

    public abstract String getUnity();

    public abstract String getDetailsName();

    public abstract String getFormatName();
}
