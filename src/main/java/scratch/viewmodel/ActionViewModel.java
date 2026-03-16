package scratch.viewmodel;

import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scratch.model.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class ActionViewModel {

    private final Program program;
    private final SceneActions sceneActions;
    private ObservableList<Action> actionsPalette = Actions.getActions();
    private IntegerProperty paletteIndex = new SimpleIntegerProperty(-1);
    private IntegerProperty progIndex = new SimpleIntegerProperty(-1);
    private StringProperty mesureProperty = new SimpleStringProperty();
    private StringProperty variableNameProperty = new SimpleStringProperty();
    private BooleanProperty isvalidMesureProperty = new SimpleBooleanProperty(true);
    private BooleanProperty isDetailableProperty = new SimpleBooleanProperty(false);
    private ObjectProperty<Action> actionSelected = new SimpleObjectProperty<>();
    private BooleanProperty hasChargerScene = new SimpleBooleanProperty(false);
    private IntegerProperty sceneIndex = new SimpleIntegerProperty(0);
    private BooleanProperty resetCanvasProperty = new SimpleBooleanProperty(false);
    private boolean isSelected = false;
    private BooleanProperty isValidVariableNameProperty = new SimpleBooleanProperty(true);


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


    public StringProperty getMesureProperty() {
        return mesureProperty;
    }

    // val   autre var
    public void listeners() {
        actionSelectedProperty().addListener((obs, o, n) -> {
            // flag pour la selection
            isSelected = true;
            if (n != null) {
                mesureProperty.set(String.valueOf(n.getMeasure()));
                if (n instanceof VariableDeclaration vD) {
                    variableNameProperty.set(vD.getNameVariable());
                } else
                    variableNameProperty.set("");
                this.setIsDetailableProperty(n.isDetailable());
            } else {
                mesureProperty.setValue("");
                this.setIsDetailableProperty(false);
            }
            isvalidMesureProperty.set(true);
            //remet la selection a faux
            isSelected = false;
        });

        mesureProperty.addListener((obs, o, n) -> {
            if (n.isEmpty()) {
                isvalidMesureProperty.set(false);
                return;
            }
            try {
                Action action = actionSelected.get();
                if (action != null) {
                    int val = Integer.parseInt(n);
                    action.setMeasure(val);
                    isvalidMesureProperty.set(action.isValidMeasure(val));
                }
            } catch (NumberFormatException e) {
                isvalidMesureProperty.set(false);
            }

        });

        variableNameProperty.addListener((observableValue, oldName, newName) -> {
            // si on est entrain de selectionner on fait rien
            if (isSelected) return;

            // si rien ne change on fait rien
            if (newName == null || newName.equals(oldName)) return;

            VariableDeclaration vD = (VariableDeclaration) actionSelected.get();

            isValidVariableNameProperty.set(vD.isValidVariableName(newName));
            Map<String, Variable> map = Variable.variableMap;
            boolean alreadyExist = map.containsKey(newName);
            // le nouveau nom est vide on supp l'ancien
            if (newName.isEmpty()) {
                if (oldName != null && !oldName.isEmpty()) {
                    map.remove(oldName);
                }
                vD.setNameVariable(newName);
                return;
            }

            // mettre a jour le map
            if (oldName != null && !oldName.isEmpty() && map.get(oldName) == (vD)) {
                map.remove(oldName);
            }
            // si ca existe et que ca m'appartient pas
            if (alreadyExist && map.get(newName) != vD) {
                vD.setNameVariable(newName);
                return;
            }


            if (vD.isValidVariableName(newName)) {
                vD.setNameVariable(newName);
                vD.addToVariableMap();
            }
        });
    }

    public void removeAction() {
        Action selected = program.getActionProg(progIndex.get());
        if (selected instanceof VariableDeclaration vD) {
            Variable.variableMap.remove(vD.getNameVariable());
        }
        program.removeAction(progIndex.get());

    }

    public void clearAllActionsProgAndVariables() {
        program.clear();
        Variable.variableMap.clear();
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


    public BooleanBinding hasAnomalies() {
        return Bindings.createBooleanBinding(() -> {
            return hasDuplicate() || isEmptyActionProg().get();
        }, getActionsProg());
    }

    public BooleanBinding hasActionsProgChange() {
        return Bindings.createBooleanBinding(() -> {
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
        sceneActions.resetLines();
        sceneActions.setCurrentActionPenDown(true);
    }

    private boolean hasDuplicate() {
        boolean abaisser = true;
        for (Action action : getActionsProg()) {
            if (action instanceof ActionPen actionPen) {
                if (actionPen.getPenDowStatus() == abaisser)
                    return true;
                else
                    abaisser = actionPen.getPenDowStatus();
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

        sceneIndex.set(idx + 1);
        if (fullyTraversedSceneList().get()) {
            progIndex.set(program.size() - 1);
        } else
            progIndex.set(sceneIndex.get());
    }

    public void executeAction() {
        Action action = program.getActionProg(sceneIndex.get());
        if (action != null) {//
            action.executeAction(sceneActions);
        }
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

    public void generateActionFromFileAndAddToProg(String actionType, int actionMesure) {
        Action action = switch (actionType) {
            case "MOVE_FORWARD" -> new ActionMoveForward(actionMesure);
            case "TURN_RIGHT" -> new ActionTurnRight(actionMesure);
            case "TURN_LEFT" -> new ActionTurnLeft(actionMesure);
            case "PEN_UP" -> new ActionPen(false);
            case "PEN_DOWN" -> new ActionPen(true);
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
        sceneActions.getCursor().getPosition().setX(canvasWidth / 2);
        sceneActions.getCursor().getPosition().setY(canvasHeight / 2);
        sceneActions.getCursor().setAngle(0);
    }

    public void loadFile(File file) {
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();

                int separatorIndex = line.indexOf(";");
                int actionMeasure = Integer.parseInt(
                        line.substring(separatorIndex + 1)
                );

                String actionType = line.substring(0, separatorIndex);

                generateActionFromFileAndAddToProg(actionType, actionMeasure);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public BooleanBinding isVariableDeclaration() {
        return Bindings.createBooleanBinding(() -> {
                    return actionSelected.get() instanceof VariableDeclaration || actionSelected.get() instanceof VariableAssignation;
                }, progIndex
        );
    }

    public StringProperty getVariableNameProperty() {
        return variableNameProperty;
    }




}