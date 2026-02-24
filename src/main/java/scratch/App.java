package scratch;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;

import javafx.scene.layout.HBox;

import javafx.stage.Stage;
import scratch.view.ViewActionPalette;
import scratch.view.ViewActionProg;
import scratch.viewmodel.ViewModelAction;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        ViewModelAction vma = new ViewModelAction() ;
        HBox pane = new HBox();
        pane.getChildren().addAll(new ViewActionPalette(vma),new ViewActionProg(vma));
        pane.setPadding(new Insets(10,10,10,10));
        Scene scene = new Scene(pane, 640, 800);
        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );
        primaryStage.setTitle("Scratch");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}