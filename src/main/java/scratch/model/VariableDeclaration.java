package scratch.model;

import javafx.beans.property.StringProperty;

import java.util.Locale;

public class VariableDeclaration extends Variable {

    public VariableDeclaration() {
        super("Déclaration variable ", "0");
    }

    public void addToVariableMap() {
        if (getNameVariable() != null && !getNameVariable().isBlank()){
            variableMap.put(getNameVariable(), this);
            System.out.println(variableMap);
        }


    }

    @Override
    public Action copy() {
        return new VariableDeclaration();
    }


    @Override
    public String getDetailsName() {
        return getName();
    }

    @Override
    public ActionType getActionType() {
        return ActionType.VARIABLEDECLARATION;
    }

    @Override
    public String getNameVariable() {
        return super.getNameVariable();
    }

}
