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
        vma.getSelectedActionPalette().bind(listAction.getSelectionModel().selectedItemProperty());
        btnAdd.disableProperty().bind(vma.canAdd().not());
        affichageListViewPalette();
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
}