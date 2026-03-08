package scratch;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import scratch.model.*;
import scratch.view.ActionPaletteView;
import scratch.view.ActionProgView;
import scratch.view.ActionSceneView;
import scratch.view.MainView;
import scratch.viewmodel.ActionViewModel;
import scratch.viewmodel.MainViewModel;

import java.io.File;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Programme programme = new Programme();
        Cursor cursor = new Cursor(0,new Point(200,200));
        ActionStylo actionStylo = new ActionStylo(true);
        SceneActions sceneActions = new SceneActions(cursor,actionStylo);

        MainViewModel vm = new MainViewModel(programme,sceneActions);

        var mainView = new MainView(vm,stage);
        createScene(stage,mainView);

        stage.show();

    }

    private void createScene(Stage stage, MainView view){
        Scene scene = new Scene(view, 1150, 800);
        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );
        stage.setTitle("Scratch");


        stage.setScene(scene);



    }

    public static void main(String[] args) {
        launch(args);
    }
}