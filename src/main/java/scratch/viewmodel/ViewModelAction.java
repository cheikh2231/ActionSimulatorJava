package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.*;


public class ViewModelAction {

    private ActionAvancer actionAvancer = new ActionAvancer();
    private ActionTournerGauche actionTournerGauche = new ActionTournerGauche();
    private ActionTournerDroite actionTournerDroite = new ActionTournerDroite();
    private ActionStylo actionStyloAbaisser = new ActionStylo(false);
    private ActionStylo actionStyloLever = new ActionStylo(true);
    private ObservableList<Action> actionPalette = FXCollections.observableArrayList();
    private ObservableList<Action> actionProg = FXCollections.observableArrayList();
    private ObservableList<Action> actionScene = FXCollections.observableArrayList();

    private ObjectProperty<Action> selectedActionPalette = new SimpleObjectProperty<>();
    private ObjectProperty<Action> selectedActionProg = new SimpleObjectProperty<>();
    private StringProperty mesureProperty = new SimpleStringProperty();
    private IntegerProperty selectedIndex = new SimpleIntegerProperty();
    private BooleanProperty isvalidMesureProperty = new SimpleBooleanProperty(true);
    private BooleanProperty isDetailableProperty = new SimpleBooleanProperty(false);


    public ObservableList<Action> getActionProg() {
        return actionProg;
    }

    public ObservableList<Action> getActionPalette() {
        return actionPalette;
    }

    public ObservableList<Action> getActionScene() {
        return actionScene;
    }

    public void addToListScene(){
        this.actionScene.addAll(actionProg);
    }

    public ViewModelAction() {
        actionPalette.addAll(actionAvancer,actionTournerGauche,actionTournerDroite,actionStyloAbaisser,actionStyloLever);
        listeners();
    }

    public ObjectProperty<Action> getSelectedActionPalette() {
        return selectedActionPalette;
    }

    public StringBinding getSelectedActionProgDetailsName() {

        StringBinding details = Bindings.createStringBinding(() -> {
                    Action actionSelected = getSelectedActionProg().get();
                    if (actionSelected != null) {
                        return actionSelected.getDetailsName();
                    }
                    return " No action selected ";
                }, selectedActionProg
        );
        return details;

    }

    public StringBinding getSelectedActionProgDetailsUnity(){
        StringBinding detailsUnity = Bindings.createStringBinding(() -> {
                    Action actionSelected = getSelectedActionProg().get();
                    if (actionSelected != null) {
                        return actionSelected.getUnity();
                    }
                    return "";
                }, selectedActionProg
        );
        return detailsUnity;
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

    public void setIsDetailableProperty(boolean isDetailableProperty) {
        this.isDetailableProperty.set(isDetailableProperty);
    }

    public BooleanProperty IsDetailableProperty(){
        return isDetailableProperty;
    }


    public StringProperty getMesureProperty() {
        return mesureProperty;
    }

    public void listeners() {
        selectedActionProg.addListener((obs, o, n) -> {
            if (n != null){
                mesureProperty.set(String.valueOf(n.mesureProperty().get()));
                this.setIsDetailableProperty(n.isDetailable());
            }
            else {
                mesureProperty.setValue("");
                this.setIsDetailableProperty(false);
            }
            isvalidMesureProperty.set(true);
        });

        mesureProperty.addListener((obs, o, n) -> {

            if (n.isEmpty()){
                isvalidMesureProperty.set(false);
                return;
            }
            try {
                Action action = getSelectedActionProg().get();
                int val = Integer.parseInt(n);
                setMesure(val);
                isvalidMesureProperty.set(action.isValidMesure(val));
            }catch (NumberFormatException e){
                isvalidMesureProperty.set(false);
            }

        });
    }

    public void setMesure(int mesure) {
        getSelectedActionProg().get().setMesure(mesure);
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

    public BooleanBinding isEmptyActionScene(){
        return Bindings.createBooleanBinding(() -> {
            return actionScene.isEmpty();
        },actionScene);
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

    public BooleanProperty isValidMesureProperty() {
        return isvalidMesureProperty;
    }
}