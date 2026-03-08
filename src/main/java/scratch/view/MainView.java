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
        var palette = new ActionPaletteView(vm.getActionVm());
        var programme = new ActionProgView(vm.getActionVm());
        var scene = new ActionSceneView(vm.getActionVm());

        palette.setPrefWidth(260);
        programme.setPrefWidth(400);
        scene.setPrefWidth(400);

        container.getChildren().addAll(palette,programme,scene);
        container.setSpacing(25);
        container.setPadding(new Insets(12));
        this.setTop(new FileView(stage));
        this.setCenter(container);
    }

}
