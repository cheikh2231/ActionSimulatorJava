package scratch.model;


import javafx.beans.property.StringProperty;

public class VariableAssignation extends Variable {
    public VariableAssignation() {
        super("Assignation ", "");
    }

    @Override
    public Action copy() {
        return new VariableAssignation();
    }

    @Override
    public String getDetailsName() {
        return getName() + "de la variable ";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.VARIABLEASSIGNATION;
    }

    @Override
    public String getFormatName() {
        return super.getFormatName()+"_ASSIGNATION";
    }
}
