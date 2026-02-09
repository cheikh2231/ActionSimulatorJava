package scratch.view;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import scratch.model.Action;

public class ViewActionProg extends VBox {
    private ListView<Action> listProg = new ListView<>();
    private Label title = new Label("Programme");

    public ListView<Action> getListProg() {
        return listProg;
    }

    public ViewActionProg() {
        this.getChildren().addAll(title,listProg);
    }

    public void add(Action action){
        listProg.getItems().add(action);
    }
}
