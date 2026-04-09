package scratch.model;


public class VariableIncrDecr extends Variable {
    public VariableIncrDecr() {
        super("Incr/Dec variable ", "");
    }

    @Override
    public Action copy() {
        return new VariableIncrDecr();
    }

    @Override
    public String getDetailsName() {
        return "Incrémentation de la variable ";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.VARIABLEINCRDECR;
    }

    @Override
    public String getFormatName() {
        return "INCREMENT_"+super.getFormatName();
    }
}
