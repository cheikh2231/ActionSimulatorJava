package scratch.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import scratch.model.Action;
import scratch.viewmodel.ViewModelAction;

public class ViewActionProg extends VBox {
    private ListView<Action> listProg = new ListView<>();
    private ViewModelAction vma ;
    private Label title = new Label("Programme");
    private VBox hda = new VBox();
    private TextField textFieldHda = new TextField();
    private Label LabelActionHda = new Label();
    private Label errorMesure = new Label(" invalid value");
    private TitledPane detailsAction = new TitledPane("Details de l'action",getHda());
    private Button btnMonter = new Button("Monter");
    private Button btnDescendre = new Button("Descendre");
    private Button btnDupliquer = new Button("Dupliquer");
    private Button btnSupprimer = new Button("Supprimer");
    private Button btnViderTout = new Button("Vider Tout");


    public HBox btnsManage(){
        HBox btnsManager = new HBox();
        btnsManager.getChildren().addAll(btnMonter,btnDescendre,btnDupliquer,btnSupprimer,btnViderTout);
        btnSupprimer.setOnAction(actionEvent -> vma.removeAction());
        btnViderTout.setOnAction(actionEvent -> vma.clearAllActions());
        btnDupliquer.setOnAction(actionEvent -> vma.duplicateAction());
        btnMonter.setOnAction(actionEvent -> vma.moveUp());
        btnDescendre.setOnAction(actionEvent -> vma.moveDown());
        return btnsManager;
    }

    public ViewActionProg(ViewModelAction vma) {
        this.vma = vma;
        bindings();
        this.listProg.setItems(vma.getActionProg());
        manageTextfield();
        affichageListViewProg();
        this.getChildren().addAll(title,listProg,btnsManage(),detailsAction);
    }

    public VBox getHda(){
        VBox principal = new VBox();
        HBox hda = new HBox();
        hda.getChildren().addAll(LabelActionHda,textFieldHda);
        principal.getChildren().addAll(hda,getLblError());
        return principal;
    }


    public HBox getLblError(){
        HBox hlblError = new HBox();
        errorMesure.setTextFill(Color.RED);
        hlblError.getChildren().add(errorMesure);
        return hlblError;
    }

    public void manageTextfield(){
        textFieldHda.setPrefWidth(36);
    }

    public void bindings(){
        listProg.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            vma.getSelectedActionProg().set(n);
        });

        vma.getSelectedActionProg().addListener((obs,o,n)->{
            listProg.getSelectionModel().select(n);
        });

        LabelActionHda.textProperty().bind(vma.getSelectedActionProgDetailsName());
        textFieldHda.textProperty().bindBidirectional(vma.getMesureProperty());
        textFieldHda.disableProperty().bind(vma.isSelectedActionProg().not().or(vma.IsDetailableProperty().not()));
        btnMonter.disableProperty().bind(vma.isSelectedActionProg().not().or(vma.canMoveUp().not()));
        btnDescendre.disableProperty().bind(vma.isSelectedActionProg().not().or(vma.canMoveDown().not()));
        btnDupliquer.disableProperty().bind(vma.isSelectedActionProg().not());
        btnSupprimer.disableProperty().bind(vma.isSelectedActionProg().not());
        btnViderTout.disableProperty().bind(vma.isEmptyActionProg());
        errorMesure.managedProperty().bind(vma.isValidMesureProperty().not());
        errorMesure.visibleProperty().bind(vma.isValidMesureProperty().not());
    }

    public void affichageListViewProg(){
        listProg.setCellFactory(lc->new ListCell<>(){
            @Override
            protected void updateItem(Action action, boolean empty) {
                super.updateItem(action, empty);
                // Création d'un label pour le text
                Label label = new Label();
                if(!(empty || action == null)) {
                    label.textProperty().bind(action.nameProperty().concat(action.mesureProperty().get() != 0 ? action.mesureProperty() : ""));
                    // Création d'un cercle
                    Circle circle = new Circle(5);
                    // On met tout dans un hbox avec 10 d'écart entre le cercle et le label
                    HBox box = new HBox(10, circle, label);
                    // On positione tout au centre de chaque ligne et sur la gauche de la ligne
                    box.setAlignment(Pos.CENTER_LEFT);
                    actionStyliser(action,circle,label);
                    setGraphic(box);
                } else {
                    label.textProperty().unbind();
                    setGraphic(null);
                }
            }
        });
    }

    public void actionStyliser(Action action, Circle circle, Label label){
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