package scratch.ListStyliser;

import javafx.scene.control.Label;
import scratch.model.Action;

public class ProgListStyliser extends AbstractListStyliser {

    @Override
    protected void bindLabel(Label label, Action action) {
        label.textProperty().bind(action.nameProperty().concat(action.getMesure() != 0 ? action.getMesure() : ""));
    }
}