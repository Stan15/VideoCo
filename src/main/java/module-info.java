module org.videoco {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencsv;
    requires org.json;

    opens org.videoco to javafx.fxml;
    exports org.videoco;
    opens org.videoco.views.movies to javafx.fxml;
    opens org.videoco.views.authentication to javafx.fxml;
}