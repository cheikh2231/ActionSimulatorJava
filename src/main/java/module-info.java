module scratch {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires javafx.graphics;
    requires javafx.base;
    requires java.scripting;
//    requires scratch;

    opens scratch to javafx.fxml;
    exports scratch;
    exports scratch.ListStyliser;
    opens scratch.ListStyliser to javafx.fxml;
}