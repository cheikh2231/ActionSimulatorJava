package scratch.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;

public class Variable extends Action{

    public static final Map<Variable,String> variableMap = new HashMap<>();
    private final StringProperty nameVariableProperty= new SimpleStringProperty("");

    public Variable(String name, Integer measure) {
        super(name, measure);
    }

    public String getNameVariable() {
        return nameVariableProperty.get();
    }

    public StringProperty nameVariableProperty() {
        return nameVariableProperty;
    }

    public void setNameVariable(String nameVariable) {
        nameVariableProperty.set(nameVariable);
    }

    @Override
    public Action copy() {
        return null;
    }

    @Override
    public String getUnity() {
        return "";
    }

    @Override
    public String getDetailsName() {
        return "";
    }

    @Override
    public String getFormatName() {
        return "";
    }

    @Override
    public String toString() {
        return "Variable{" +
                "variableMap=" + variableMap.size()+
                '}';
    }


}
