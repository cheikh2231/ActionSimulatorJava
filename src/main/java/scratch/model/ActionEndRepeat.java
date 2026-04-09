package scratch.model;

public class ActionEndRepeat extends Action{

    public ActionEndRepeat() {
        super("Fin répéter ","0");
    }


    @Override
    public Action copy() {
        return new ActionEndRepeat();
    }

    @Override
    public String getUnity() {
        return null;
    }

    @Override
    public String getDetailsName() {
        return null;
    }

    @Override
    public String getFormatName() {
        return "END_REPEAT";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.REPEAT;
    }
}
