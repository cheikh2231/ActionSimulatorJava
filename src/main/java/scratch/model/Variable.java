package scratch.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.HashMap;
import java.util.Map;

public class Variable extends Action{

    public static final ObservableMap<String,Variable> variableMap = FXCollections.observableHashMap();
    public Variable(String name, String measure) {
        super(name, measure);
    }


    @Override
    public boolean isValidVariableName(String variableName) {
        String regex = "^[a-zA-Z_][a-zA-Z0-9_]*$";
        return variableName.matches(regex) || variableName.isEmpty();
    }

    @Override
    public Action copy() {
        return null;
    }

    @Override
    public void executeAction(SceneActions sceneActions) {
        return;
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
    public ActionType getActionType() {
        return null;
    }


    @Override
    public boolean isValidMeasure(String mesure) {
        return true;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "variableMap=" + variableMap.size()+
                '}';
    }


}
