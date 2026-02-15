package scratch.viewmodel;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import scratch.model.Action;


public class ViewModelAction {

    private ObservableList<Action> actionPalette = FXCollections.observableArrayList();
    private ObservableList<Action> actionProg = FXCollections.observableArrayList();
    private ObjectProperty<Action> selectedAction = new SimpleObjectProperty<>();
    private ObjectProperty<Action> selectedActionProg = new SimpleObjectProperty<>();

    public ObservableList<Action> getActionProg() {
        return actionProg;
    }

    public ObjectProperty<Action> getSelectedAction() {
        return selectedAction;
    }

    public StringBinding getSelectedActionProgDetails(){

        StringBinding details = Bindings.createStringBinding(() -> {
            Action actionSelected = getSelectedActionProg().get();
                    if(actionSelected != null){
                        return actionSelected.toString();
                    }
                    return "No action selected";
                },selectedActionProg
        );

        return details;
    }

    public void addToProg() {
        if (canAdd().get()) {
            Action actionCopy =  getSelectedAction().get().copy();
            actionCopy.setCanAffiche(true);
            this.actionProg.add(actionCopy);
        }
    }

    public ObjectProperty<Action> getSelectedActionProg() {
        return selectedActionProg;
    }

    public BooleanBinding canAdd(){
       return getSelectedAction().isNotNull();
    }

}