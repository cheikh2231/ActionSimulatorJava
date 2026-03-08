package scratch.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Actions {

    private static ObservableList<Action> actions = FXCollections.observableArrayList(
            new ActionMoveForward(),
            new ActionTurnLeft(),
            new ActionTurnRight(),
            new ActionPen(false),
            new ActionPen(true)
    );

    public static ObservableList<Action> getActions() {
        return FXCollections.unmodifiableObservableList(actions);
    }


    public static Action getAction(int idx){
        return (idx>=0 && idx<actions.size() ? actions.get(idx) : null);
    }


}
