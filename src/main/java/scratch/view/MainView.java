package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import scratch.viewmodel.MainViewModel;

public class MainView extends HBox {
    public MainView(MainViewModel vm) {

        var palette = new ActionPaletteView(vm.getActionVm());
        var programme = new ActionProgView(vm.getActionVm());
        var scene = new ActionSceneView(vm.getActionVm());

        palette.setPrefWidth(260);
        programme.setPrefWidth(400);
        scene.setPrefWidth(400);

        this.setSpacing(25);
        this.setPadding(new Insets(12));

        this.getChildren().addAll(palette, programme, scene);
    }

}
