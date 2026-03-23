package scratch.model;

public class ActionTurnRight extends ActionTurn {

    public ActionTurnRight(String mesure) {
        super("droite de ", mesure);
    }

    public ActionTurnRight() {
        super("droite de ");
    }

    @Override
    public Action copy() {
        return new ActionTurnRight(getMeasure());
    }

    @Override
    public void executeAction(SceneActions sceneActions) {
        super.executeAction(sceneActions);
        Integer val = getValue(getMeasure());
        if (val != null)
            sceneActions.turn(-(getValue(getMeasure()) + 360));
    }

    @Override
    public String toString() {
        return  super.getName() + getMeasure();
    }
    @Override
    public String getDetailsName() {
        return super.getDetailsName() + "droite de ";
    }

    @Override
    public String getFormatName() {
        return super.getFormatName()+"RIGHT";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.ACTIONTURNRIGHT;
    }

}
