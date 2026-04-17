package scratch.model;

import javafx.beans.property.StringProperty;

import java.util.Locale;

public class VariableDeclaration extends Variable {

    public VariableDeclaration() {
        super("Déclaration variable ", "0");
    }

    public void addToVariableMap() {
        if (getNameVariable() != null && !getNameVariable().isBlank()){
            variableMap.put(getNameVariable(), this.copy());
            System.out.println(variableMap);
        }
    }

    @Override
    public VariableDeclaration copy() {
        VariableDeclaration vD = new VariableDeclaration();
        vD.setNameVariable(getNameVariable());
        vD.setMeasure(getMeasure());
        return vD;
    }

    @Override
    public boolean canIncrDecr(String measureProperty) {
        return false;
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

    @Override
    public String getFormatName() {
        return super.getFormatName()+"_DECLARATION";
    }
}
