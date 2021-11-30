package org.videoco.views.movies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.videoco.controllers.MovieController;
import org.videoco.models.MovieModel;
import org.videoco.utils.Utils;
import org.videoco.views.ViewType;

public class MovieCardView {
    private MovieController controller;
    private MovieModel movie;
    @FXML
    private ImageView image;

    public void setMovie(MovieModel movie) {
        this.movie = movie;
        try {
            image.setImage(new Image(Utils.createImageInputStream(movie.getCardImage())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void selectMovie(ActionEvent event) {
        try {
            controller.setFocusModel(movie);
            controller.transition(ViewType.MoviePage, event);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
