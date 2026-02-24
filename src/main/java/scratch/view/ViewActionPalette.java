package scratch.view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import javafx.scene.control.*;
import javafx.scene.shape.Circle;
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
        affichageListViewProg();
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

    public void affichageListViewProg(){
        listAction.setCellFactory(lc->new ListCell<>(){
            @Override
            protected void updateItem(Action action, boolean empty) {
                super.updateItem(action, empty);
                if(!(empty || action == null)) {
                    // Création d'un label pour le text
                    Label label = new Label(action.getName());
                    // Création d'un cercle
                    Circle circle = new Circle(5);
                    // On met tout dans un hbox avec 10 d'écart entre le cercle et le label
                    HBox box = new HBox(10, circle, label);
                    // On positione tout au centre de chaque ligne et sur la gauche de la ligne
                    box.setAlignment(Pos.CENTER_LEFT);
                    actionStyliser(action,circle,label);
                    setGraphic(box);
                }
            }
        });
    }

    public void actionStyliser(Action action,Circle circle,Label label){
        // On met le name en lowercase dans uen variable pour utiliser contains dans notre if/else
        String name = action.getName().toLowerCase();
        if(name.contains("avancer")) {
            circle.getStyleClass().add("text-blue");
            label.getStyleClass().add("text-blue");
        } else if(name.contains("tourner")) {
            circle.getStyleClass().add("text-red");
            label.getStyleClass().add("text-red");
        } else {
            circle.getStyleClass().add("text-green");
            label.getStyleClass().add("text-green");
        }
    }
}