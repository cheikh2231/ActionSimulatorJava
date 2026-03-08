package scratch.view;

import javafx.geometry.Insets;
import javafx.scene.layout.VBox;


import javafx.scene.control.*;
import scratch.ListStyliser.PaletteListStyliser;
import scratch.model.Action;
import scratch.viewmodel.ActionViewModel;

public class ActionPaletteView extends VBox {
    private final ActionViewModel actionVm ;
    private final Label title = new Label("Palette d'actions");
    private final ListView<Action> listAction = new ListView<>();
    private final Button btnAdd = new Button("Ajouter au programme");


    public ActionPaletteView(ActionViewModel actionVm) {
        this.actionVm = actionVm;
        listAction.setItems(actionVm.getActionPalette());
        this.getChildren().addAll(title,listAction,btnAdd);
        configListViewPalette();
        bindings();
        setupLayout();
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

    public void configListViewPalette() {
        listAction.setCellFactory(lc -> new PaletteListStyliser() {
        });
    }

    public void bindings(){
        actionVm.paletteIndexProperty().bind(listAction.getSelectionModel().selectedIndexProperty());
        btnAdd.disableProperty().bind(actionVm.canAddToProgram().not());
    }

    private void setupLayout() {
        this.setSpacing(10);
        this.setPadding(new Insets(5));
    }
}