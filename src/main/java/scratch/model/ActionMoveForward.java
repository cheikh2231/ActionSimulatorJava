package scratch.model;


public class ActionMoveForward extends Action {

    public ActionMoveForward(int mesure) {
       super("Avancer de ",mesure);
    }

    public ActionMoveForward() {
        super("Avancer de ",30);
    }


    @Override
    public Action copy() {
        return new ActionMoveForward(getMeasure());
    }

    @Override
    public void executeAction(SceneActions sceneActions) {
        sceneActions.moveForward(this.getMeasure());
    }

    @Override
    public String toString() {
        return  getName() + getMeasure();
    }

    @Override
    public boolean isValidMeasure(int mesure) {
        return mesure>0 && mesure<=100;
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
}
