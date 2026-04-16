package scratch.model;

public class ActionEndRepeat extends Action{

    public ActionEndRepeat() {
        super("Fin répéter ","");
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
        return "Fin repéter";
    }

    @Override
    public String getFormatName() {
        return "END_REPEAT";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.ENDREPEAT;
    }


}
