package scratch.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import scratch.ListStyliser.ProgListStyliser;
import scratch.model.Action;
import scratch.viewmodel.ActionViewModel;

public class ActionProgView extends VBox {
    private ListView<Action> listProg = new ListView<>();
    private ActionViewModel actionVm;
    private Label title = new Label("Programme");
    private Label labelUnityHda = new Label();
    private VBox hda = new VBox();
    private TextField textFieldHda = new TextField();
    private Label LabelActionHda = new Label();
    private Label errorMesure = new Label(" invalid value");
    private TitledPane detailsAction = new TitledPane("Details de l'action", getHda());
    private Button btnMonter = new Button("Monter");
    private Button btnDescendre = new Button("Descendre");
    private Button btnDupliquer = new Button("Dupliquer");
    private Button btnSupprimer = new Button("Supprimer");
    private Button btnViderTout = new Button("Vider Tout");


    public HBox btnsManage() {
        HBox btnsManager = new HBox(5);
        btnsManager.getChildren().addAll(btnMonter, btnDescendre, btnDupliquer, btnSupprimer, btnViderTout);
        btnSupprimer.setOnAction(actionEvent -> actionVm.removeAction());
        btnViderTout.setOnAction(actionEvent -> actionVm.clearAllActionsProg());
        btnDupliquer.setOnAction(actionEvent -> actionVm.duplicateAction());
        btnMonter.setOnAction(actionEvent -> actionVm.moveUp());
        btnDescendre.setOnAction(actionEvent -> actionVm.moveDown());
        btnsManager.setSpacing(12);
        return btnsManager;
    }

    public ActionProgView(ActionViewModel actionVm) {
        this.actionVm = actionVm;
        bindings();
        design();
        this.listProg.setItems(actionVm.getActionsProg());
        manageTextfield();
        affichageListViewProg();

        this.getChildren().addAll(title, listProg, btnsManage(), detailsAction);
    }

    public VBox getHda() {
        VBox principal = new VBox();
        HBox hda = new HBox();
        hda.getChildren().addAll(LabelActionHda, textFieldHda, labelUnityHda);
        principal.getChildren().addAll(hda, getLblError());
        return principal;
    }


    public HBox getLblError() {
        HBox hlblError = new HBox();
        errorMesure.setTextFill(Color.RED);
        hlblError.getChildren().add(errorMesure);
        return hlblError;
    }

    public void manageTextfield() {
        textFieldHda.setPrefWidth(36);
        textFieldHda.setAlignment(Pos.CENTER);
    }

    public void bindings() {
        actionVm.progIndexProperty().addListener((observableValue, number, n) ->
                listProg.getSelectionModel().select(n.intValue()));
        labelUnityHda.textProperty().bind(actionVm.getSelectedActionProgDetailsUnity());
        LabelActionHda.textProperty().bind(actionVm.getSelectedActionProgDetailsName());
        textFieldHda.textProperty().bindBidirectional(actionVm.getMesureProperty());
        textFieldHda.disableProperty().bind(actionVm.isSelectedActionProg().not().or(actionVm.IsDetailableProperty().not()));
        btnMonter.disableProperty().bind(actionVm.isSelectedActionProg().not().or(actionVm.canMoveUp().not()));
        btnDescendre.disableProperty().bind(actionVm.isSelectedActionProg().not().or(actionVm.canMoveDown().not()));
        btnDupliquer.disableProperty().bind(actionVm.isSelectedActionProg().not());
        btnSupprimer.disableProperty().bind(actionVm.isSelectedActionProg().not());
        btnViderTout.disableProperty().bind(actionVm.isEmptyActionProg());
        errorMesure.managedProperty().bind(actionVm.isValidMesureProperty().not());
        errorMesure.visibleProperty().bind(actionVm.isValidMesureProperty().not());
        listProg.getSelectionModel().selectedIndexProperty().addListener((obs, o, n) -> {
            actionVm.progIndexProperty().set(n.intValue());
        });
        actionVm.actionSelectedProperty().bind(listProg.getSelectionModel().selectedItemProperty());
    }

    public void affichageListViewProg() {
        listProg.setCellFactory(lc -> new ProgListStyliser() {

        });
    }

    private void design() {
        this.setSpacing(16);
    }
}