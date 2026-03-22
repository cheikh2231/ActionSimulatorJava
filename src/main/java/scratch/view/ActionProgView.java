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
    private final ListView<Action> progListView = new ListView<>();
    private final ActionViewModel actionVm;
    private final Label actionProgLabel = new Label("Programme");
    private final Label measureUnitLabel = new Label();
    private final TextField measureTextField = new TextField();
    private final TextField variableNameTextField = new TextField();
    private final Label actionNameLabel = new Label();
    private final Label textfieldsErrorLabel = new Label(" invalid value");
    private final Label valeurLabel = new Label(" valeur : ");
    private final TitledPane detailsAction = new TitledPane("Details de l'action", createActionDetailsBox());
    private final Button btnUp = new Button("Monter");
    private final Button btnDown = new Button("Descendre");
    private final Button btnDuplicate = new Button("Dupliquer");
    private final Button btnDelete = new Button("Supprimer");
    private final Button btnClear = new Button("Vider Tout");


    public HBox btnsManage() {
        HBox btnsManager = new HBox(5);
        btnsManager.getChildren().addAll(btnUp, btnDown, btnDuplicate, btnDelete, btnClear);
        btnDelete.setOnAction(actionEvent -> actionVm.removeAction());
        btnClear.setOnAction(actionEvent -> actionVm.clearAllActionsProgAndVariables());
        btnDuplicate.setOnAction(actionEvent -> actionVm.duplicateAction());
        btnUp.setOnAction(actionEvent -> actionVm.moveUp());
        btnDown.setOnAction(actionEvent -> actionVm.moveDown());
        btnsManager.setSpacing(12);
        return btnsManager;
    }

    public ActionProgView(ActionViewModel actionVm) {
        this.actionVm = actionVm;
        bindings();
        setupLayout();
        this.progListView.setItems(actionVm.getActionsProg());
        setupMeasureVariableNameTextfield();
        configListViewProg();
        this.getChildren().addAll(actionProgLabel, progListView, btnsManage(), detailsAction);
    }

    private VBox createActionDetailsBox() {
        final VBox principalBox = new VBox();
        final HBox detailsHBox = new HBox();

        detailsHBox.getChildren().addAll(actionNameLabel,variableNameTextField, valeurLabel,measureTextField, measureUnitLabel);
        principalBox.getChildren().addAll(detailsHBox, createErrorLabelBox());

        return principalBox;
    }


    private HBox createErrorLabelBox() {
        final HBox errorBox = new HBox();
        textfieldsErrorLabel.setTextFill(Color.RED);
        errorBox.getChildren().add(textfieldsErrorLabel);
        return errorBox;
    }

    private void setupMeasureVariableNameTextfield() {
        variableNameTextField.setPrefWidth(42);
        measureTextField.setPrefWidth(42);
        measureTextField.setAlignment(Pos.CENTER);
    }

    private void bindings() {
        actionVm.progIndexProperty().addListener((observableValue, number, n) ->
                progListView.getSelectionModel().select(n.intValue()));
        measureUnitLabel.textProperty().bind(actionVm.getSelectedActionProgDetailsUnity());
        actionNameLabel.textProperty().bind(actionVm.getSelectedActionProgDetailsName());
        valeurLabel.visibleProperty().bind(actionVm.isAssignation());
        valeurLabel.managedProperty().bind(actionVm.isAssignation());
        measureTextField.textProperty().bindBidirectional(actionVm.getMesureProperty());
        measureTextField.visibleProperty().bind(actionVm.isDeclaration().not());
        measureTextField.disableProperty().bind(actionVm.isSelectedActionProg().not().or(actionVm.IsDetailableProperty().not()));
        variableNameTextField.visibleProperty().bind(actionVm.isDeclaration().or(actionVm.isAssignation()));
        variableNameTextField.managedProperty().bind(actionVm.isDeclaration().or(actionVm.isAssignation()));
        variableNameTextField.textProperty().bindBidirectional(actionVm.getVariableNameProperty());
        btnUp.disableProperty().bind(actionVm.isSelectedActionProg().not().or(actionVm.canMoveUp().not()));
        btnDown.disableProperty().bind(actionVm.isSelectedActionProg().not().or(actionVm.canMoveDown().not()));
        btnDuplicate.disableProperty().bind(actionVm.isSelectedActionProg().not());
        btnDelete.disableProperty().bind(actionVm.isSelectedActionProg().not());
        btnClear.disableProperty().bind(actionVm.isEmptyActionProg());
        textfieldsErrorLabel.managedProperty().bind(((actionVm.isDeclaration()).and(actionVm.isValidVariableNameProperty().not())).or(actionVm.isValidMesureProperty().not()));
        textfieldsErrorLabel.visibleProperty().bind(actionVm.isValidMesureProperty().not().or(actionVm.isValidVariableNameProperty().not()));
        progListView.getSelectionModel().selectedIndexProperty().addListener((obs, o, n) -> {
            actionVm.progIndexProperty().set(n.intValue());
        });
        actionVm.actionSelectedProperty().bind(progListView.getSelectionModel().selectedItemProperty());


    }

    private void configListViewProg() {
        progListView.setCellFactory(lc -> new ProgListStyliser() {

        });
    }

    private void setupLayout() {
        this.setSpacing(16);
    }
}