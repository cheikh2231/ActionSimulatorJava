package scratch.view;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scratch.viewmodel.ActionViewModel;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileView extends HBox{

    public FileView(ActionViewModel actionVm,Stage stage) {
        // Création de la barre de menu, ajout d'un Menu principal "File"
        MenuBar barreDeMenu = new MenuBar();
        Menu menuFile = new Menu("File");
        // Création des items pour le menu
        MenuItem buttonNew = new MenuItem("New..");
        MenuItem buttonOpen = new MenuItem("Open..");
        MenuItem buttonSaveAs = new MenuItem("Save As..");
        buttonSaveAs.setOnAction(actionEvent -> fileSaveAs(stage));
        MenuItem buttonExit = new MenuItem("Exit");
        BorderPane root = new BorderPane();
        root.setTop(barreDeMenu);
        barreDeMenu.getMenus().add(menuFile);
        menuFile.getItems().addAll(buttonNew,buttonOpen,buttonSaveAs,buttonExit);
        this.getChildren().add(root);
    }

    public void fileSaveAs(Stage stage){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            try (BufferedWriter bw = Files.newBufferedWriter(selectedFile.toPath(),StandardCharsets.UTF_8)) {
                bw.write("Hello World!\n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
