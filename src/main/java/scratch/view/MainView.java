package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import scratch.viewmodel.MainViewModel;

public class MainView extends HBox {
    public MainView(MainViewModel vm) {
        this.getChildren().addAll(new ActionPaletteView(vm.getActionVm()),new ActionProgView(vm.getActionVm()),new ActionSceneView(vm.getActionVm()));
        this.setPadding(new Insets(10,10,10,10));
    }
}
