package scratch.model;


public class ActionMoveForward extends Action {

    public ActionMoveForward(String mesure) {
       super("Avancer de ",mesure);
    }

    public ActionMoveForward() {
        super("Avancer de ","30");
    }


    @Override
    public Action copy() {
        return new ActionMoveForward(getMeasure());
    }

    @Override
    public void executeAction(SceneActions sceneActions) {
        super.executeAction(sceneActions);
        sceneActions.moveForward(getValue(getMeasure()));
    }

    @Override
    public String toString() {
        return  getName() + getMeasure();
    }

    @Override
    public boolean isValidMeasure(String mesure) {
        if (Variable.variableMap.containsKey(mesure))
            return true;
        Integer val = getValue(mesure);
        if (val != null)
            return val > 0 && val <= 100;
        return true;
    }

    @Override
    public String getDetailsName() {
        return getName();
    }

    @Override
    public String getUnity() {
        return "  pixels ";
    }

    @Override
    public String getFormatName() {
        return "MOVE_FORWARD";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.ACTIONMOVEFORWARD;
    }
}
