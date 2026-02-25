package scratch.view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import scratch.viewmodel.ViewModelAction;

public class ViewActionScene extends VBox {
    private ViewModelAction vma;
    private Label title = new Label("Scene");
    private Canvas canvas = new Canvas(400,400);
    private Button reinitialiser = new Button("Ré-initialiser");
    private Button suivant = new Button("Suivant");
    private HBox btnsControls= new HBox();

    public ViewActionScene(ViewModelAction vma) {
        this.vma=vma;
        btnsControls.getChildren().addAll(reinitialiser,suivant);
        canvasDesign();
        this.getChildren().addAll(title,canvas,btnsControls);
    }

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
}
