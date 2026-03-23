package scratch.model;

import javafx.beans.property.*;

public abstract class Action {
    private StringProperty name = new SimpleStringProperty();
    private StringProperty measure = new SimpleStringProperty();

    public Action(String name, String measure) {
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

    public String getMeasure() {
        return measure.get();
    }

    public StringProperty measureProperty() {
        return measure;
    }

    public void setMeasure(String measure) {
        if (isValidMeasure(measure))
            this.measure.setValue(measure);
    }



    public abstract Action copy();

    public abstract void executeAction(SceneActions sceneActions);

    public boolean isValidMeasure(String mesure) {
        return false;
    }

    public boolean isDetailable() {
        return true;
    }

    public abstract String getUnity();

    public abstract String getDetailsName();

    public abstract String getFormatName();
    public abstract ActionType getActionType();

    public String getNameVariable(){
        return "";
    }
    public boolean isValidVariableName(String variableName){
        return true;
    }


    public Integer getValue(String value){ //nb
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            if (Variable.variableMap.containsKey(value)){ // oui il contient
                String nb = Variable.variableMap.get(value).getMeasure();
                while (Variable.variableMap.get(nb) != null){
                    nb = Variable.variableMap.get(nb).getMeasure();
                }
                return Integer.parseInt(nb);
            }
        }
        return null;
    }


}
