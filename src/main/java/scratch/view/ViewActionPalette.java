package scratch.view;

import javafx.scene.layout.VBox;


import javafx.scene.control.*;
import javafx.scene.paint.Color;
import scratch.model.Action;
import scratch.model.ActionAvancer;
import scratch.model.ActionTournerDroite;
import scratch.model.ActionTournerGauche;
import scratch.viewmodel.ViewModelAction;

public class ViewActionPalette extends VBox {
    private ViewModelAction vma ;
    Label title = new Label("Palette d'actions");
    private ListView<Action> listAction = new ListView<>();
    private Button btnAdd = new Button("Ajouter au programme");
    private ActionAvancer actionAvancer = new ActionAvancer();
    private ActionTournerGauche turnLeft = new ActionTournerGauche();
    private ActionTournerDroite turnRight = new ActionTournerDroite();

    public ViewActionPalette(ViewModelAction vma) {
        this.vma = vma;
        listAction.getItems().addAll(actionAvancer,turnLeft,turnRight);
        this.getChildren().addAll(title,listAction,btnAdd);
        vma.getSelectedAction().bind(listAction.getSelectionModel().selectedItemProperty());
        btnAdd.disableProperty().bind(vma.canAdd().not());
        manage();
    }

    public void manage(){
        btnAdd.setOnAction(e->{
            vma.addToProg();
        });
    }
}