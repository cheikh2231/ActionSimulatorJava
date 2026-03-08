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
import java.util.Scanner;

public class FileView extends HBox{
    private ActionViewModel actionVm;
    public FileView(ActionViewModel actionVm,Stage stage) {
        this.actionVm = actionVm;
        // Création de la barre de menu, ajout d'un Menu principal "File"
        MenuBar barreDeMenu = new MenuBar();
        Menu menuFile = new Menu("File");
        // Création des items pour le menu
        MenuItem buttonNew = new MenuItem("New..");
        buttonNew.setOnAction(actionEvent -> resetAll());
        MenuItem buttonOpen = new MenuItem("Open..");
        buttonOpen.setOnAction(actionEvent -> openFile(stage));
        MenuItem buttonSaveAs = new MenuItem("Save As..");
        buttonSaveAs.setOnAction(actionEvent -> fileSaveAs(stage));
        MenuItem buttonExit = new MenuItem("Exit");
        buttonExit.setOnAction(actionEvent -> System.exit(0));
        BorderPane root = new BorderPane();
        root.setTop(barreDeMenu);
        barreDeMenu.getMenus().add(menuFile);
        barreDeMenu.prefWidthProperty().bind(stage.widthProperty());
        menuFile.getItems().addAll(buttonNew,buttonOpen,buttonSaveAs,buttonExit);
        this.getChildren().add(root);
    }

    public void resetAll(){
        actionVm.clearAllActionsProg();
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
            try {
                Scanner fileScan = new Scanner(selectedFile);
                while(fileScan.hasNextLine()){
                    String line = fileScan.nextLine();
                    int mesureStart = line.indexOf(";");
                    int actionMesure = Integer.parseInt(line.substring(mesureStart+1));
                    String actionType = line.substring(0,mesureStart);

                    actionVm.generateActionAndAddToProg(actionType,actionMesure);
                }
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void fileSaveAs(Stage stage){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extension = new FileChooser.ExtensionFilter("Scratch files","*.scr");
        fileChooser.getExtensionFilters().add(extension);
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            try (BufferedWriter bw = Files.newBufferedWriter(selectedFile.toPath(),StandardCharsets.UTF_8)) {
                for(Action action : actionVm.getActionsProg()){
                    bw.write(action.getFormatName()+";"+action.getMesure()+"\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
