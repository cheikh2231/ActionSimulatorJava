package scratch.model;

public class VariableDeclaration extends Variable {
    public static int id = 0;

    public VariableDeclaration(String nameVariable) {
        super("Déclaration variable ", 0);
        ++id;
    }
    public void declare(){
        variableMap.put(this,0);
    }


    @Override
    public Action copy() {
        return new VariableDeclaration(super.getNameVariable());
    }


    @Override
    public String getDetailsName() {
        return getName();
    }

}
