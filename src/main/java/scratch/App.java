package scratch;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import scratch.view.ViewActionPalette;
import scratch.view.ViewActionProg;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {

        HBox pane = new HBox();
        pane.getChildren().addAll(new ViewActionPalette(),new ViewActionProg());
        pane.setPadding(new Insets(10,10,10,10));
        Scene scene = new Scene(pane, 640, 480);
        primaryStage.setTitle("Scratch");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}