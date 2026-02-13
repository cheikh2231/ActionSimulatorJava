package scratch.viewmodel;

import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.Action;

public class ViewModelAction {

    private ObservableList<Action> actionPalette = FXCollections.observableArrayList();
    private ObservableList<Action> actionProg = FXCollections.observableArrayList();
    private ObjectProperty<Action> selectedAction = new SimpleObjectProperty<>();


    public ObservableList<Action> getActionProg() {
        return actionProg;
    }

    public ObjectProperty<Action> getSelectedAction() {
        return selectedAction;
    }

    public void addToProg() {
        if (canAdd().get()) {
            getSelectedAction().get().setCanAffiche();
            this.actionProg.add(getSelectedAction().get());
        }
    }

    public BooleanBinding canAdd(){
       return getSelectedAction().isNotNull();
    }
}