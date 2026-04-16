package scratch.viewmodel;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import scratch.model.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class ActionViewModel {

    private final Program program;
    private final SceneActions sceneActions;
    private ObservableList<Action> actionsPalette = Actions.getActions();
    private IntegerProperty paletteIndex = new SimpleIntegerProperty(-1);
    private IntegerProperty progIndex = new SimpleIntegerProperty(-1);
    private StringProperty measureProperty = new SimpleStringProperty();
    private StringProperty variableNameProperty = new SimpleStringProperty();
    private BooleanProperty isvalidMesureProperty = new SimpleBooleanProperty(true);
    private BooleanProperty isDetailableProperty = new SimpleBooleanProperty(false);
     BooleanProperty hasChargerScene = new SimpleBooleanProperty(false);
    private IntegerProperty sceneIndex = new SimpleIntegerProperty(0);
    private BooleanProperty resetCanvasProperty = new SimpleBooleanProperty(false);
    private boolean isSelected = false;
    private BooleanProperty isValidVariableNameProperty = new SimpleBooleanProperty(true);
    private BooleanProperty hasRuntimeError = new SimpleBooleanProperty(false);
    private boolean isExecuting = false;
    private final ObservableList<Map.Entry<String, Variable>> variableList = FXCollections.observableArrayList();
    private final Map<Integer,ActionRepeat> startAndCountMap = new HashMap<>();
    public ObservableList<Map.Entry<String, Variable>> getVariableList() {
        return variableList;
    }
    public void refreshVariableTable() {
        variableList.setAll(Variable.variableMap.entrySet());
    }

    public void removeAllVariableTable(){
        variableList.clear();
    }
    public BooleanProperty isValidVariableNameProperty() {
        return isValidVariableNameProperty;
    }


    public BooleanProperty getResetCanvasProperty() {
        return resetCanvasProperty;
    }

    public void setResetCanvasProperty() {
        resetCanvasProperty.set(!resetCanvasProperty.get());
    }


    public BooleanProperty hasRuntimeError() {
        return hasRuntimeError;
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
                }, progIndex, getActionsProg()
        );

    }

    public StringBinding getSelectedActionProgDetailsUnity() {
        return Bindings.createStringBinding(() -> {
                    Action actionSelected = program.getActionProg(progIndex.get());
                    if (actionSelected != null) {
                        return actionSelected.getUnity();
                    }
                    return "";
                }, progIndex, getActionsProg()
        );
    }

    public void addPaletteToProg() {
        int idx = paletteIndexProperty().get();
        int idxProg = progIndex.get();
        if (canAddToProgram().get()) {
            if (idxProg == -1) {
                program.addAction(idx, program.size());
                return;
            }
            program.addAction(idx, idxProg + 1);
        }
    }

    public void setIsDetailableProperty(boolean isDetailableProperty) {
        this.isDetailableProperty.set(isDetailableProperty);
    }

    public BooleanProperty IsDetailableProperty() {
        return isDetailableProperty;
    }


    public StringProperty getMeasureProperty() {
        return measureProperty;
    }

    // val   autre var
    public void listeners() {
        progIndex.addListener((obs, o, newVal) -> {
            // flag pour la selection
            isSelected = true;
            if (newVal != null && newVal.intValue() != -1) {
                Action n = getAction(newVal.intValue());
                if (n != null) {
                    measureProperty.set((String.valueOf(n.getMeasure()).equals("0")) ? "" : String.valueOf(n.getMeasure()));
                    variableNameProperty.set(n.getNameVariable());
                    this.setIsDetailableProperty(n.isDetailable());
                }
            } else {
                measureProperty.setValue("");
                this.setIsDetailableProperty(false);
            }
            isvalidMesureProperty.set(true); // pour stylo//remet la selection a faux
            isSelected = false;
        });

        measureProperty.addListener((obs, o, n) -> {
            if (isSelected) return;
            if (n.isEmpty()) {
                isvalidMesureProperty.set(false);
                return;
            }
            Action currentAction = getAction(progIndex.get());
            if (currentAction == null) return;
            boolean success = program.updateActionFromUI(currentAction.getNameVariable().trim(), progIndex.get(), n.trim());
            isvalidMesureProperty.set(success);
        });

        variableNameProperty.addListener((observableValue, oldName, newName) -> {
            // si on est entrain de selectionner on fait rien
            if (isSelected) return;
            System.out.println(Variable.variableMap.size());
            boolean succes = program.updateActionFromUI(newName.trim(), progIndex.get(), !program.getActionProg(progIndex.get()).getMeasure().equals("0") ? program.getActionProg(progIndex.get()).getMeasure() : "0");
            isValidVariableNameProperty.set(succes);
        });


    }

    public void removeAction() {
        program.removeAction(progIndex.get());

        if (progIndex.get() == 0) {
            progIndex.set(-1);
            progIndex.set(0);
        }

    }

    public void clearAllActionsProgAndVariables() {
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
        }, progIndex, getActionsProg());
    }

    public void moveUp() {
        if (canMoveUp().get()) {
            program.up(progIndex.get());
            progIndex.set(progIndex.get() - 1);
        }
    }

    public void moveDown() {
        if (canMoveDown().get()) {
            program.down(progIndex.get());
        }
    }

    public BooleanProperty isValidMesureProperty() {
        return isvalidMesureProperty;
    }

    public void increment() {
        if (canIncrDecr().get()) {
            program.increment(progIndex.get());
            measureProperty.set(program.getActionProg(progIndex.get()).getMeasure());
        }
    }

    public void decrement() {
        if (canIncrDecr().get()) {
            program.decrement(progIndex.get());
            measureProperty.set(program.getActionProg(progIndex.get()).getMeasure());
        }
    }

    // program has anomalies
    // consequences => btn charger desactiver
    // when => duplication pen || listProgEmpty || have something devant declaration !
    public BooleanBinding hasAnomalies() {
        return Bindings.createBooleanBinding(() -> {
            return program.hasAnomalies() || program.isEmpty() || !isvalidMesureProperty.get() || !isValidVariableNameProperty.get() || hasNameVariableUsingByRepeatInScope();
        }, measureProperty,getActionsProg(), isvalidMesureProperty, isValidVariableNameProperty);
    }

    public BooleanBinding hasProgChange(){
        return Bindings.createBooleanBinding(()->{
            program.indentAction();
            program.buildRepeatChildren();
            if (hasChargerScene.get()) {
                if (!isExecuting) {
                    hasChargerScene.set(false);
                    return true;
                }
            }
            return false;
        },getActionsProg());
    }


    public void charger() {
        reinitialiserListeScene();
        sceneActions.setCurrentActionPenDown(true);
        setHasChargerScene(true);
    }

    public void executer() {
        Variable.clear();
        if (!program.isEmpty()){
            progIndex.set(-1);
            progIndex.set(0);
            sceneIndex.setValue(0);
        }
    }

    public void reinitialiserListeScene() {
        progIndex.set(-1);
        measureProperty.set("");
        sceneIndex.set(0);
        sceneActions.reset();
        hasRuntimeError.setValue(false);
        startAndCountMap.clear();
        getActionsProg().stream()
                .filter(action -> action.hasErrorProperty().get())
                .forEach(action -> action.setHasError(false));
    }


    public void setHasChargerScene(boolean hasChargerScene) {
        this.hasChargerScene.set(hasChargerScene);
    }

    public BooleanProperty hasChargerSceneProperty() {
        return hasChargerScene;
    }

    public void getNext() {
        int idx = sceneIndex.get();
        sceneIndex.set(idx + 1);
        if (fullyTraversedSceneList().get()) {
            progIndex.set(program.size() - 1);
        } else
            progIndex.set(sceneIndex.get());

    }
    public void executeAction() {
        isExecuting = true;
        Action action = program.getActionProg(sceneIndex.get());
        boolean success = sceneActions.executeAction(action);

        action.setHasError(!success);
        hasRuntimeError.set(!success);
        if (success) {
            if (action.getActionType() == ActionType.REPEAT) {
                ActionRepeat repeat = (ActionRepeat) action;
                repeat.resetNbRead(); // remet le compteur à sa valeur initiale
                startAndCountMap.put(sceneIndex.get(), repeat);
                //r 2fois  {0,2}
                //   r  2fois  {1,2}
                //     av
                //     av
                //
                //   er        4
                //er

                //r 2fois  {0,2}
                //   r  2fois  {1,2} 19
                //     av 17
                //     av
                //
                //   er        4
                //er
                getNext();
            } else if (action.getActionType() == ActionType.ENDREPEAT) {
                ActionRepeat repeat = getMyRepeat(sceneIndex.get());
                int repeatStart = getStart(repeat); //16
                if (repeat.getNbRead() > 1) { //2
                    repeat.setNbRead(repeat.getNbRead()-1);
                    sceneIndex.set(repeatStart + 1);
                    progIndex.set(sceneIndex.get());
                } else {
                    getNext();
                }
            } else {
                getNext();
            }
            refreshVariableTable();
        }
        isExecuting = false;
    }

    public ActionRepeat getMyRepeat(int idx){
        for (Integer a:startAndCountMap.keySet()){
            if (idx == (a + getAllchildren(startAndCountMap.get(a)))){
                return startAndCountMap.get(a);
            }
        }
        return null;
    }

    public int getAllchildren(ActionRepeat actionRepeat){
        int size = 1;
        for (Action a : actionRepeat.getChildren()){
            if (a.getActionType() == ActionType.REPEAT){
                size += 1;
                size += getAllchildren((ActionRepeat) a);
            }else
                size += 1;
        }
        System.out.println(size);
        return size;
    }

    public int getStart(Action a){
        for(Integer start : startAndCountMap.keySet()){
            if (startAndCountMap.get(start).equals(a))
                return start;
        }
        return -1;
    }

    public BooleanBinding existIndex() {
        return Bindings.createBooleanBinding(() -> {
            return program.existIndex(progIndex.get());
        }, progIndex);
    }


    public List<Line> getLines() {
        return sceneActions.getLines();
    }

    public BooleanBinding fullyTraversedSceneList() {
        return Bindings.createBooleanBinding(() -> sceneIndex.get() == program.size(), sceneIndex, getActionsProg());
    }

    public Cursor getCursor() {
        return sceneActions.getCursor();
    }

    public void generateActionFromFileAndAddToProg(String actionType, String actionMeasureOrNameVariable, String reste) {
        Action action = switch (actionType) {
            case "MOVE_FORWARD" -> new ActionMoveForward(actionMeasureOrNameVariable);
            case "TURN_RIGHT" -> new ActionTurnRight(actionMeasureOrNameVariable);
            case "TURN_LEFT" -> new ActionTurnLeft(actionMeasureOrNameVariable);
            case "PEN_UP" -> new ActionPen(false);
            case "PEN_DOWN" -> new ActionPen(true);
            case "VARIABLE_ASSIGNATION" -> {
                VariableAssignation va = new VariableAssignation();
                va.setMeasure(reste);
                va.setNameVariable(actionMeasureOrNameVariable);
                // met à jour la valeur dans la map si la variable existe
                if (Variable.variableMap.containsKey(actionMeasureOrNameVariable)) {
                    Variable.variableMap.get(actionMeasureOrNameVariable).setMeasure(reste);
                }
                yield va;
            }
            case "VARIABLE_DECLARATION" -> {
                VariableDeclaration vd = new VariableDeclaration();
                vd.setNameVariable(actionMeasureOrNameVariable);
                vd.addToVariableMap();
                yield vd;
            }
            default -> null;
        };
        if (action != null) {
            getActionsProg().add(action);
        }
    }

    public BooleanBinding canAddToProgram() {
        return paletteIndex.greaterThanOrEqualTo(0);
    }

    public void resetCursorToCenter(double canvasWidth, double canvasHeight) {
        sceneActions.resetCursorToCenter(canvasWidth, canvasHeight);
    }

    public void loadFile(File file) {
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                String actionType = "";
                String actionMeasure = "";
                String actionNameVariable = "";
                String reste = "";
                // trouver le premier séparateur
                int firstIndex = line.indexOf(";");
                if (firstIndex != -1) {
                    actionType = line.substring(0, firstIndex);
                    // chercher le deuxième en commençant APRES le premier (firstIndex + 1)
                    int secondIndex = line.indexOf(";", firstIndex + 1);
                    if (secondIndex != -1) {
                        // Si on trouve un deuxième ;
                        actionMeasure = line.substring(firstIndex + 1, secondIndex);
                        reste = line.substring(secondIndex + 1);
                    } else {
                        // S'il n'y a qu un seul ;
                        actionMeasure = line.substring(firstIndex + 1);
                    }
                }
                generateActionFromFileAndAddToProg(actionType, actionMeasure, reste);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StringBinding getSeparatorLabel() {
        return Bindings.createStringBinding(() -> {
            if (isAssignation().get())
                return " valeur : ";
            return " de ";
        }, progIndex);
    }

    public BooleanBinding textFieldValueVariableRules() {
        return Bindings.createBooleanBinding(() -> {
                    return program.isDeclaration(progIndex.get()) || program.isAssignation(progIndex.get()) || program.isIncrDecr(progIndex.get());
                }, progIndex
        );
    }

    public BooleanBinding isDeclaration() {
        return Bindings.createBooleanBinding(() -> {
                    return program.isDeclaration(progIndex.get());
                }, progIndex
        );
    }

    public BooleanBinding isEndRepeat() {
        return Bindings.createBooleanBinding(() -> {
                    return program.isEndRepeat(progIndex.get());
                }, progIndex
        );
    }

    public BooleanBinding isAssignation() {
        return Bindings.createBooleanBinding(() -> {
                    return program.isAssignation(progIndex.get());
                }, progIndex
        );
    }

    public BooleanBinding isIncrDecr() {
        return Bindings.createBooleanBinding(() -> {
                    return program.isIncrDecr(progIndex.get());
                }, progIndex
        );
    }

    public BooleanBinding canIncrDecr() {
        return Bindings.createBooleanBinding(() -> {
            return program.canIncrDecr(measureProperty.get(), progIndex.get());
        }, progIndex, getActionsProg(), measureProperty);
    }

    public StringProperty getVariableNameProperty() {
        return variableNameProperty;
    }

    public Action getAction(int idx) {
        return program.getActionProg(idx);
    }

    public BooleanBinding hasActionSelectedRuntime() {
        return Bindings.createBooleanBinding(() -> {
                    return hasRuntimeError.get() &&
                           existIndex().get() && program.getActionProg(progIndex.get()).hasErrorProperty().get();
                }, hasRuntimeError, progIndex
        );
    }

    public boolean hasNameVariableUsingByRepeatInScope(){
        for(Action a : getActionsProg()){
            if(a.getActionType()==ActionType.REPEAT){
                if (((ActionRepeat) a).hasConflictWithChildren()) {
                    return true;
                }
            }
        }
        return false;
    }
}