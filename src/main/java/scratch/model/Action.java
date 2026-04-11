package scratch.model;

import javafx.beans.property.*;

import java.util.HashSet;
import java.util.Set;

public abstract class Action {
    private StringProperty name = new SimpleStringProperty();
    private StringProperty measure = new SimpleStringProperty();
    private final StringProperty nameVariableProperty= new SimpleStringProperty("");
    private BooleanProperty hasError = new SimpleBooleanProperty(false);

    public BooleanProperty hasErrorProperty() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError.set(hasError);
    }

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
        this.measure.setValue(measure); // ← toujours stocker
    }

    public void setMeasureIfValid(String measure) {
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


    public Integer getValue(String value) {
        if (value == null) return null;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // Ce n'est pas un nombre, on traite comme une variable
            return resolveVariableChain(value);
        }
    }

    private Integer resolveVariableChain(String startVar) {
        String current = startVar;
        // On garde trace des variables déjà visitées pour éviter les boucles infinies
        Set<String> visited = new HashSet<>();

        while (Variable.variableMap.containsKey(current)) {
            // retombe sur var deja vue  boucle infinie détectée
            boolean addToSet = visited.add(current);
            if (!addToSet) {
                return null;
            }

            //recupe le suivant vererifier si c'est un int sinon le current devient le suivant
            //exemple declarer v et h
                    // assgner v 15
                    // assigner h v

            // on fait une boucle jusqu'a trouver la valeur de val (15)
            String next = Variable.variableMap.get(current).getMeasure();


            try {
                return Integer.parseInt(next);
            } catch (NumberFormatException e) {
                current = next;
            }
        }

        return null;
    }

    public boolean canIncrDecr(String measureProperty){
        try{
            Integer.parseInt(measureProperty);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void increment(){
        Integer val = getValue(getMeasure());
        setMeasure(String.valueOf(val+1));
    }

    public void decrement(){
       Integer val = getValue(getMeasure());
       setMeasure(String.valueOf(val-1));
    }

}
