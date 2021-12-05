module org.videoco {
    requires javafx.controls;
    requires javafx.fxml;
    requires opencsv;
    requires org.json;

    exports org.videoco;
    opens org.videoco to javafx.fxml;
    opens org.videoco.views to javafx.fxml;
    opens org.videoco.views.movies to javafx.fxml;
    opens org.videoco.views.orders to javafx.fxml;
    opens org.videoco.views.authentication to javafx.fxml;
    opens org.videoco.views.components to javafx.fxml;
    opens org.videoco.views.sidebar to javafx.fxml;
    opens org.videoco.views.sidebar.switcher to javafx.fxml;
    opens org.videoco.views.sidebar.info to javafx.fxml;
}