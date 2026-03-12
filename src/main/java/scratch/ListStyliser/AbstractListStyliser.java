package scratch.ListStyliser;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import scratch.model.Action;

public abstract class AbstractListStyliser extends ListCell<Action> {

    @Override
    protected void updateItem(Action action, boolean empty) {

        super.updateItem(action, empty);
        if (empty || action == null) {

            setGraphic(null);
            return;
        }
        Label label = new Label();
        bindLabel(label, action);

        Circle circle = new Circle(5);
        HBox box = new HBox(10, circle, label);
        box.setAlignment(Pos.CENTER_LEFT);

        actionStyliser(action, circle, label);
        setGraphic(box);
    }

    protected abstract void bindLabel(Label label, Action action);

    protected void actionStyliser(Action action, Circle circle, Label label) {
        String name = action.getName().toLowerCase();

        if (name.contains("avancer")) {
            circle.getStyleClass().add("text-blue");
            label.getStyleClass().add("text-blue");
        } else if (name.contains("tourner")) {
            circle.getStyleClass().add("text-red");
            label.getStyleClass().add("text-red");
        } else if(name.contains("stylo")) {
            circle.getStyleClass().add("text-green");
            label.getStyleClass().add("text-green");
        }else{
            circle.getStyleClass().add("text-turquoise");
            label.getStyleClass().add("text-turquoise");
        }
    }
}