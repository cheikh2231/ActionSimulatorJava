package scratch.viewmodel;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import scratch.model.*;

import java.io.File;
import java.util.*;

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
    private BooleanProperty hasChargerScene = new SimpleBooleanProperty(false);
    // -1 = pas encore démarré
    private IntegerProperty sceneIndex = new SimpleIntegerProperty(-1);
    private BooleanProperty resetCanvasProperty = new SimpleBooleanProperty(false);
    // flag : vrai pendant la sélection d'une action, pour ignorer les listeners
    private boolean isSelected = false;
    private BooleanProperty isValidVariableNameProperty = new SimpleBooleanProperty(true);
    private BooleanProperty hasRuntimeError = new SimpleBooleanProperty(false);
    // flag : vrai pendant l'exécution d'une action
    private boolean isExecuting = false;
    private final ObservableList<Map.Entry<String, Variable>> variableList = FXCollections.observableArrayList();
    // associe l'index de début d'un REPEAT à son ActionRepeat
    private final Map<Integer, ActionRepeat> startAndCountMap = new HashMap<>();
    private BooleanProperty isExecutionAuto = new SimpleBooleanProperty(false);
    private DoubleProperty sliderValue = new SimpleDoubleProperty();
    private final DoubleProperty canvasHeight = new SimpleDoubleProperty();
    private final DoubleProperty canvasWidth = new SimpleDoubleProperty();

    public DoubleProperty sliderValue() {
        return sliderValue;
    }

    public DoubleProperty canvasHeight() {
        return canvasHeight;
    }

    public DoubleProperty canvasWidth() {
        return canvasWidth;
    }

    public ObservableList<Map.Entry<String, Variable>> getVariableList() {
        return variableList;
    }

    public void refreshVariableTable() {
        variableList.setAll(Variable.variableMap.entrySet());
    }

    // flag : vrai pendant le chargement d'un fichier, pour ignorer les listeners
    public boolean isLoading = false;

    public void removeAllVariableTable() {
        variableList.clear();
    }

    public BooleanProperty isValidVariableNameProperty() {
        return isValidVariableNameProperty;
    }

    public BooleanProperty isExecutionAutoProperty() {
        return isExecutionAuto;
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
            return actionSelected != null ? actionSelected.getDetailsName() : " No action selected ";
        }, progIndex, getActionsProg());
    }

    public StringBinding getSelectedActionProgDetailsUnity() {
        return Bindings.createStringBinding(() -> {
            Action actionSelected = program.getActionProg(progIndex.get());
            return actionSelected != null ? actionSelected.getUnity() : "";
        }, progIndex, getActionsProg());
    }

    public void addPaletteToProg() {
        if (canAddToProgram().get()) {
            program.addAction(paletteIndex.get(), progIndex.get());
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

    public void listeners() {
        // Quand on sélectionne une action dans la liste, on met à jour les champs de détail
        progIndex.addListener((obs, o, newVal) -> {
            isSelected = true;
            if (newVal != null && newVal.intValue() != -1) {
                Action n = getAction(newVal.intValue());
                if (n != null) {
                    // "0" est la valeur par défaut → on affiche vide
                    measureProperty.set(n.getMeasure().equals("0") ? "" : n.getMeasure());
                    variableNameProperty.set(n.getNameVariable());
                    this.setIsDetailableProperty(n.isDetailable());
                }
            } else {
                measureProperty.setValue("");
                this.setIsDetailableProperty(false);
            }
            isvalidMesureProperty.set(true);
            isSelected = false;
        });

        // Quand l'utilisateur modifie la mesure dans le TextField
        measureProperty.addListener((obs, o, n) -> {
            if (isSelected || isLoading) return;
            if (n.isEmpty()) {
                isvalidMesureProperty.set(false);
                return;
            }
            Action currentAction = getAction(progIndex.get());
            if (currentAction == null) return;
            boolean success = program.updateActionFromUI(currentAction.getNameVariable().trim(), progIndex.get(), n.trim());
            isvalidMesureProperty.set(success);
        });

        // Quand l'utilisateur modifie le nom de variable dans le TextField
        variableNameProperty.addListener((observableValue, oldName, newName) -> {
            if (isSelected || isLoading) return;
            if (progIndex.get() != -1) {
                String measure = !program.getActionProg(progIndex.get()).getMeasure().equals("0")
                        ? program.getActionProg(progIndex.get()).getMeasure() : "0";
                boolean succes = program.updateActionFromUI(newName.trim(), progIndex.get(), measure);
                isValidVariableNameProperty.set(succes);
            }
        });

        // Basculement manuel ↔ automatique
        isExecutionAutoProperty().addListener((observableValue, aBoolean, isAuto) -> {
            if (!isAuto)
                stopAutomaticExec();
            else
                automaticExecution();
        });

        // Changement de vitesse du slider → relance l'exécution automatique
        sliderValue.addListener((observableValue, number, t1) -> automaticExecution());

        // Toute modification de la liste du programme réinitialise l'état d'exécution
        getActionsProg().addListener((ListChangeListener<Action>) change -> {
            stopAutomaticExec();
            program.indentAction();
            program.buildRepeatChildren();
            hasRuntimeError.setValue(false);
            getActionsProg().stream()
                    .filter(action -> action.hasErrorProperty().get())
                    .forEach(action -> action.setHasError(false));
            if (hasChargerScene.get() && !isExecuting) {
                hasChargerScene.set(false);
            }
        });
    }

    public void removeAction() {
        program.removeAction(progIndex.get());
        // force le rafraîchissement si on supprime le premier élément
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
        return Bindings.createBooleanBinding(() ->
                program.canMoveDown(progIndex.get()), progIndex, getActionsProg());
    }

    public void moveUp() {
        if (canMoveUp().get()) {
            program.up(progIndex.get());
            progIndex.set(progIndex.get() - 1);
        }
    }

    public void moveDown() {
        if (canMoveDown().get()) {
            Action selected = getAction(progIndex.get());
            program.down(progIndex.get());
            progIndex.set(getActionsProg().indexOf(selected));
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

    // Anomalie = stylo dupliqué, liste vide, déclaration mal placée, mesure/nom invalide
    public BooleanBinding hasAnomalies() {
        return Bindings.createBooleanBinding(() ->
                        program.hasAnomalies() || program.isEmpty()
                                || !isvalidMesureProperty.get() || !isValidVariableNameProperty.get(),
                measureProperty, getActionsProg(), isvalidMesureProperty, isValidVariableNameProperty);
    }

    public void charger() {
        reinitialiserListeScene();
        sceneActions.setCurrentActionPenDown(true);
        setHasChargerScene(true);
        removeAllVariableTable();
    }

    public void executer() {
        reinitialiserListeScene();
        if (!program.isEmpty()) {
            progIndex.set(-1);
            progIndex.set(0);
            sceneIndex.setValue(0);
        }
        if (isExecutionAutoProperty().get()) {
            automaticExecution();
        }
    }

    public void reinitialiserListeScene() {
        stopAutomaticExec();
        resetCursorToCenter();
        Variable.clear();
        removeAllVariableTable();
        sceneIndex.set(-1);
        progIndex.set(-1);
        measureProperty.set("");
        sceneActions.reset();
        hasRuntimeError.setValue(false);
        startAndCountMap.clear();
        variableNameProperty.set("");
        isvalidMesureProperty.set(true);
        isValidVariableNameProperty.set(true);
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
        sceneIndex.set(sceneIndex.get() + 1);
        // si on a tout parcouru, on pointe sur la dernière action
        if (fullyTraversedSceneList().get())
            progIndex.set(program.size() - 1);
        else
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
                // on mémorise le REPEAT et son nombre d'itérations restantes
                ActionRepeat repeat = (ActionRepeat) action;
                repeat.resetNbRead();
                startAndCountMap.put(sceneIndex.get(), repeat);
                getNext();
            } else if (action.getActionType() == ActionType.ENDREPEAT) {
                ActionRepeat repeat = getMyRepeat(sceneIndex.get());
                int repeatStart = getStart(repeat);
                if (repeat.getNbRead() > 1) {
                    // on décrémente et on retourne au premier enfant du REPEAT
                    repeat.setNbRead(repeat.getNbRead() - 1);
                    sceneIndex.set(repeatStart + 1);
                    progIndex.set(sceneIndex.get());
                } else {
                    // répétition terminée, on passe à la suite
                    getNext();
                }
            } else {
                getNext();
            }
            refreshVariableTable();
        }
        isExecuting = false;
    }

    // Retrouve le REPEAT associé à un END_REPEAT à partir de son index
    public ActionRepeat getMyRepeat(int idx) {
        for (Integer a : startAndCountMap.keySet()) {
            if (idx == (a + getAllchildren(startAndCountMap.get(a)))) {
                return startAndCountMap.get(a);
            }
        }
        return null;
    }

    // Compte récursivement tous les enfants d'un REPEAT (y compris les REPEAT imbriqués)
    public int getAllchildren(ActionRepeat actionRepeat) {
        int size = 1; // le END_REPEAT lui-même
        for (Action a : actionRepeat.getChildren()) {
            if (a.getActionType() == ActionType.REPEAT) {
                size += 1;
                size += getAllchildren((ActionRepeat) a);
            } else {
                size += 1;
            }
        }
        return size;
    }

    // Retrouve l'index de début d'un ActionRepeat dans startAndCountMap
    public int getStart(Action a) {
        for (Integer start : startAndCountMap.keySet()) {
            if (startAndCountMap.get(start).equals(a))
                return start;
        }
        return -1;
    }

    public BooleanBinding existIndex() {
        return Bindings.createBooleanBinding(() ->
                program.existIndex(progIndex.get()), progIndex);
    }

    public List<Line> getLines() {
        return sceneActions.getLines();
    }

    public BooleanBinding fullyTraversedSceneList() {
        return Bindings.createBooleanBinding(() ->
                sceneIndex.get() == program.size(), sceneIndex, getActionsProg());
    }

    public Cursor getCursor() {
        return sceneActions.getCursor();
    }

    public void generateActionFromFileAndAddToProg(String actionType, String actionMeasureOrNameVariable, String reste) {
        reinitialiserListeScene();
        Action action = switch (actionType) {
            case "MOVE_FORWARD" -> new ActionMoveForward(actionMeasureOrNameVariable);
            case "TURN_RIGHT" -> new ActionTurnRight(actionMeasureOrNameVariable);
            case "TURN_LEFT" -> new ActionTurnLeft(actionMeasureOrNameVariable);
            case "PEN_UP" -> new ActionPen(false);
            case "PEN_DOWN" -> new ActionPen(true);
            case "VAR_ASSIGNMENT" -> {
                VariableAssignation va = new VariableAssignation();
                va.setMeasure(reste);
                va.setNameVariable(actionMeasureOrNameVariable);
                yield va;
            }
            case "VAR_DECLARATION" -> {
                VariableDeclaration vd = new VariableDeclaration();
                vd.setNameVariable(actionMeasureOrNameVariable);
                yield vd;
            }
            case "REPEAT" -> {
                ActionRepeat aR = new ActionRepeat();
                aR.setMeasure(actionMeasureOrNameVariable);
                yield aR;
            }
            case "END_REPEAT" -> new ActionEndRepeat();
            case "INCREMENT_VARIABLE" -> {
                VariableIncrDecr ai = new VariableIncrDecr();
                ai.setMeasure(reste);
                ai.setNameVariable(actionMeasureOrNameVariable);
                yield ai;
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

    public void resetCursorToCenter() {
        sceneActions.resetCursorToCenter(canvasWidth.get(), canvasHeight.get());
    }

    public void loadFile(File file) {
        isLoading = true;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                String actionType = "", actionMeasure = "", reste = "";

                int firstIndex = line.indexOf(";");
                if (firstIndex != -1) {
                    actionType = line.substring(0, firstIndex);
                    int secondIndex = line.indexOf(";", firstIndex + 1);
                    if (secondIndex != -1) {
                        // deux séparateurs : TYPE;nomVariable;valeur
                        actionMeasure = line.substring(firstIndex + 1, secondIndex);
                        reste = line.substring(secondIndex + 1);
                    } else {
                        // un seul séparateur : TYPE;mesure
                        actionMeasure = line.substring(firstIndex + 1);
                    }
                }
                generateActionFromFileAndAddToProg(actionType, actionMeasure, reste);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        isLoading = false;
    }

    // Libellé du séparateur entre nom de variable et valeur
    public StringBinding getSeparatorLabel() {
        return Bindings.createStringBinding(() ->
                isAssignation().get() ? " valeur : " : " de ", progIndex);
    }

    // Vrai si l'action sélectionnée implique un nom de variable (déclaration, assignation, incrément)
    public BooleanBinding textFieldValueVariableRules() {
        return Bindings.createBooleanBinding(() ->
                program.isDeclaration(progIndex.get())
                        || program.isAssignation(progIndex.get())
                        || program.isIncrDecr(progIndex.get()), progIndex);
    }

    public BooleanBinding isDeclaration() {
        return Bindings.createBooleanBinding(() -> program.isDeclaration(progIndex.get()), progIndex);
    }

    public BooleanBinding isEndRepeat() {
        return Bindings.createBooleanBinding(() -> program.isEndRepeat(progIndex.get()), progIndex);
    }

    public BooleanBinding isAssignation() {
        return Bindings.createBooleanBinding(() -> program.isAssignation(progIndex.get()), progIndex);
    }

    public BooleanBinding isIncrDecr() {
        return Bindings.createBooleanBinding(() -> program.isIncrDecr(progIndex.get()), progIndex);
    }

    public BooleanBinding canIncrDecr() {
        return Bindings.createBooleanBinding(() ->
                        program.canIncrDecr(measureProperty.get(), progIndex.get()),
                progIndex, getActionsProg(), measureProperty);
    }

    public StringProperty getVariableNameProperty() {
        return variableNameProperty;
    }

    public Action getAction(int idx) {
        return program.getActionProg(idx);
    }

    public BooleanBinding hasActionSelectedRuntime() {
        return Bindings.createBooleanBinding(() ->
                        hasRuntimeError.get() && existIndex().get()
                                && program.getActionProg(progIndex.get()).hasErrorProperty().get(),
                hasRuntimeError, progIndex);
    }

    private Timeline timeline;

    public void automaticExecution() {
        stopAutomaticExec();
        // ne démarre que si le programme est chargé, en mode auto, et qu'il reste des actions
        if (hasChargerScene.get() && isExecutionAuto.get() && program.existIndex(sceneIndex.get())) {
            timeline = new Timeline(new KeyFrame(
                    Duration.millis(sliderValue.get() * 1000),
                    event -> {
                        if (!fullyTraversedSceneList().get() && !hasRuntimeError().get())
                            executeAction();
                        else
                            stopAutomaticExec();
                    }
            ));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        }
    }

    public void stopAutomaticExec() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    public IntegerProperty sceneIndexProperty() {
        return sceneIndex;
    }

    public StringBinding getLabelVariables() {
        return Bindings.createStringBinding(() -> {
            // on arrondit à 1 décimale pour éviter les résidus flottants (ex: 1.13E-13 → 0.0)
            double x = Math.round((getCursor().getPosition().getX() - (canvasWidth.get() / 2)) * 10.0) / 10.0;
            // inversion de l'axe Y : dans JavaFX Y croît vers le bas, on affiche l'inverse
            double y = Math.round(((canvasHeight.get() / 2) - getCursor().getPosition().getY()) * 10.0) / 10.0;
            // normalisation de l'angle dans [0, 359]
            int angle = ((getCursor().getAngle() % 360) + 360) % 360;
            return String.format("Tortue : x = %.1f , y = %.1f , direction = %d °", x, y, angle);
        }, sceneIndex);
    }
}