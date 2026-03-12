package scratch.model;


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
}
