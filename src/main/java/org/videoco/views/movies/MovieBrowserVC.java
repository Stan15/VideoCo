package org.videoco.views.movies;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.videoco.controllers.movies.MovieController;
import org.videoco.models.MovieModel;
import org.videoco.utils.database_queries.DBQuerier;
import org.videoco.utils.database_queries.Query;
import org.videoco.views.DatabaseBrowser;
import org.videoco.views.ViewEnum;
import org.videoco.controllers.movies.MovieQuerier;
import org.videoco.controllers.movies.MovieQuery;

import java.net.URL;
import java.util.ResourceBundle;

public class MovieBrowserVC extends DatabaseBrowser<MovieModel> {
    @FXML ChoiceBox<MovieModel.MovieCategory> categoryChoiceBox;
    @FXML TableColumn<MovieModel, String> titleColumn;
    @FXML TableColumn<MovieModel, String> actorsColumn;
    @FXML TableColumn<MovieModel, String> blurbColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        categoryChoiceBox.getItems().removeIf((c) -> true);
        categoryChoiceBox.setValue(MovieModel.MovieCategory.ALL);
        for (MovieModel.MovieCategory cat : MovieModel.MovieCategory.values()) {
            categoryChoiceBox.getItems().add(cat);
        }
        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable) -> this.updateQuery()
        );
    }

    @Override
    public void setColumns() {
        titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
        actorsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getActorsFormated()));
        blurbColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getBlurb()));
    }

    @Override
    public DBQuerier<MovieModel> createQuerier() {
        return new MovieQuerier(new MovieController(this.user));
    }

    @Override
    public Query<MovieModel> getQuery() {
        return new MovieQuery(this.searchBar.getText(), this.categoryChoiceBox.getSelectionModel().getSelectedItem());
    }

    @FXML
    @Override
    public void onModelSelect(MouseEvent event) {
        MovieController controller = new MovieController(user);
        MovieModel selectedMovie = tableView.getSelectionModel().getSelectedItem();
        if (selectedMovie==null) return;
        controller.setFocusModel(selectedMovie);
        controller.createPopup(ViewEnum.MOVIE_PAGE, event);
    }
}