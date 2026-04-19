package scratch.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import scratch.model.Cursor;
import scratch.model.Line;
import scratch.model.Variable;
import scratch.viewmodel.ActionViewModel;

import java.util.Map;
import java.util.Timer;


public class ActionSceneView extends VBox {
    private final ActionViewModel actionVm;
    private final Label titleLabel = new Label("Scene");
    // Canvas fixe 400x400
    private final Canvas canvas = new Canvas(400, 400);
    private final Button resetButton = new Button("Ré-initialiser");
    private final Button loadButton = new Button("Charger");
    private final Button executeButton = new Button("Executer");
    private final Button nextButton = new Button("Suivant");
    public int TURTLE_HEAD_SIZE = Cursor.TURTLE_HEAD_SIZE;
    public int TURTLE_SIZE = Cursor.TURTLE_SIZE;
    private final Circle turtleHead = new Circle(TURTLE_HEAD_SIZE);
    private Polygon turtle;
    // Pane qui contient le canvas et la tortue
    private Pane drawingPane = new Pane();
    private ToggleGroup groupeExecution = new ToggleGroup();
    private RadioButton rbAuto = new RadioButton("Exécution automatique");
    private RadioButton rbManuel = new RadioButton("Exécution manuelle");
    private Slider slider = new Slider(0.01, 5, 0.5);
    private TableView<Map.Entry<String, Variable>> variableTable = new TableView<>();
    private final Label varTitle = new Label("Variables");
    private final Label turtlePosition = new Label();

    public ActionSceneView(ActionViewModel actionVm) {
        this.actionVm = actionVm;
        drawTurtle(canvas.getWidth() / 2, canvas.getHeight() / 2, 0);
        setupCanvas();
        setupView();
        drawingPane.getChildren().addAll(canvas, turtleHead, turtle);
        this.getChildren().addAll(titleLabel, drawingPane, tableVariableContainer(), createControlButtons(), radioButtonsExecutions(), executionSpeedSlider());
        bindings();
    }

    public HBox radioButtonsExecutions() {
        rbAuto.setToggleGroup(groupeExecution);
        rbManuel.setToggleGroup(groupeExecution);
        // Manuel sélectionné par défaut
        rbManuel.setSelected(true);
        HBox hboxRadioButtons = new HBox(15);
        hboxRadioButtons.setAlignment(Pos.CENTER);
        hboxRadioButtons.getChildren().addAll(rbAuto, rbManuel);
        return hboxRadioButtons;
    }

    public Slider executionSpeedSlider() {
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(1);
        slider.setPrefWidth(400);
        return slider;
    }

    public HBox createControlButtons() {
        HBox controlButtons = new HBox(10);

        // Réinitialise le canvas et recharge le programme
        nextButton.setOnAction(actionEvent -> actionVm.executeAction());
        loadButton.setOnAction(actionEvent -> {
            resetCanvasView();
            actionVm.charger();
        });
        // Réinitialise le canvas et lance l'exécution automatique
        executeButton.setOnAction(actionEvent -> {
            resetCanvasView();
            actionVm.executer();
        });
        resetButton.setOnAction(actionEvent -> {
            resetCanvasView();
            actionVm.reinitialiserListeScene();
        });

        controlButtons.getChildren().addAll(loadButton, resetButton, executeButton, nextButton);
        controlButtons.setAlignment(Pos.CENTER);
        return controlButtons;
    }

    public void bindings() {
        // Charger visible seulement si pas encore chargé
        loadButton.disableProperty().bind(actionVm.hasAnomalies());
        loadButton.visibleProperty().bind(actionVm.hasChargerSceneProperty().not());
        loadButton.managedProperty().bind(actionVm.hasChargerSceneProperty().not());

        // Reset visible seulement si programme chargé
        resetButton.visibleProperty().bind(actionVm.hasChargerSceneProperty());
        resetButton.managedProperty().bind(actionVm.hasChargerSceneProperty());

        // Executer et Suivant sont mutuellement exclusifs
        executeButton.disableProperty().bind(actionVm.hasChargerSceneProperty().not().or(actionVm.fullyTraversedSceneList()).or(actionVm.hasRuntimeError()));
        executeButton.visibleProperty().bind(nextButton.visibleProperty().not());
        executeButton.managedProperty().bind(nextButton.visibleProperty().not());

        // Suivant visible uniquement en mode manuel, si programme chargé et pas terminé
        nextButton.visibleProperty().bind(actionVm.hasChargerSceneProperty()
                .and(actionVm.existIndex())
                .and(actionVm.fullyTraversedSceneList().not())
                .and(actionVm.hasRuntimeError().not())
                .and(actionVm.isExecutionAutoProperty().not()));
        nextButton.managedProperty().bind(actionVm.hasChargerSceneProperty()
                .and(actionVm.existIndex())
                .and(actionVm.fullyTraversedSceneList().not()));

        // Slider visible uniquement en mode automatique
        actionVm.isExecutionAutoProperty().bind(groupeExecution.selectedToggleProperty().isEqualTo(rbAuto));
        slider.visibleProperty().bind(actionVm.isExecutionAutoProperty());
        slider.managedProperty().bind(actionVm.isExecutionAutoProperty());

        // Redessine la tortue à chaque avancement du sceneIndex
        actionVm.sceneIndexProperty().addListener((observableValue, oldV, newV) -> {
            if (newV.intValue() > oldV.intValue())
                draw();
        });

        // Sync bidirectionnel : slider, dimensions canvas → ViewModel
        actionVm.sliderValue().bindBidirectional(slider.valueProperty());
        actionVm.canvasHeight().bindBidirectional(canvas.heightProperty());
        actionVm.canvasWidth().bindBidirectional(canvas.widthProperty());

        // Affichage position et angle de la tortue
        turtlePosition.textProperty().bind(actionVm.getLabelVariables());
    }

    public void setupCanvas() {
        // Bordure extérieure noire
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Grille intérieure grise (10x10 cellules)
        GraphicsContext gcInside = canvas.getGraphicsContext2D();
        gcInside.setStroke(Color.LIGHTGRAY);
        gcInside.setLineWidth(1);
        int heightCanvas = (int) canvas.getHeight();
        int cellSize = heightCanvas / 10;
        int widthCanvas = (int) canvas.getWidth();
        for (int i = cellSize; i < heightCanvas; i += cellSize)
            gcInside.strokeLine(0, i, heightCanvas, i);
        for (int i = cellSize; i < widthCanvas; i += cellSize)
            gcInside.strokeLine(i, 0, i, widthCanvas);
    }

    private void drawTurtle(double x, double y, double angle) {
        // Calcul des 3 sommets du triangle selon la position et l'angle
        double xP1 = x + TURTLE_SIZE * Math.sin(Math.toRadians(60)) * Math.cos(Math.toRadians(angle + 90)),
                yP1 = y - TURTLE_SIZE * Math.sin(Math.toRadians(60)) * Math.sin(Math.toRadians(angle + 90)),
                xP2 = x - TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.cos(Math.toRadians(angle)),
                yP2 = y + TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.sin(Math.toRadians(angle)),
                xP3 = x + TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.cos(Math.toRadians(angle)),
                yP3 = y - TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.sin(Math.toRadians(angle));

        this.turtle = new Polygon(xP1, yP1, xP2, yP2, xP3, yP3);
        this.turtle.setFill(Color.DEEPSKYBLUE);
        this.turtle.setStroke(Color.BLACK);
        // La tête (cercle) se place sur le premier sommet
        this.turtleHead.setTranslateX(xP1);
        this.turtleHead.setTranslateY(yP1);
    }

    private void drawLine() {
        // Dessine uniquement le dernier segment tracé
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        if (!actionVm.getLines().isEmpty()) {
            Line lastLine = actionVm.getLines().getLast();
            gc.strokeLine(lastLine.getStart().getX(), lastLine.getStart().getY(),
                    lastLine.getStop().getX(), lastLine.getStop().getY());
        }
    }

    private void resetCanvasView() {
        actionVm.getResetCanvasProperty().addListener((obs, oldVal, newVal) -> resetCanvasView());
        // Efface le canvas et retire la tortue
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawingPane.getChildren().removeAll(turtle, turtleHead);
        setupCanvas();
        actionVm.resetCursorToCenter();
        // Repose la tortue au centre
        drawTurtle(canvas.getWidth() / 2, canvas.getHeight() / 2, 0);
        drawingPane.getChildren().addAll(turtle, turtleHead);
    }

    private void setupView() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
    }

    private VBox tableVariableContainer() {
        VBox container = new VBox();
        HBox hbox = new HBox();
        container.setMaxHeight(150);
        container.setPadding(new Insets(10, 10, 10, 10));
        container.setBorder(new Border(new BorderStroke(
                Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)
        )));

        // Colonne Nom → nameVariableProperty de la variable
        TableColumn<Map.Entry<String, Variable>, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(e -> e.getValue().getValue().nameVariableProperty());

        // Colonne Valeur → valeur courante de la variable
        TableColumn<Map.Entry<String, Variable>, String> valueColumn = new TableColumn<>("Valeur");
        valueColumn.setCellValueFactory(e -> new SimpleStringProperty(
                String.valueOf(e.getValue().getValue().getValue(e.getValue().getValue().getMeasure()))));

        variableTable.getColumns().setAll(nomColumn, valueColumn);
        hbox.setMinWidth(500);
        nomColumn.setMinWidth(249);
        valueColumn.setMinWidth(249);
        nomColumn.setSortable(false);
        valueColumn.setSortable(false);
        variableTable.setItems(actionVm.getVariableList());

        hbox.getChildren().add(variableTable);
        container.getChildren().addAll(turtlePosition, varTitle, hbox);
        return container;
    }

    public void draw() {
        drawLine();
        drawingPane.getChildren().removeAll(turtle, turtleHead);
        double x = actionVm.getCursor().getPosition().getX();
        double y = actionVm.getCursor().getPosition().getY();
        double angle = actionVm.getCursor().getAngle();
        drawTurtle(x, y, angle);
        drawingPane.getChildren().addAll(turtle, turtleHead);
    }
}