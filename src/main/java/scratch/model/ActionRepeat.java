package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ActionRepeat extends Action{
    public ActionRepeat() {
        super("Répéter ","4");
    }
    private ObservableList<Action> children = FXCollections.observableArrayList();
    private int nbRead = getValue(getMeasure());
    public ObservableList<Action> getChildren() { return children; }

    public void addChild(Action a){
        children.add(a);
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
        return "Répéter ";
    }

    @Override
    public String getFormatName() {
        return "REPEAT";
    }

    @Override
    public ActionType getActionType() {
        return ActionType.REPEAT;
    }

    @Override
    public boolean canIncrDecr(String measureProperty) {
        return false;
    }

    public int getNbRead() {
        return nbRead;
    }

    public void setNbRead(int nbRead) {
        this.nbRead = nbRead;
    }

    public void resetNbRead(){
        setNbRead(getValue(getMeasure()));
    }

    public boolean hasConflictWithChildren() {
        return children.stream()
                .filter(action -> action.getActionType() == ActionType.VARIABLEASSIGNATION
                        || action.getActionType() == ActionType.VARIABLEINCRDECR)
                .anyMatch(action -> action.getNameVariable().equals(this.getMeasure()));
    }
}
