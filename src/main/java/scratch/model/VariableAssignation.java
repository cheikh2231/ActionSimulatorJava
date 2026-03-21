package scratch.model;


import javafx.beans.property.StringProperty;

public class VariableAssignation extends Variable {
    public VariableAssignation() {
        super("Assignation ", 0);
    }

    @Override
    public Action copy() {
        return new VariableAssignation();
    }

    @Override
    public String getDetailsName() {
        return getName();
    }

    @Override
    public ActionType getActionType() {
        return ActionType.VARIABLEASSIGNATION;
    }


}
