package scratch.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;


public class Variable extends Action{

    public static final ObservableMap<String,Variable> variableMap = FXCollections.observableHashMap();
    public Variable(String name, String measure) {
        super(name, measure);
    }


    @Override
    public boolean isValidVariableName(String variableName) {
        String regex = "^[a-zA-Z_][a-zA-Z0-9_]*$";
        return variableName.matches(regex) || variableName.isBlank();
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
        return "VAR";
    }

    @Override
    public ActionType getActionType() {
        return null;
    }


    @Override
    public String toString() {
        return "Variable{" +
                "variableMap=" + variableMap.size()+
                '}';
    }

    public static void clear(){
        Variable.variableMap.clear();
    }

}
