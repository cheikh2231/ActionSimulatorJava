package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import scratch.viewmodel.MainViewModel;

public class MainView extends BorderPane {
    public MainView(MainViewModel vm,Stage stage) {
        HBox container = new HBox();
        container.getChildren().addAll(new ActionPaletteView(vm.getActionVm()),new ActionProgView(vm.getActionVm()),new ActionSceneView(vm.getActionVm()));
        this.setTop(new FileView(stage));
        this.setCenter(container);
        container.setPadding(new Insets(10,10,10,10));
    }
}
