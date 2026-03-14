package scratch.view;

import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scratch.model.*;
import scratch.viewmodel.ActionViewModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileView extends HBox{
    private ActionViewModel actionVm;

    public FileView(ActionViewModel actionVm,Stage stage) {
        this.actionVm = actionVm;
        // Création de la barre de menu, ajout d'un Menu principal "File"
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        // Création des items pour le menu
        MenuItem newMenuItem = new MenuItem("New..");
        newMenuItem.setOnAction(actionEvent -> resetAll());
        MenuItem openMenuItem = new MenuItem("Open..");
        openMenuItem.setOnAction(actionEvent -> openFile(stage));
        MenuItem saveAsMenuItem = new MenuItem("Save As..");
        saveAsMenuItem.setOnAction(actionEvent -> saveFileAs(stage));
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> System.exit(0));
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        menuBar.getMenus().add(fileMenu);
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        fileMenu.getItems().addAll(newMenuItem,openMenuItem,saveAsMenuItem,exitMenuItem);
        this.getChildren().add(root);
    }

    public void resetAll(){
        actionVm.clearAllActionsProgAndVariables();
        actionVm.reinitialiserListeScene();
        actionVm.setResetCanvasProperty();
    }

    public void openFile(Stage stage){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open SCR file");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Scratch files", "*.scr")    );
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            resetAll();
           actionVm.loadFile(selectedFile);
        }
    }

    public void saveFileAs(Stage stage){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("Scratch files","*.scr");
        fileChooser.getExtensionFilters().add(extension);
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try (BufferedWriter bw = Files.newBufferedWriter(selectedFile.toPath(),StandardCharsets.UTF_8)) {
                for(Action action : actionVm.getActionsProg()){
                    bw.write(action.getFormatName()+";"+action.getMeasure()+"\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
