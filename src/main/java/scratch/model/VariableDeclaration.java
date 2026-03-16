package scratch.model;

public class VariableDeclaration extends Variable {

    public VariableDeclaration() {
        super("Déclaration variable ", 0);
    }

    public void addToVariableMap() {
        variableMap.put(getNameVariable(), this);
        System.out.println(variableMap);
    }

    @Override
    public Action copy() {
        return new VariableDeclaration();
    }


    @Override
    public String getDetailsName() {
        return getName();
    }

    public boolean isValidVariableName(String variableName) {
        String regex = "^[a-zA-Z_][a-zA-Z0-9_]*$";
        return variableName.matches(regex) || variableName.isEmpty();
    }

    @Override
    public void setNameVariable(String nameVariable) {
        if (isValidVariableName(nameVariable))
            super.setNameVariable(nameVariable);
    }
}
