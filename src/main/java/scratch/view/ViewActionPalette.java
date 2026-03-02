package scratch.view;

import javafx.scene.layout.VBox;


import javafx.scene.control.*;
import scratch.ListStyliser.PaletteListStyliser;
import scratch.model.Action;
import scratch.viewmodel.ViewModelAction;

public class ViewActionPalette extends VBox {
    private ViewModelAction vma ;
    Label title = new Label("Palette d'actions");
    private ListView<Action> listAction = new ListView<>();
    private Button btnAdd = new Button("Ajouter au programme");


    public ViewActionPalette(ViewModelAction vma) {
        this.vma = vma;
        listAction.setItems(vma.getActionPalette());
        this.getChildren().addAll(title,listAction,btnAdd);
        affichageListViewPalette();
        bindings();

        manage();
    }

    public void manage(){
        listAction.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() == 2)
                vma.addPaletteToProg();
        });
        btnAdd.setOnAction(e->{
            vma.addPaletteToProg();
        });
    }

    public void affichageListViewPalette() {
        listAction.setCellFactory(lc -> new PaletteListStyliser() {
        });
    }

    public void bindings(){
        vma.paletteIndexProperty().bind(listAction.getSelectionModel().selectedIndexProperty());
        btnAdd.disableProperty().bind(vma.paletteIndexProperty().lessThan(0));
    }
}