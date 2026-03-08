package scratch.viewmodel;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.*;
import java.util.List;


public class ActionViewModel {

    private final Program program;
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
    private BooleanProperty resetCanvasProperty = new SimpleBooleanProperty(false);

    public BooleanProperty getResetCanvasProperty() {
        return resetCanvasProperty;
    }

    public void setResetCanvasProperty() {
        resetCanvasProperty.set(!resetCanvasProperty.get());
    }

    public ObjectProperty<Action> actionSelectedProperty() {
        return actionSelected;
    }

    public ObservableList<Action> getActionsProg() {
        return program.getActionsProgList();
    }

    public ObservableList<Action> getActionPalette() {
        return FXCollections.unmodifiableObservableList(actionsPalette);
    }


    public ActionViewModel(Program program, SceneActions sceneActions) {
        this.program = program;
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
                    Action actionSelected = program.getActionProg(progIndex.get());
                    if (actionSelected != null) {
                        return actionSelected.getDetailsName();
                    }
                    return " No action selected ";
                }, progIndex ,getActionsProg()
        );

    }

    public StringBinding getSelectedActionProgDetailsUnity(){
        return Bindings.createStringBinding(() -> {
                    Action actionSelected = program.getActionProg(progIndex.get());
                    if (actionSelected != null) {
                        return actionSelected.getUnity();
                    }
                    return "";
                }, progIndex , getActionsProg()
        );
    }

    public void addPaletteToProg() {
        int idx = paletteIndexProperty().get();
        if (canAddToProgram().get())
            program.addAction(idx);
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
                mesureProperty.set(String.valueOf(n.getMeasure()));
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
                    action.setMeasure(val);
                    isvalidMesureProperty.set(action.isValidMeasure(val));
                }
            }catch (NumberFormatException e){
                isvalidMesureProperty.set(false);
            }

        });

    }
    public void removeAction() {
        program.removeAction(progIndex.get());
    }

    public void clearAllActionsProg() {
        program.clear();
    }

    public BooleanBinding isEmptyActionProg() {
        return Bindings.createBooleanBinding(program::isEmpty, program.getActionsProgList());
    }

    public void duplicateAction() {
        program.dupplicate(progIndex.get());
    }

    public BooleanBinding isSelectedActionProg() {
        return progIndex.greaterThanOrEqualTo(0);
    }

    public BooleanBinding canMoveUp() {
       return progIndex.greaterThan(0);
    }

    public BooleanBinding canMoveDown() {
        return Bindings.createBooleanBinding(() -> {
            return program.canMoveDown(progIndex.get());
        },progIndex,getActionsProg());
    }

    public void moveUp(){
        if (canMoveUp().get()) {
            program.up(progIndex.get());
            progIndex.set(progIndex.get() - 1);
        }
    }

    public void moveDown(){
        if (canMoveDown().get()) {
            program.down(progIndex.get());
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
        reinitialiserListeScene();
        sceneActions.setCurrentActionStylo(true);
        setHasChargerScene(true);
    }

    public void executer(){
        if(!program.isEmpty())
            progIndex.set(0);
    }

    public void reinitialiserListeScene(){
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
            progIndex.set(program.size()-1);
        }else
            progIndex.set(sceneIndex.get());
    }

    public void executeAction(){
        Action action = program.getActionProg(sceneIndex.get());
        if(action != null){
            switch (action) {
                case ActionMoveForward actionMoveForward -> sceneActions.moveForward(actionMoveForward.getMeasure());
                case ActionTurnLeft actionTurnLeft -> sceneActions.turn((getCursor().getAngle() + actionTurnLeft.getMeasure())%360);
                case ActionTurnRight actionTournerDroite -> sceneActions.turn((getCursor().getAngle() - actionTournerDroite.getMeasure() + 360)%360);
                case ActionStylo actionStylo -> sceneActions.changeEtatStylo();
                default -> {
                    return ;
                }
            }
        }
        getNext();
    }
    public BooleanBinding existIndex(){
        return Bindings.createBooleanBinding(()->{
            return program.existIndex(progIndex.get());
        },progIndex);
    }


    public List<Line> getLines(){
        return sceneActions.getLines();
    }

    public BooleanBinding fullyTraversedSceneList(){
        return Bindings.createBooleanBinding(()-> sceneIndex.get() == program.size(), sceneIndex,getActionsProg());
    }

    public Cursor getCursor(){
        return sceneActions.getCursor();
    }

    public void generateActionAndAddToProg(String actionType,int actionMesure){
       Action action = switch (actionType){
            case "MOVE_FORWARD" -> new ActionMoveForward(actionMesure);
            case "TURN_RIGHT" -> new ActionTurnRight(actionMesure);
            case "TURN_LEFT" -> new ActionTurnLeft(actionMesure);
            case "PEN_UP" -> new ActionStylo(false);
            case "PEN_DOWN" -> new ActionStylo(true);
            default -> null;
        };
       getActionsProg().add(action);
    }

    public BooleanBinding canAddToProgram() {
        return paletteIndex.greaterThanOrEqualTo(0);
    }



}