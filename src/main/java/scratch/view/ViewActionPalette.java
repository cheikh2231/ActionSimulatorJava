package scratch.view;

import javafx.scene.layout.VBox;


import javafx.scene.control.*;
import javafx.scene.paint.Color;
import scratch.model.Action;
import scratch.model.ActionAvancer;
import scratch.model.ActionTournerDroite;
import scratch.model.ActionTournerGauche;

public class ViewActionPalette extends VBox {
    private ViewActionProg viewActionProg = new ViewActionProg();
    Label title = new Label("Palette d'actions");
    private ListView<Action> listAction = new ListView<>();
    private Button btnAdd = new Button("Ajouter au programme");
    private ActionAvancer actionAvancer = new ActionAvancer();
    private ActionTournerGauche turnLeft = new ActionTournerGauche();
    private ActionTournerDroite turnRight = new ActionTournerDroite();

    public ViewActionPalette() {
        listAction.getItems().addAll(actionAvancer,turnLeft,turnRight);
        this.getChildren().addAll(title,listAction,btnAdd);
        manage();
    }

    public void manage(){
        btnAdd.setOnAction(e->{

            viewActionProg.add(new ActionAvancer());
            System.out.println(listAction.getSelectionModel().getSelectedItem());
        });
    }
}
