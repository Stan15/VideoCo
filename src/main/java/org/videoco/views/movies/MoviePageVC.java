package org.videoco.views.movies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.videoco.controllers.orders.OrderController;
import org.videoco.models.Model;
import org.videoco.models.MovieModel;
import org.videoco.views.ViewController;
import org.videoco.views.ViewEnum;

public class MoviePageVC extends ViewController {
    @FXML Label title;
    @FXML Label dateOfRelease;
    @FXML Label category;
    @FXML TextFlow description;
    @FXML TextFlow actors;
    @FXML TextFlow directors;

    OrderController orderController;

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        this.setupMovieInfo();
    }

    public void setupMovieInfo() {
        MovieModel movie = (MovieModel) this.model;

        title.setText(movie.getTitle());
        dateOfRelease.setText(movie.getDateOfRelease());
        category.setText(movie.getCategory());
        description.getChildren().add(new Text(movie.getDescription()));
        actors.getChildren().add(new Text(movie.getActors()));
        directors.getChildren().add(new Text(movie.getDirectors()));
    }

    @FXML
    public void addToCart(ActionEvent event) {
        if (this.orderController==null)
            this.orderController = new OrderController(this.user);
        if (!orderController.addToCart((MovieModel) this.model)) {
            this.createAlert(Alert.AlertType.ERROR, "Movie already in your cart.");
            return;
        }
        this.createAlert(Alert.AlertType.INFORMATION, "Movie successfully added to cart.", (optional) -> {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    @FXML
    public void removeFromCart(ActionEvent event) {
        if (this.orderController==null)
            this.orderController = new OrderController(this.user);
        if (!orderController.removeFromCart((MovieModel) this.model)) {
            this.createAlert(Alert.AlertType.ERROR, "Movie is not in your cart.");
            return;
        }
        this.createAlert(Alert.AlertType.INFORMATION, "Movie successfully removed from cart.", (optional) -> {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        });
    }
}
