package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ActionRepeat extends Action{
    public ActionRepeat() {
        super("Répéter ","4");
    }
    private ObservableList<Action> children = FXCollections.observableArrayList();

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
}
