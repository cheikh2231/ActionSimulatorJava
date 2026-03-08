package scratch.view;

import javafx.scene.layout.VBox;


import javafx.scene.control.*;
import scratch.ListStyliser.PaletteListStyliser;
import scratch.model.Action;
import scratch.viewmodel.ActionViewModel;

public class ActionPaletteView extends VBox {
    private ActionViewModel actionVm ;
    Label title = new Label("Palette d'actions");
    private ListView<Action> listAction = new ListView<>();
    private Button btnAdd = new Button("Ajouter au programme");


    public ActionPaletteView(ActionViewModel actionVm) {
        this.actionVm = actionVm;
        listAction.setItems(actionVm.getActionPalette());
        this.getChildren().addAll(title,listAction,btnAdd);
        affichageListViewPalette();
        bindings();

        manage();
    }

    public void manage(){
        listAction.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() == 2)
                actionVm.addPaletteToProg();
        });
        btnAdd.setOnAction(e->{
            actionVm.addPaletteToProg();
        });
    }

    public void affichageListViewPalette() {
        listAction.setCellFactory(lc -> new PaletteListStyliser() {
        });
    }

    public void bindings(){
        actionVm.paletteIndexProperty().bind(listAction.getSelectionModel().selectedIndexProperty());
        btnAdd.disableProperty().bind(actionVm.paletteIndexProperty().lessThan(0));
    }
}