package scratch.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import scratch.viewmodel.MainViewModel;

public class MainView extends BorderPane {
    public MainView(MainViewModel vm,Stage stage) {
        HBox container = new HBox();
        var paletteView = new ActionPaletteView(vm.getActionVm());
        var programmeView = new ActionProgView(vm.getActionVm());
        var sceneView = new ActionSceneView(vm.getActionVm());
        var fileView = new FileView(vm.getActionVm(),stage);
        paletteView.setPrefWidth(260);
        programmeView.setPrefWidth(400);
        sceneView.setPrefWidth(400);

        container.getChildren().addAll(paletteView,programmeView,sceneView);
        container.setSpacing(25);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(12));
        this.setTop(fileView);
        this.setCenter(container);
    }

}
