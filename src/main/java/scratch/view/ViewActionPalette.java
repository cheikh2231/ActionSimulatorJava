package scratch.view;

import javafx.scene.layout.VBox;


import javafx.scene.control.*;
import scratch.model.Action;

public class ViewActionPalette extends VBox {
    Label title = new Label("title");
    private ListView<Action> listAction = new ListView<>();

    public ViewActionPalette() {
       this.getChildren().addAll(title,listAction);
    }
}
