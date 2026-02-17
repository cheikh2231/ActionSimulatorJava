package scratch.view;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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


    public ListView<Action> getListProg() {
        return listProg;
    }

    public ViewActionProg(ViewModelAction vma) {
        this.vma = vma;
        bindings();
        this.listProg.setItems(vma.getActionProg());
        manageTextfield();
        affichageListViewProg();
        this.getChildren().addAll(title,listProg,detailsAction);
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
        vma.getSelectedActionProg().bind(listProg.getSelectionModel().selectedItemProperty());
//        vma.getSelectedActionProg().addListener((obs,o,n)->{
//            hdaLabelAction.setText(n.toString());
//        });
        LabelActionHda.textProperty().bind(vma.getSelectedActionProgDetailsName());
        textFieldHda.textProperty().bindBidirectional(vma.getMesureProperty());
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