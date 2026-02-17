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
    private StringProperty mesureProperty = new SimpleStringProperty();
    public ObservableList<Action> getActionProg() {
        return actionProg;
    }

    public ViewModelAction() {
        listeners();
    }

    public ObjectProperty<Action> getSelectedAction() {
        return selectedAction;
    }

    public StringBinding getSelectedActionProgDetailsName(){

        StringBinding details = Bindings.createStringBinding(() -> {
            Action actionSelected = getSelectedActionProg().get();
                    if(actionSelected != null){
                        return actionSelected.getName();
                    }
                    return "No action selected";
                },selectedActionProg
        );
        return details;
    }

    public void addToProg() {
        if (canAdd().get()) {
            Action actionCopy =  getSelectedAction().get().copy();
            this.actionProg.add(actionCopy);
        }
    }

    public ObjectProperty<Action> getSelectedActionProg() {
        return selectedActionProg;
    }

    public BooleanBinding canAdd(){
       return getSelectedAction().isNotNull();
    }

    public BooleanBinding isSelectedAction(){
       return getSelectedActionProg().isNotNull();
    }


    public StringProperty getMesureProperty(){
        return mesureProperty;
    }

    public void listeners(){
        selectedActionProg.addListener((obs,o,n)->{
            if(n != null)
                mesureProperty.set(String.valueOf(n.mesureProperty().get()));
            else
                mesureProperty.setValue("");
        });

        mesureProperty.addListener((obs,o,n)->{
            if (!n.isEmpty()) {
                setMesure(Integer.parseInt(n));
            }
        });

    }

    public void setMesure(int mesure){
        selectedActionProg.get().setMesure(mesure);
    }

    public void removeAction(){
        actionProg.remove(selectedActionProg.get());
    }

    public void clearAllActions(){
        actionProg.clear();
    }
}