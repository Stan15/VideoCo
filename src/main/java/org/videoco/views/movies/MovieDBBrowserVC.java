package org.videoco.views.movies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import org.videoco.controllers.movies.MovieController;
import org.videoco.controllers.movies.MovieQuery;
import org.videoco.models.MovieModel;
import org.videoco.utils.database_queries.Query;
import org.videoco.views.ViewEnum;

public class MovieDBBrowserVC extends MovieBrowserVC {
    @FXML Button addMovieButton;

    @Override
    public Query<MovieModel> getQuery() {
        return new MovieQuery(this.searchBar.getText(), this.categoryChoiceBox.getSelectionModel().getSelectedItem(), true);
    }

    @FXML
    public void createAddMoviePopup(ActionEvent event) {
        MovieController controller = new MovieController(user);
        controller.createPopup(ViewEnum.MOVIE_DB_EDIT_PAGE, event,
                (node, loader) -> {
                    MovieDBEditPageVC viewController = loader.getController();
                    viewController.removeDeleteBttn();
                    viewController.removeUpdateBttn();
                    return node;
                }
        );
    }

    @Override
    public void onModelSelect(MouseEvent event) {
        MovieController controller = new MovieController(user);
        controller.setFocusModel(tableView.getSelectionModel().getSelectedItem());
        controller.createPopup(ViewEnum.MOVIE_DB_EDIT_PAGE, event,
                (node, loader) -> {
                    MovieDBEditPageVC viewController = loader.getController();
                    viewController.removeAddBttn();
                    return node;
                }
        );
    }
}
