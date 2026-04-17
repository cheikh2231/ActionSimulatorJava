package scratch.model;


import javafx.beans.property.StringProperty;

public class VariableAssignation extends Variable {
    public VariableAssignation() {
        super("Assignation ", "");
    }

    public Action copy() {
        VariableAssignation vA = new VariableAssignation();
        vA.setNameVariable(getNameVariable());
        vA.setMeasure(getMeasure());
        return vA;
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
        return super.getFormatName()+"_ASSIGNMENT";
    }


}


