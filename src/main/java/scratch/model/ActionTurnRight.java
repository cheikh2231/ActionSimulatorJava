package scratch.model;

public class ActionTurnRight extends ActionTurn {

    public ActionTurnRight(int mesure) {
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
        sceneActions.turn(-(getMeasure() + 360) );
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

}
