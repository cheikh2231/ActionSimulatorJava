package scratch.viewmodel;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.*;


public class ViewModelAction {

    private final Programme programme;
    private ObservableList<Action> actionsPalette = Actions.getActions();
//    private ObservableList<Action> actionProg = FXCollections.observableArrayList();
    private ObservableList<Action> actionScene = FXCollections.observableArrayList();
    private IntegerProperty paletteIndex = new SimpleIntegerProperty(-1);
    private IntegerProperty progIndex = new SimpleIntegerProperty(-1);
    private StringProperty mesureProperty = new SimpleStringProperty();
    private BooleanProperty isvalidMesureProperty = new SimpleBooleanProperty(true);
    private BooleanProperty isDetailableProperty = new SimpleBooleanProperty(false);
    private ObjectProperty<Action> selectedActionScene = new SimpleObjectProperty<>();
    private ObjectProperty<Action> selectedActionProg = new SimpleObjectProperty<>();

    public ObservableList<Action> getActionProg() {
        return programme.getActionsProg();
    }

    public ObservableList<Action> getActionPalette() {
        return FXCollections.unmodifiableObservableList(actionsPalette);
    }

    public ObservableList<Action> getActionScene() {
        return actionScene;
    }

    public void addToListScene(){
        this.actionScene.addAll(programme.getActionsProg());
    }

    public ViewModelAction(Programme programme) {
        this.programme = programme;
        listeners();
    }


    public IntegerProperty paletteIndexProperty() {
        return paletteIndex;
    }

    public IntegerProperty progIndexProperty() {
        return progIndex;
    }

    public ObjectProperty<Action> getSelectedActionProg(){
        return selectedActionProg;
    }

    public void setSelectedActionProg(Action action) {
        selectedActionProg.set(action);
    }

    public StringBinding getSelectedActionProgDetailsName() {

        StringBinding details = Bindings.createStringBinding(() -> {
                    Action actionSelected = getSelectedActionProg().get();
                    if (actionSelected != null) {
                        return actionSelected.getDetailsName();
                    }
                    return " No action selected ";
                }, progIndex
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
                }, progIndex
        );
        return detailsUnity;
    }

    public void addPaletteToProg() {
        int idx = paletteIndexProperty().get();
        programme.ajouterAction(idx);
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
        getSelectedActionProg().addListener((obs, o, n) -> {
            if (n != null){
                mesureProperty.set(String.valueOf(n.getMesure()));
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

        progIndex.addListener((obs,o,n)->{
            setSelectedActionProg(programme.getActionProg(n.intValue()));
        });
    }

    public void setMesure(int mesure) {
        getSelectedActionProg().get().setMesure(mesure);
    }

    public void removeAction() {
        programme.getActionsProg().remove(selectedActionProg.get());

    }

    public void clearAllActions() {
        programme.getActionsProg().clear();
        actionScene.clear();
    }

    public BooleanBinding isEmptyActionProg() {
        return Bindings.createBooleanBinding(() -> {
            return programme.getActionsProg().isEmpty();
        }, programme.getActionsProg());
    }

    public BooleanBinding isEmptyActionScene(){
        return Bindings.createBooleanBinding(() -> {
            return actionScene.isEmpty();
        },actionScene);
    }

    public void duplicateAction() {
        programme.dupplicate(progIndex.get());
    }

    public BooleanBinding isSelectedActionProg() {
        return selectedActionProg.isNotNull();
    }

    public BooleanBinding canMoveUp() {
       return progIndex.greaterThan(0);
    }

    public BooleanBinding canMoveDown() {
        return Bindings.createBooleanBinding(() -> {
            return progIndex.get() < programme.getActionProgSize()-1 && progIndex.get() >=0;
        },selectedActionProg,progIndex);
    }

    public void moveUp(){
        programme.up(progIndex.get());
        progIndex.set(progIndex.get()-1);
    }

    public void moveDown(){
        programme.down(progIndex.get());
        progIndex.set(progIndex.get()+1);
    }

    public BooleanProperty isValidMesureProperty() {
        return isvalidMesureProperty;
    }

    public void setActionScene(){
        this.selectedActionScene.set(programme.getActionsProg().getFirst());
        this.selectedActionProg.set(programme.getActionsProg().getFirst());
    }

//    public ObjectProperty<Action> getSelectedActionScene() {
//        return selectedActionScene;
//    }
//
//    public void getNext(){
//        int index = actionScene.indexOf(getSelectedActionScene().get());
//        if (canMoveDown().get()) {
//            Action suivant = actionScene.get(index + 1);
//            this.selectedActionScene.set(suivant);
//            this.selectedActionProg.set(suivant);
//        }
//    }


}