package scratch.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import scratch.model.Cursor;
import scratch.model.Line;
import scratch.viewmodel.ActionViewModel;


public class ActionSceneView extends VBox {
    private ActionViewModel actionVm;
    private Label title = new Label("Scene");
    private Canvas canvas = new Canvas(500, 500);
    private Button reinitialiser = new Button("Ré-initialiser");
    private Button charger = new Button("Charger");
    private Button executer = new Button("Executer");
    private Button suivant = new Button("Suivant");
    public int TURTLE_HEAD_SIZE = Cursor.TURTLE_HEAD_SIZE;
    public int TURTLE_SIZE = Cursor.TURTLE_SIZE;
    private final Circle headTurtle = new Circle(TURTLE_HEAD_SIZE);
    private Polygon turtle;
    private Pane drawingPane = new Pane();

    public ActionSceneView(ActionViewModel actionVm) {
        this.actionVm = actionVm;
        drawTurtle(canvas.getWidth() / 2, canvas.getHeight() / 2, 0);
        canvasDesign();
        viewDesign();
        drawingPane.getChildren().addAll(canvas, headTurtle, turtle);
        this.getChildren().addAll(title, drawingPane, btnsManage());
        bindings();
    }


    public HBox btnsManage() {
        HBox btnsControls = new HBox(10);
        suivant.setOnAction(actionEvent -> {

            actionVm.executeAction();
            drawLine();
            drawingPane.getChildren().removeAll(turtle, headTurtle);
            double x = actionVm.getCursor().getPosition().getX();
            double y = actionVm.getCursor().getPosition().getY();
            double angle = (actionVm.getCursor().getAngle());
            drawTurtle(x, y, angle);
            drawingPane.getChildren().addAll(turtle, headTurtle);

        });
        charger.setOnAction(actionEvent -> {
            resetCanvas();
            actionVm.charger();
        });
        executer.setOnAction(actionEvent -> actionVm.executer());
        reinitialiser.setOnAction(actionEvent -> {
            resetCanvas();
            // On réinitialiser notre liste
            actionVm.reinitialiserListeScene();
        });
        btnsControls.getChildren().addAll(charger, reinitialiser, executer, suivant);

        // design des boutons taille et position
        btnsControls.setAlignment(Pos.CENTER);
        return btnsControls;
    }

    public void bindings() {
        charger.disableProperty().bind(actionVm.hasAnomalies());
        charger.visibleProperty().bind(actionVm.hasActionsProgChange().or(actionVm.hasChargerSceneProperty().not()));
        charger.managedProperty().bind(actionVm.hasChargerSceneProperty().not());
        reinitialiser.visibleProperty().bind(actionVm.hasActionsProgChange().not().and(actionVm.hasChargerSceneProperty()));
        reinitialiser.managedProperty().bind(actionVm.hasActionsProgChange().not().and(actionVm.hasChargerSceneProperty()));

        executer.disableProperty().bind(actionVm.hasChargerSceneProperty().not().or(actionVm.fullyTraversedSceneList()));
        executer.visibleProperty().bind(suivant.visibleProperty().not());
        executer.managedProperty().bind(suivant.visibleProperty().not());
        suivant.visibleProperty().bind(actionVm.hasChargerSceneProperty().and(actionVm.existIndex().and(actionVm.fullyTraversedSceneList().not())));
        suivant.managedProperty().bind(actionVm.hasChargerSceneProperty().and(actionVm.existIndex().and(actionVm.fullyTraversedSceneList().not())));

    }

    public void canvasDesign() {
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
        int celulleCanvas = heightCanvas/10;
        int widthCanvas = (int) canvas.getWidth();

        for (int i = celulleCanvas; i < heightCanvas; i += celulleCanvas) {
            gcInside.strokeLine(0, i, heightCanvas, i);// (x1, y1, x2, y2)
        }
        for (int i = celulleCanvas; i < widthCanvas; i += celulleCanvas) {
            gcInside.strokeLine(i, 0, i,widthCanvas);// (x1, y1, x2, y2)
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
        this.headTurtle.setTranslateX(xP1); // move the head of the turtle (circle)
        this.headTurtle.setTranslateY(yP1);

    }


    private void drawLine() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        if (!actionVm.getLines().isEmpty()) {
            Line lastLine = actionVm.getLines().getLast();
            gc.strokeLine(lastLine.getStart().getX(), lastLine.getStart().getY(), lastLine.getStop().getX(), lastLine.getStop().getY());
        }
    }

    private void resetCanvas(){
        // Si actionVm.resetCanvasListenerProperty() est modifié alors la ligne suivante ce lance
        actionVm.getResetCanvasProperty().addListener((obs, oldVal, newVal) -> resetCanvas());
        // On supprime le canvas et la tortue
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawingPane.getChildren().removeAll(turtle, headTurtle);
        // on redessine un canvas
        canvasDesign();
        //reset position
        actionVm.getCursor().getPosition().setX(canvas.getWidth() / 2);
        actionVm.getCursor().getPosition().setY(canvas.getHeight() / 2);
        actionVm.getCursor().setAngle(0);
        // on ajout une nouvelle tortue a la bonne position
        drawTurtle(canvas.getWidth() / 2, canvas.getHeight() / 2, 0);
        drawingPane.getChildren().addAll(turtle, headTurtle);
    }

    private void viewDesign() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
    }

}
