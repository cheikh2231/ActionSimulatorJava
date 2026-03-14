package scratch.model;

public class VariableDeclaration extends Variable {

    public VariableDeclaration() {
        super("Déclaration variable ", 0);
    }

    public void addToVariableMap(){
        variableMap.put(getNameVariable(),this);
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

}
