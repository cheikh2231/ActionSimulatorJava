package scratch.ListStyliser;

import javafx.scene.control.Label;
import scratch.model.Action;

public class PaletteListStyliser extends AbstractListStyliser {

    @Override
    protected void bindLabel(Label label, Action action) {
        label.setText(action.getName());
    }
}