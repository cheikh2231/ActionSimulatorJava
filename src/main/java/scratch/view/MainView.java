package scratch.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import scratch.viewmodel.MainViewModel;

public class MainView extends BorderPane {
    public MainView(MainViewModel vm,Stage stage) {
        HBox mainContainer = new HBox();
        var actionPaletteView = new ActionPaletteView(vm.getActionVm());
        var actionProgView = new ActionProgView(vm.getActionVm());
        var actionSceneView = new ActionSceneView(vm.getActionVm());
        var fileView = new FileView(vm.getActionVm(),stage);
        actionPaletteView.setPrefWidth(260);
        actionProgView.setPrefWidth(400);
        actionSceneView.setPrefWidth(400);

        mainContainer.getChildren().addAll(actionPaletteView,actionProgView,actionSceneView);
        mainContainer.setSpacing(25);
//        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(12));
        this.setTop(fileView);
        this.setCenter(mainContainer);
    }

}
