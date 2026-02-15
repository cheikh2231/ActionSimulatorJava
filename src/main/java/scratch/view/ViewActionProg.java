package scratch.view;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
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
    private Label hdaLabelAction = new Label();
    private TitledPane detailsAction = new TitledPane("Details de l'action",getHda());
    public ListView<Action> getListProg() {
        return listProg;
    }


    public ViewActionProg(ViewModelAction vma) {
        this.vma = vma;
        bindings();
        this.listProg.setItems(vma.getActionProg());
        manageTextfield();

        this.getChildren().addAll(title,listProg,detailsAction);
    }

    public HBox getHda(){
        hda.getChildren().addAll(hdaLabelAction,textFieldHda);
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
        vma.getSelectedActionProg().addListener((obs,o,n)->{
            hdaLabelAction.setText(n.toString());
        });
    }
}