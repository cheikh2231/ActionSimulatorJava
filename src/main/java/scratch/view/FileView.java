package scratch.view;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileView extends HBox{
    public FileView(Stage stage) {
        // Création de la barre de menu, ajout d'un Menu principal "File"
        MenuBar barreDeMenu = new MenuBar();
        Menu menuFile = new Menu("File");
        // Création des items pour le menu
        MenuItem buttonOpen = new MenuItem("Open..");
        BorderPane root = new BorderPane();
        root.setTop(barreDeMenu);
        barreDeMenu.getMenus().add(menuFile);
        menuFile.getItems().add(buttonOpen);
        barreDeMenu.prefWidthProperty().bind(stage.widthProperty());
        this.getChildren().add(root);
    }
}
