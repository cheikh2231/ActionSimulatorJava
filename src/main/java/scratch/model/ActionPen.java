package scratch.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ActionPen extends Action{
    private final BooleanProperty penDowStatus = new SimpleBooleanProperty(true);
    public ActionPen(boolean penDowStatus) {
        super(penDowStatus ? "Abaisser stylo":"Lever stylo",0);
        setPenDownStatus(penDowStatus);
    }

    public boolean getPenDowStatus() {
        return penDowStatus.get();
    }

    public void setPenDownStatus(boolean etatStylo) {
        this.penDowStatus.set(etatStylo);
    }

    public String getName(){
       return super.getName();
    }

    @Override
    public Action copy() {
        return new ActionPen(getPenDowStatus());
    }

    @Override
    public void executeAction(SceneActions sceneActions) {
        sceneActions.switchPenStatus();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isDetailable() {
        return false;
    }

    @Override
    public String getUnity() {
        return "";
    }

    @Override
    public String getDetailsName() {
        return getName();
    }

    @Override
    public String getFormatName() {
        return "PEN_"+(getPenDowStatus() ? "DOWN":"UP");
    }

    @Override
    public ActionType getActionType() {
        return ActionType.ACTIONPEN;
    }
}
