package scratch.view;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import scratch.model.Action;
import scratch.model.Cursor;
import scratch.model.Line;
import scratch.model.Point;
import scratch.viewmodel.ViewModelAction;

public class ViewActionScene extends VBox {
    private ViewModelAction vma;
    private Label title = new Label("Scene");
    private Canvas canvas = new Canvas(400,400);
    private Button reinitialiser = new Button("Ré-initialiser");
    private Button charger = new Button("Charger");
    private Button executer = new Button("Executer");
    private Button suivant = new Button("Suivant");
    public int TURTLE_HEAD_SIZE = Cursor.TURTLE_HEAD_SIZE;
    public int TURTLE_SIZE = Cursor.TURTLE_SIZE;
    private final Circle headTurtle = new Circle(TURTLE_HEAD_SIZE);
    private Polygon turtle ;
    private Pane drawingPane = new Pane();

    public ViewActionScene(ViewModelAction vma) {
        this.vma=vma;
        drawTurtle(canvas.getWidth()/2,canvas.getHeight()/2,0);
        canvasDesign();
        drawingPane.getChildren().addAll(canvas,headTurtle,turtle);
        this.getChildren().addAll(title,drawingPane,btnsManage());
        bindings();
    }


    public HBox btnsManage(){
        HBox btnsControls= new HBox();
        suivant.setOnAction(actionEvent -> {
            vma.avancer();
            drawLine();
            drawingPane.getChildren().removeAll(turtle,headTurtle);
            double x = vma.getCursor().getPosition().getX();
            double y = vma.getCursor().getPosition().getY();
            double angle = vma.getCursor().getAngle();
            drawTurtle(x,y,angle);
            drawingPane.getChildren().addAll(turtle,headTurtle);
        });
        charger.setOnAction(actionEvent -> vma.charger());
        executer.setOnAction(actionEvent -> vma.executer());
        btnsControls.getChildren().addAll(charger,reinitialiser,executer,suivant);
        return btnsControls;
    }

    public void bindings(){
        charger.disableProperty().bind(vma.hasAnomalies());
        charger.visibleProperty().bind(vma.hasActionsProgChange().or(vma.hasChargerSceneProperty().not()));
        charger.managedProperty().bind(vma.hasChargerSceneProperty().not());
        reinitialiser.visibleProperty().bind(charger.visibleProperty().not());
        reinitialiser.managedProperty().bind(charger.visibleProperty().not());

        executer.disableProperty().bind(vma.hasChargerSceneProperty().not().or(vma.existIndex()));
        executer.visibleProperty().bind(suivant.visibleProperty().not());
        executer.managedProperty().bind(suivant.visibleProperty().not());
        suivant.visibleProperty().bind(vma.hasChargerSceneProperty().and(vma.existIndex()));
        suivant.managedProperty().bind(vma.hasChargerSceneProperty().and(vma.existIndex()));

    }



//    public void getNext(){
//        vma.getNext();
//    }

//    public void setNewPosition(){
//        turtle.setTranslateY(turtle.getTranslateY()+20);
//        turtle.setTranslateX(turtle.getTranslateX()+20);
//        headTurtle.setTranslateX(headTurtle.getTranslateX()+20);
//        headTurtle.setTranslateY(headTurtle.getTranslateY()+20);
//    }


    public void canvasDesign(){
        // Design exterieur du canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // Design intérieur du canvas
        GraphicsContext gcInside = canvas.getGraphicsContext2D();
        gcInside.setStroke(Color.LIGHTGRAY);
        gcInside.setLineWidth(1);
        for (int i = 40; i<400;i+=40) {
            gcInside.strokeLine(0, i, 400, i);// (x1, y1, x2, y2)
        }
        for (int i = 40; i<400;i+=40) {
            gcInside.strokeLine(i, 0, i, 400);// (x1, y1, x2, y2)
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


    private void drawLine(){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        for (Line l : vma.getLines()){
            gc.strokeLine(l.getPointDepart().getX(),l.getPointDepart().getY(),l.getPointArrive().getX(),l.getPointArrive().getY());
        }
    }


}
