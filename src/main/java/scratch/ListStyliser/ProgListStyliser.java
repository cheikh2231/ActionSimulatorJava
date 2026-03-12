package scratch.ListStyliser;

import javafx.scene.control.Label;
import scratch.model.Action;
import scratch.model.Variable;
import scratch.model.VariableDeclaration;

public class ProgListStyliser extends AbstractListStyliser {

    @Override
    protected void bindLabel(Label label, Action action) {
        if(action instanceof VariableDeclaration vD)
            label.textProperty().bind(action.nameProperty().concat(vD.nameVariableProperty() != null ? vD.nameVariableProperty() : ""));
        else
            label.textProperty().bind(action.nameProperty().concat(action.measureProperty().get() != 0 ? action.measureProperty() : ""));
    }
}