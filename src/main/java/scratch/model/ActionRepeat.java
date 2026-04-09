package scratch.model;

public class ActionRepeat extends Action{
    public ActionRepeat() {
        super("Répéter ","0");
    }

    @Override
    public Action copy() {
        return new ActionRepeat();
    }

    @Override
    public String getUnity() {
        return " fois";
    }

    @Override
    public String getDetailsName() {
        return "répéter ";
    }

    @Override
    public String getFormatName() {
        return "REPEAT";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.REPEAT;
    }
}
