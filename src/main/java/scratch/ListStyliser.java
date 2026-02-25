package scratch;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import scratch.model.Action;

public class ListStyliser extends ListCell<Action> {

    protected void updateItem(Action action, boolean empty) {
        super.updateItem(action, empty);
        if(!(empty || action == null)) {
            // Création d'un label pour le text
            Label label = new Label(action.getName());
            // Création d'un cercle
            Circle circle = new Circle(5);
            // On met tout dans un hbox avec 10 d'écart entre le cercle et le label
            HBox box = new HBox(10, circle, label);
            // On positione tout au centre de chaque ligne et sur la gauche de la ligne
            box.setAlignment(Pos.CENTER_LEFT);
            actionStyliser(action,circle,label);
            setGraphic(box);
        }
}


public void actionStyliser(Action action,Circle circle,Label label){
    // On met le name en lowercase dans uen variable pour utiliser contains dans notre if/else
    String name = action.getName().toLowerCase();
    if(name.contains("avancer")) {
        circle.getStyleClass().add("text-blue");
        label.getStyleClass().add("text-blue");
    } else if(name.contains("tourner")) {
        circle.getStyleClass().add("text-red");
        label.getStyleClass().add("text-red");
    } else {
        circle.getStyleClass().add("text-green");
        label.getStyleClass().add("text-green");
    }
}
}
