package scratch.model;

import javafx.beans.property.*;

public abstract class Action {
    private StringProperty name = new SimpleStringProperty();
    private StringProperty measure = new SimpleStringProperty();
    private final StringProperty nameVariableProperty= new SimpleStringProperty("");

    public StringProperty nameVariableProperty() {
        return nameVariableProperty;
    }
    public void setNameVariable(String nameVariable) {
        if (isValidVariableName(nameVariable))
            this.nameVariableProperty.set(nameVariable);
    }

    public String getNameVariable() {
        return nameVariableProperty.get();
    }

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

    public void executeAction(SceneActions sceneActions){
        if (!isValidMeasure(String.valueOf(getValue(getMeasure())))){
            throw new RuntimeException();
        }
    }

    public boolean isValidMeasure(String mesure) {
        return true;
    }

    public boolean isDetailable() {
        return true;
    }

    public abstract String getUnity();

    public abstract String getDetailsName();

    public abstract String getFormatName();
    public abstract ActionType getActionType();

    public boolean isValidVariableName(String variableName){
        return true;
    }


    public Integer getValue(String value){ //nb
        try {
            // Dans le cas ou je recois un integer et non une variable je retourne l'integer
            return Integer.parseInt(value);
        }catch (Exception e){
            // Si j'ai reçu une variable, je vérifie qu'elle est dans la map.
            if (Variable.variableMap.containsKey(value)){
                // si elle est dans la map on récupère la value
                String nb = Variable.variableMap.get(value).getMeasure();
                // si la variable.getMeasure contient elle meme une variable alors on entre dans la boucle jusqu'a ce
                // qu'on obtient quelque chose qu'on peut parseInt()
                while (Variable.variableMap.get(nb) != null){
                    nb = Variable.variableMap.get(nb).getMeasure();
                }

                // quand on est remonté jusqu'a la valeure originelle, alors on try et catch pour savoir si on peut la parseInt,
                // dans le cas contraire c'est que cest une mauvaise valeure
                try{
                    return Integer.parseInt(nb);
                } catch (NumberFormatException ex) {
                    return null;
                }
            }
        }
        return null;
    }


}
