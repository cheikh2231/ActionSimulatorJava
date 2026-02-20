package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.Action;


public class ViewModelAction {

    private ObservableList<Action> actionPalette = FXCollections.observableArrayList();
    private ObservableList<Action> actionProg = FXCollections.observableArrayList();
    private ObjectProperty<Action> selectedActionPalette = new SimpleObjectProperty<>();
    private ObjectProperty<Action> selectedActionProg = new SimpleObjectProperty<>();
    private StringProperty mesureProperty = new SimpleStringProperty();
    private IntegerProperty selectedIndex = new SimpleIntegerProperty();


    private BooleanProperty isValidMesureProperty = new SimpleBooleanProperty(true);

    public ObservableList<Action> getActionProg() {
        return actionProg;
    }

    public ViewModelAction() {
        listeners();
    }

    public ObjectProperty<Action> getSelectedActionPalette() {
        return selectedActionPalette;
    }

    public StringBinding getSelectedActionProgDetailsName() {

        StringBinding details = Bindings.createStringBinding(() -> {
                    Action actionSelected = getSelectedActionProg().get();
                    if (actionSelected != null) {
                        return actionSelected.getName();
                    }
                    return " No action selected ";
                }, selectedActionProg
        );
        return details;
    }

    public void addPaletteToProg() {
        if (canAdd().get()) {
            Action actionCopy = getSelectedActionPalette().get().copy();
            this.actionProg.add(actionCopy);
        }
    }

    public ObjectProperty<Action> getSelectedActionProg() {
        return selectedActionProg;
    }

    public BooleanBinding canAdd() {
        return getSelectedActionPalette().isNotNull();
    }

    public BooleanBinding isSelectedActionProg() {
        return getSelectedActionProg().isNotNull();
    }


    public StringProperty getMesureProperty() {
        return mesureProperty;
    }

    public void listeners() {
        selectedActionProg.addListener((obs, o, n) -> {
            if (n != null)
                mesureProperty.set(String.valueOf(n.mesureProperty().get()));
            else
                mesureProperty.setValue("");
        });

        mesureProperty.addListener((obs, o, n) -> {

            if (!n.isEmpty()) {
                setMesure(Integer.parseInt(n));
            }

            isValidMesureProperty.set(selectedActionProg.get().isValidMesure().get());


        });
    }

    public void setMesure(int mesure) {
        selectedActionProg.get().setMesure(mesure);
    }

    public void removeAction() {
        actionProg.remove(selectedActionProg.get());
    }

    public void clearAllActions() {
        actionProg.clear();
    }

    public BooleanBinding isEmptyActionProg() {
        return Bindings.createBooleanBinding(() -> {
            return actionProg.isEmpty();
        }, actionProg);
    }

    public void duplicateAction() {
        if (isSelectedActionProg().get()) {
            Action dupplicated = getSelectedActionProg().get().copy();
            actionProg.add(dupplicated);
        }
    }

    public BooleanBinding canMoveUp() {
        return Bindings.createBooleanBinding(() -> {
            int index = actionProg.indexOf(selectedActionProg.get());
            return index > 0;
        }, selectedActionProg);
    }

    public BooleanBinding canMoveDown() {
        return Bindings.createBooleanBinding(() -> {
            int index = actionProg.indexOf(selectedActionProg.get());
            return index < size() - 1;
        }, selectedActionProg);
    }

    public void moveUp() {
        if (canMoveUp().get()) {
            int index = actionProg.indexOf(selectedActionProg.get());
            Action current = actionProg.remove(index);
            actionProg.add(index - 1, current);
            selectedActionProg.set(actionProg.get(index - 1));
        }
    }

    public void moveDown() {
        if (canMoveDown().get()) {
            int index = actionProg.indexOf(selectedActionProg.get());
            Action current = actionProg.remove(index);
            actionProg.add(index + 1, current);
            selectedActionProg.set(actionProg.get(index + 1));
        }
    }

    public int size() {
        return actionProg.size();
    }

    //    public BooleanProperty isValidMesureProperty(){
//        if (selectedActionProg.isNotNull().get())
//            return selectedActionProg.get().isValidMesure();
//        return Action.getIsValidMesureProperty();
//    }
    public BooleanProperty getIsValidMesureProperty() {
        return isValidMesureProperty;
    }

}