package scratch.model;

public class VariableDeclaration extends Variable {
    private static Integer id=0;
    public VariableDeclaration() {
        super("Déclaration variable ", 0);
        id++;
    }

    public void addToVariableMap(){
        variableMap.put(this,getNameVariable());
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
