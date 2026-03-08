package scratch;

import javafx.application.Application;
import javafx.scene.Scene;

import javafx.stage.Stage;
import scratch.model.*;
import scratch.view.MainView;
import scratch.viewmodel.MainViewModel;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Program program = new Program();
        Cursor cursor = new Cursor(0,new Point(200,200));
        ActionStylo actionStylo = new ActionStylo(true);
        SceneActions sceneActions = new SceneActions(cursor,actionStylo);

        MainViewModel vm = new MainViewModel(program,sceneActions);

        var mainView = new MainView(vm,stage);
        createScene(stage,mainView);

        stage.show();

    }

    private void createScene(Stage stage, MainView view){
        Scene scene = new Scene(view, 1300, 800);
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