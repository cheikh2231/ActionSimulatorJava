package scratch.viewmodel;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.*;

import java.util.List;


public class ViewModelAction {

    private final Programme programme;
    private final SceneActions sceneActions;
    private ObservableList<Action> actionsPalette = Actions.getActions();
    private IntegerProperty paletteIndex = new SimpleIntegerProperty(-1);
    private IntegerProperty progIndex = new SimpleIntegerProperty(-1);
    private StringProperty mesureProperty = new SimpleStringProperty();
    private BooleanProperty isvalidMesureProperty = new SimpleBooleanProperty(true);
    private BooleanProperty isDetailableProperty = new SimpleBooleanProperty(false);
    private ObjectProperty<Action> actionSelected= new SimpleObjectProperty<>();
    private BooleanProperty hasChargerScene = new SimpleBooleanProperty(false);
    private IntegerProperty sceneIndex = new SimpleIntegerProperty(0);
    public ObjectProperty<Action> actionSelectedProperty() {
        return actionSelected;
    }

    public ObservableList<Action> getActionsProg() {
        return programme.getActionsProg();
    }

    public ObservableList<Action> getActionPalette() {
        return FXCollections.unmodifiableObservableList(actionsPalette);
    }


    public ViewModelAction(Programme programme,SceneActions sceneActions) {
        this.programme = programme;
        this.sceneActions = sceneActions;
        listeners();
    }

    public IntegerProperty paletteIndexProperty() {
        return paletteIndex;
    }

    public IntegerProperty progIndexProperty() {
        return progIndex;
    }

    public StringBinding getSelectedActionProgDetailsName() {

        return Bindings.createStringBinding(() -> {
                    Action actionSelected = programme.getActionProg(progIndex.get());
                    if (actionSelected != null) {
                        return actionSelected.getDetailsName();
                    }
                    return " No action selected ";
                }, progIndex ,getActionsProg()
        );

    }

    public StringBinding getSelectedActionProgDetailsUnity(){
        return Bindings.createStringBinding(() -> {
                    Action actionSelected = programme.getActionProg(progIndex.get());
                    if (actionSelected != null) {
                        return actionSelected.getUnity();
                    }
                    return "";
                }, progIndex , getActionsProg()
        );
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
       actionSelectedProperty().addListener((obs, o, n) -> {
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
                Action action = actionSelected.get();
                if(action!=null){
                    int val = Integer.parseInt(n);
                    action.setMesure(val);
                    isvalidMesureProperty.set(action.isValidMesure(val));
                }
            }catch (NumberFormatException e){
                isvalidMesureProperty.set(false);
            }

        });

    }
    public void removeAction() {
        programme.removeAction(progIndex.get());
    }

    public void clearAllActions() {
        programme.clear();
    }

    public BooleanBinding isEmptyActionProg() {
        return Bindings.createBooleanBinding(programme::isEmpty, programme.getActionsProg());
    }

    public void duplicateAction() {
        programme.dupplicate(progIndex.get());
    }

    public BooleanBinding isSelectedActionProg() {
        return progIndex.greaterThanOrEqualTo(0);
    }

    public BooleanBinding canMoveUp() {
       return progIndex.greaterThan(0);
    }

    public BooleanBinding canMoveDown() {
        return Bindings.createBooleanBinding(() -> {
            return programme.canMoveDown(progIndex.get());
        },progIndex,getActionsProg());
    }

    public void moveUp(){
        if (canMoveUp().get()) {
            programme.up(progIndex.get());
            progIndex.set(progIndex.get() - 1);
        }
    }

    public void moveDown(){
        if (canMoveDown().get()) {
            programme.down(progIndex.get());
            progIndex.set(progIndex.get() + 1);
        }
    }

    public BooleanProperty isValidMesureProperty() {
        return isvalidMesureProperty;
    }


    public BooleanBinding hasAnomalies(){
        return Bindings.createBooleanBinding(()->{
            return hasDuplicate() || isEmptyActionProg().get();
        },getActionsProg());
    }

    public BooleanBinding hasActionsProgChange(){
        return Bindings.createBooleanBinding(()->{
            if (hasChargerScene.get()){
                hasChargerScene.set(false);
                return true;
            }
            return false;
        },getActionsProg());
    }

    public void charger(){
        reinitialiser();
        setHasChargerScene(true);
    }

    public void executer(){
        if(!programme.isEmpty())
            progIndex.set(0);
    }

    public void reinitialiser(){
        progIndex.set(-1);
        sceneIndex.set(0);
        sceneActions.resetLines();
    }

    private boolean hasDuplicate(){
        boolean abaisser = true;
        for (Action action: getActionsProg() ) {
               if (action instanceof ActionStylo actionStylo){
                   if (actionStylo.getEtatStyloAbaisser() == abaisser)
                       return true;
                   else
                       abaisser = actionStylo.getEtatStyloAbaisser();
               }
        }
        return false;
    }

    public void setHasChargerScene(boolean hasChargerScene) {
        this.hasChargerScene.set(hasChargerScene);
    }

    public BooleanProperty hasChargerSceneProperty() {
        return hasChargerScene;
    }

    public void getNext() {
        int idx = sceneIndex.get();

        sceneIndex.set(idx+1);
        if (fullyTraversedSceneList().get()){
            progIndex.set(programme.size()-1);
        }else
            progIndex.set(sceneIndex.get());
    }

    public void executeAction(){
        Action action = programme.getActionProg(sceneIndex.get());
        if(action != null){
            switch (action) {
                case ActionAvancer actionAvancer -> sceneActions.avancer(actionAvancer.getMesure());
                case ActionTournerGauche actionTournerGauche -> sceneActions.tourner((getCursor().getAngle() + actionTournerGauche.getMesure())%360);
                case ActionTournerDroite actionTournerDroite -> sceneActions.tourner((getCursor().getAngle() - actionTournerDroite.getMesure() + 360)%360);
                default -> {
                }
            }
        }
        getNext();
    }
    public BooleanBinding existIndex(){
        return Bindings.createBooleanBinding(()->{
            return programme.existIndex(progIndex.get());
        },progIndex);
    }


    public List<Line> getLines(){
        return sceneActions.getLines();
    }

    public BooleanBinding fullyTraversedSceneList(){
        return Bindings.createBooleanBinding(()-> sceneIndex.get() == programme.size(), sceneIndex,getActionsProg());
    }

    public Cursor getCursor(){
        return sceneActions.getCursor();
    }

}