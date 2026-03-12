package scratch.model;

public class VariableDeclaration extends Variable {
    public static int id = 0;
    private  String nameVariable;

    public VariableDeclaration(String nameVariable) {
        super("Déclaration variable", 0);
        this.nameVariable = nameVariable;
        ++id;
    }

    public void declare(){
        variableMap.put(this,0);
    }

    public void setNameVariable(String nameVariable) {
        this.nameVariable = nameVariable;
    }

    public String getNameVariable() {
        return nameVariable;
    }

    @Override
    public Action copy() {
        return new VariableDeclaration(getName());
    }

    @Override
    public String getDetailsName() {
        return getName();
    }

}
