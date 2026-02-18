package scratch.view;

import javafx.beans.property.Property;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.action.ActionProxy;
import scratch.model.Action;
import scratch.viewmodel.ViewModelAction;

public class ViewActionProg extends VBox {
    private ListView<Action> listProg = new ListView<>();
    private ViewModelAction vma ;
    private Label title = new Label("Programme");
    private HBox hda = new HBox();
    private TextField textFieldHda = new TextField();
    private Label LabelActionHda = new Label();
    private TitledPane detailsAction = new TitledPane("Details de l'action",getHda());
    private Button btnMonter = new Button("Monter");
    private Button btnDescendre = new Button("Descendre");
    private Button btnDupliquer = new Button("Dupliquer");
    private Button btnSupprimer = new Button("Supprimer");
    private Button btnViderTout = new Button("Vider Tout");

    public ListView<Action> getListProg() {
        return listProg;
    }

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

    public HBox getHda(){
        hda.getChildren().addAll(LabelActionHda,textFieldHda);
        return hda;
    }

    public void add(Action action){
        listProg.getItems().add(action);
    }

    public void manageTextfield(){
        textFieldHda.setPrefWidth(36);
    }

    public void bindings(){
        listProg.getSelectionModel().selectedItemProperty().addListener((obs,o,n)->{
            System.out.println(n);
            vma.getSelectedActionProg().set(n);
            System.out.println(vma.getSelectedAction());
        });

        vma.getSelectedActionProg().addListener((obs,o,n)->{
            listProg.getSelectionModel().select(n);
        });

        LabelActionHda.textProperty().bind(vma.getSelectedActionProgDetailsName());
        textFieldHda.textProperty().bindBidirectional(vma.getMesureProperty());
        textFieldHda.disableProperty().bind(vma.isSelectedAction().not());
        btnMonter.disableProperty().bind(vma.isSelectedAction().not().or(vma.canMoveUp().not()));
        btnDescendre.disableProperty().bind(vma.isSelectedAction().not().or(vma.canMoveDown().not()));
        btnDupliquer.disableProperty().bind(vma.isSelectedAction().not());
        btnSupprimer.disableProperty().bind(vma.isSelectedAction().not());
        btnViderTout.disableProperty().bind(vma.isEmptyActionProg());
    }

    public void affichageListViewProg(){
        listProg.setCellFactory(lc->new ListCell<>(){
            @Override
            protected void updateItem(Action action, boolean empty) {
                super.updateItem(action, empty);
                if(!(empty || action == null)) {
                    textProperty().bind(action.nameProperty().concat(action.mesureProperty()));
                } else {
                    textProperty().unbind();
                    setText(null);
                }
            }
        });
    }
}