package scratch.model;

public class ActionTurnLeft extends ActionTurn {

    public ActionTurnLeft(String mesure) {
        super("gauche de ", mesure);
    }

    public ActionTurnLeft() {
        super("gauche de ");
    }

    @Override
    public Action copy() {
        return new ActionTurnLeft(getMeasure());
    }

    @Override
    public void executeAction(SceneActions sceneActions) {
        if (getValue(getMeasure()) != null)
            sceneActions.turn(getValue(this.getMeasure()));
    }

    @Override
    public String toString() {
        return super.getName() + getMeasure();
    }

    @Override
    public String getDetailsName() {
        return super.getDetailsName() + "gauche de ";
    }

    @Override
    public String getFormatName() {
        return super.getFormatName() + "LEFT";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.ACTIONTURNLEFT;
    }
}
