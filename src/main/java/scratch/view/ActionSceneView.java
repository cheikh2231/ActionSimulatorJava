package scratch.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import scratch.model.Action;
import scratch.model.Cursor;
import scratch.model.Line;
import scratch.model.Variable;
import scratch.viewmodel.ActionViewModel;

import java.util.Map;


public class ActionSceneView extends VBox {
    private final ActionViewModel actionVm;
    private final Label titleLabel = new Label("Scene");
    private final Canvas canvas = new Canvas(500, 500);
    private final Button resetButton = new Button("Ré-initialiser");
    private final Button loadButton = new Button("Charger");
    private final Button executeButton = new Button("Executer");
    private final Button nextButton = new Button("Suivant");
    public int TURTLE_HEAD_SIZE = Cursor.TURTLE_HEAD_SIZE;
    public int TURTLE_SIZE = Cursor.TURTLE_SIZE;
    private final Circle turtleHead = new Circle(TURTLE_HEAD_SIZE);
    private Polygon turtle;
    private Pane drawingPane = new Pane();
    TableView<Map.Entry<String,Variable>> variableTable = new TableView<>();

    public ActionSceneView(ActionViewModel actionVm) {
        this.actionVm = actionVm;
        drawTurtle(canvas.getWidth() / 2, canvas.getHeight() / 2, 0);
        setupCanvas();
        setupView();
        drawingPane.getChildren().addAll(canvas, turtleHead, turtle);
        this.getChildren().addAll(titleLabel, drawingPane,tableVariableContainer(), createControlButtons());
        bindings();


    }


    public HBox createControlButtons() {
        HBox controlButtons = new HBox(10);
        nextButton.setOnAction(actionEvent -> {
            actionVm.executeAction();
            drawLine();
            drawingPane.getChildren().removeAll(turtle, turtleHead);
            double x = actionVm.getCursor().getPosition().getX();
            double y = actionVm.getCursor().getPosition().getY();
            double angle = (actionVm.getCursor().getAngle());
            drawTurtle(x, y, angle);
            drawingPane.getChildren().addAll(turtle, turtleHead);
        });

        loadButton.setOnAction(actionEvent -> {
            resetCanvasView();
            actionVm.charger();
        });

        executeButton.setOnAction(actionEvent -> actionVm.executer());

        resetButton.setOnAction(actionEvent -> {
            resetCanvasView();
            // On réinitialiser notre liste
            actionVm.reinitialiserListeScene();
        });

        controlButtons.getChildren().addAll(loadButton, resetButton, executeButton, nextButton);

        // design des boutons taille et position
        controlButtons.setAlignment(Pos.CENTER);
        return controlButtons;
    }

    public void bindings() {
        loadButton.disableProperty().bind(actionVm.hasAnomalies());
        loadButton.visibleProperty().bind(actionVm.hasProgChange().or(actionVm.hasChargerSceneProperty().not()));
        loadButton.managedProperty().bind(actionVm.hasChargerSceneProperty().not());
        resetButton.visibleProperty().bind(actionVm.hasProgChange().not().and(actionVm.hasChargerSceneProperty()));
        resetButton.managedProperty().bind(actionVm.hasProgChange().not().and(actionVm.hasChargerSceneProperty()));

        executeButton.disableProperty().bind(actionVm.hasChargerSceneProperty().not().or(actionVm.fullyTraversedSceneList()));
        executeButton.visibleProperty().bind(nextButton.visibleProperty().not());
        executeButton.managedProperty().bind(nextButton.visibleProperty().not());
        nextButton.visibleProperty().bind(actionVm.hasChargerSceneProperty().and(actionVm.existIndex().and(actionVm.fullyTraversedSceneList().not())));
        nextButton.managedProperty().bind(actionVm.hasChargerSceneProperty().and(actionVm.existIndex().and(actionVm.fullyTraversedSceneList().not())));

    }

    public void setupCanvas() {
        // Design exterieur du canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Design intérieur du canvas
        GraphicsContext gcInside = canvas.getGraphicsContext2D();
        gcInside.setStroke(Color.LIGHTGRAY);
        gcInside.setLineWidth(1);

        int heightCanvas = (int) canvas.getHeight();
        int cellSize = heightCanvas / 10;
        int widthCanvas = (int) canvas.getWidth();

        for (int i = cellSize; i < heightCanvas; i += cellSize) {
            gcInside.strokeLine(0, i, heightCanvas, i);// (x1, y1, x2, y2)
        }
        for (int i = cellSize; i < widthCanvas; i += cellSize) {
            gcInside.strokeLine(i, 0, i, widthCanvas);// (x1, y1, x2, y2)
        }
    }

    private void drawTurtle(double x, double y, double angle) {
        // angle on degrees
        double xP1 = x + TURTLE_SIZE * Math.sin(Math.toRadians(60)) * Math.cos(Math.toRadians(angle + 90)),
                yP1 = y - TURTLE_SIZE * Math.sin(Math.toRadians(60)) * Math.sin(Math.toRadians(angle + 90)),
                xP2 = x - TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.cos(Math.toRadians(angle)),
                yP2 = y + TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.sin(Math.toRadians(angle)),
                xP3 = x + TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.cos(Math.toRadians(angle)),
                yP3 = y - TURTLE_SIZE * Math.sin(Math.toRadians(30)) * Math.sin(Math.toRadians(angle));

        this.turtle = new Polygon(xP1, yP1, xP2, yP2, xP3, yP3); // draw the body (triangle)
        this.turtle.setFill(Color.DEEPSKYBLUE); // Color of the body
        this.turtle.setStroke(Color.BLACK);
        this.turtleHead.setTranslateX(xP1); // move the head of the turtle (circle)
        this.turtleHead.setTranslateY(yP1);
    }

    private void drawLine() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        if (!actionVm.getLines().isEmpty()) {
            Line lastLine = actionVm.getLines().getLast();
            gc.strokeLine(lastLine.getStart().getX(), lastLine.getStart().getY(), lastLine.getStop().getX(), lastLine.getStop().getY());
        }
    }

    private void resetCanvasView() {
        // Si actionVm.resetCanvasListenerProperty() est modifié alors la ligne suivante ce lance
        actionVm.getResetCanvasProperty().addListener((obs, oldVal, newVal) -> resetCanvasView());
        // On supprime le canvas et la tortue
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawingPane.getChildren().removeAll(turtle, turtleHead);
        // on redessine un canvas
        setupCanvas();
        //reset position
        actionVm.resetCursorToCenter(canvas.getWidth(), canvas.getHeight());
        // on ajout une nouvelle tortue a la bonne position
        drawTurtle(canvas.getWidth() / 2, canvas.getHeight() / 2, 0);
        drawingPane.getChildren().addAll(turtle, turtleHead);
    }

    private void setupView() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
    }


    private HBox tableVariableContainer(){
        HBox hbox = new HBox();
        hbox.setMaxHeight(150);

        // 1.Création des nom de colums et pour chaque column on indique quel type de data y afficher, ou trouver cette data, a l'occurence là c'est Variable.getValue().nameVariableProperty qu'il trouve car il extend action
        TableColumn<Map.Entry<String, Variable>, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(entryStringCellDataFeatures -> entryStringCellDataFeatures.getValue().getValue().nameVariableProperty());
        TableColumn<Map.Entry<String, Variable>, String> valueColumn = new TableColumn<>("Valeur");
        valueColumn.setCellValueFactory(entryStringCellDataFeatures -> entryStringCellDataFeatures.getValue().getValue().measureProperty());
        // 2. Je rajoute les deux columns dans la tableview
        variableTable.getColumns().setAll(nomColumn, valueColumn);

        hbox.setMinWidth(500);
        nomColumn.setMinWidth(249);
        valueColumn.setMinWidth(249);
        // 3. Je desactive le sorted par ordre alphabetique et numeric
        nomColumn.setSortable(false);
        valueColumn.setSortable(false);

        // 4. J'ajoute dans ma table tout ce que je récupère de l'observableList crée dans actionVM
        variableTable.setItems(actionVm.getVariableList());

        hbox.getChildren().add(variableTable);
        return hbox;
    }

}
