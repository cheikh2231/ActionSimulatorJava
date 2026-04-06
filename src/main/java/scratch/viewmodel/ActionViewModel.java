package scratch.viewmodel;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.*;

import java.io.File;
import java.util.List;
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
    private ObjectProperty<Action> actionSelected = new SimpleObjectProperty<>();
    private BooleanProperty hasChargerScene = new SimpleBooleanProperty(false);
    private IntegerProperty sceneIndex = new SimpleIntegerProperty(0);
    private BooleanProperty resetCanvasProperty = new SimpleBooleanProperty(false);
    private boolean isSelected = false;
    private BooleanProperty isValidVariableNameProperty = new SimpleBooleanProperty(true);
    private BooleanProperty hasRuntimeError = new SimpleBooleanProperty(false);

    public BooleanProperty isValidVariableNameProperty() {
        return isValidVariableNameProperty;
    }


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
            isvalidMesureProperty.set(true); // pour stylo
            //remet la selection a faux
            isSelected = false;
        });

        measureProperty.addListener((obs, o, n) -> {
            if (n.isEmpty()) {
                isvalidMesureProperty.set(false);
                return;
            }
            Action currentAction = getAction(progIndex.get());
            if (currentAction == null) return;
            boolean success = program.updateVariableToMap(currentAction.getNameVariable().trim(), progIndex.get(), n.trim());
            isvalidMesureProperty.set(success);
        });

        variableNameProperty.addListener((observableValue, oldName, newName) -> {
            // si on est entrain de selectionner on fait rien
            if (isSelected) return;
            boolean succes = program.updateVariableToMap(newName.trim(), progIndex.get(), !program.getActionProg(progIndex.get()).getMeasure().equals("0") ? program.getActionProg(progIndex.get()).getMeasure() : "0");
            isValidVariableNameProperty.set(succes);
        });
    }

    public void removeAction() {
        program.removeAction(progIndex.get());

        if (progIndex.get() == 0){
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
            progIndex.set(progIndex.get() + 1);
        }
    }

    public BooleanProperty isValidMesureProperty() {
        return isvalidMesureProperty;
    }

    public void increment(){
        if(canIncrDecr().get()){
            program.increment(progIndex.get());
            measureProperty.set(program.getActionProg(progIndex.get()).getMeasure());
        }
    }

    public void decrement(){
        if(canIncrDecr().get()){
            program.decrement(progIndex.get());
            measureProperty.set(program.getActionProg(progIndex.get()).getMeasure());
        }
    }

    // program has anomalies
    // consequences => btn charger desactiver
    // when => duplication pen || listProgEmpty || have something devant declaration !
    public BooleanBinding hasAnomalies() {
        return Bindings.createBooleanBinding(() -> {
                    return program.hasAnomalies() || program.isEmpty() || !isvalidMesureProperty.get() || !isValidVariableNameProperty.get();
                },
                getActionsProg(),isvalidMesureProperty,isValidVariableNameProperty,measureProperty
        );
    }

    public BooleanBinding hasActionsProgChange() {
        return Bindings.createBooleanBinding(() -> {
            hasRuntimeError.set(false);
            if (hasChargerScene.get()) {
                hasChargerScene.set(false);
                return true;
            }
            return false;
        }, getActionsProg());
    }

    public void charger() {
        reinitialiserListeScene();
        sceneActions.setCurrentActionPenDown(true);
        setHasChargerScene(true);
    }

    public void executer() {
        if (!program.isEmpty())
            progIndex.set(0);
    }

    public void reinitialiserListeScene() {
        progIndex.set(-1);
        sceneIndex.set(0);
        sceneActions.reset();
        hasRuntimeError.setValue(false);
        getActionsProg().stream().filter(action -> action.hasErrorProperty().get()).forEach(action -> action.setHasError(false));
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
        Action action = program.getActionProg(sceneIndex.get());
        boolean success = sceneActions.executeAction(action);
        hasRuntimeError.set(!success);
        action.setHasError(!success);
        if (success)
            getNext();
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
                generateActionFromFileAndAddToProg(actionType,actionMeasure,reste);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public BooleanBinding isDeclaration() {
        return Bindings.createBooleanBinding(() -> {
                    return program.isDeclaration(progIndex.get());
                }, progIndex
        );
    }

    public BooleanBinding isAssignation() {
        return Bindings.createBooleanBinding(() -> {
                    return program.isAssignation(progIndex.get());
                }, progIndex
        );
    }
    public BooleanBinding canIncrDecr(){
        return Bindings.createBooleanBinding(() -> {
            return program.canIncrDecr(measureProperty.get(),progIndex.get());
        },progIndex, getActionsProg(), measureProperty);
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
                            progIndex.get() == sceneIndex.get() ;
                },hasRuntimeError,progIndex
        );
    }

}