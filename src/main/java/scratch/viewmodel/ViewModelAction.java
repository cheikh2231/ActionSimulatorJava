package scratch.viewmodel;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.Action;

public class ViewModelAction {

    private ObservableList<Action> actionPalette = FXCollections.observableArrayList();
    private ObservableList<Action> actionProg = FXCollections.observableArrayList();
    private ObjectProperty<Action> selectedAction = new SimpleObjectProperty<>();

    public ObservableList<Action> getActionPalette() {
        return actionPalette;
    }

    public ObservableList<Action> getActionProg() {
        return actionProg;
    }

    public ObjectProperty<Action> getSelectedAction() {
        return selectedAction;
    }

    public void addToProg() {
        if (getSelectedAction().get() != null)
            this.actionProg.add(getSelectedAction().get());
    }
}