package scratch.model;

import javafx.beans.property.*;

import java.util.HashSet;
import java.util.Set;

public abstract class Action {
    private StringProperty name = new SimpleStringProperty();
    private StringProperty measure = new SimpleStringProperty();
    private final StringProperty nameVariableProperty = new SimpleStringProperty("");
    private BooleanProperty hasError = new SimpleBooleanProperty(false);
    // niveau d'indentation dans la liste (pour l'affichage des REPEAT imbriqués)
    private IntegerProperty indentLevel = new SimpleIntegerProperty(0);

    public int getIndentLevel() {
        return indentLevel.get();
    }

    public void setIndentLevel(int level) {
        indentLevel.set(level);
    }

    public IntegerProperty indentLevelProperty() {
        return indentLevel;
    }

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
        this.measure.setValue(measure);
    }

    public void setMeasureIfValid(String measure) {
        if (isValidMeasure(measure))
            this.measure.setValue(measure);
    }

    public abstract Action copy();

    public void executeAction(SceneActions sceneActions) {
        if (!isValidMeasure(String.valueOf(getValue(getMeasure()))))
            throw new RuntimeException();
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

    public boolean isValidVariableName(String variableName) {
        return true;
    }

    public Integer getValue(String value) {
        if (value == null) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // ce n'est pas un entier → on résout comme une variable
            return resolveVariableChain(value);
        }
    }

    private Integer resolveVariableChain(String startVar) {
        String current = startVar;
        Set<String> visited = new HashSet<>();

        while (Variable.variableMap.containsKey(current)) {
            // détection de boucle : si on revoit une variable déjà visitée, on arrête   assignation val : val !!!!
            if (!visited.add(current)) return null;

            String next = Variable.variableMap.get(current).getMeasure();
            try {
                // si la valeur est un entier, on a trouvé la valeur finale
                return Integer.parseInt(next);
            } catch (NumberFormatException e) {
                // sinon on continue la chaîne (ex: h => v => 15)
                current = next;
            }
        }
        return null;
    }

    // vrai uniquement si la mesure est un entier (pas une variable)
    public boolean canIncrDecr(String measureProperty) {
        try {
            Integer.parseInt(measureProperty);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void increment() {
        setMeasure(String.valueOf(getValue(getMeasure()) + 1));
    }

    public void decrement() {
        setMeasure(String.valueOf(getValue(getMeasure()) - 1));
    }
}