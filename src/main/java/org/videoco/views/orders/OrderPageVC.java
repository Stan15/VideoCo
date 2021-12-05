package org.videoco.views.orders;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.videoco.controllers.movies.MovieController;
import org.videoco.controllers.movies.MovieQuerier;
import org.videoco.controllers.orders.OrderController;
import org.videoco.models.Model;
import org.videoco.models.MovieModel;
import org.videoco.models.orders.OrderModel;
import org.videoco.models.users.UserModel;
import org.videoco.utils.Utils;
import org.videoco.views.ViewController;
import org.videoco.views.ViewEnum;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderPageVC extends ViewController implements Initializable {
    @FXML Label orderStatus;
    @FXML Label orderID;
    @FXML Label dateOrdered;
    @FXML Label dateDelivered;
    @FXML ListView<MovieModel> movieListView;
    @FXML ButtonBar buttonBar;
    @FXML Button placeOrderBttn;
    @FXML Button cancelOrderBttn;

    MovieQuerier movieQuerier;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.movieQuerier = createMovieQuerier();
        this.movieListView.setItems(this.movieQuerier.getQueriedList());
        this.setupDisplayItems();
    }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        this.initialize(null, null);
    }

    @Override
    public void setUser(UserModel user) {
        super.setUser(user);
        this.initialize(null, null);
    }

    @FXML
    public void setupButtons() {
        switch(((OrderModel) this.model).getStatus()) {
            case DRAFT -> buttonBar.getButtons().remove(cancelOrderBttn);
            case PLACED, SHIPPED -> buttonBar.getButtons().remove(placeOrderBttn);
            case DELIVERED, CANCELLED -> {
                    buttonBar.getButtons().remove(placeOrderBttn);
                    buttonBar.getButtons().remove(cancelOrderBttn);
            }
        }
    }

    @FXML
    public void cancelOrder(ActionEvent event) {
        OrderController orderController = new OrderController(this.user);
        if (this.model==null) {
            new Exception("No model attached to view controller").printStackTrace();
            return;
        }
        String error = orderController.cancelOrder(this.model.getDatabaseKey());
        if (error!=null)
            this.createAlert(Alert.AlertType.ERROR, error);
        else
            this.createAlert(Alert.AlertType.INFORMATION, "Order successfully cancelled.");
        this.setupButtons();
    }

    @FXML
    public void placeOrder(ActionEvent event) {
        OrderController orderController = new OrderController(this.user);
        String error = orderController.placeOrder(false);
        if (error!=null)
            this.createAlert(Alert.AlertType.ERROR, error);
        else
            this.createAlert(Alert.AlertType.INFORMATION, "Order successfully placed.");
        this.setupButtons();
    }

    public void setupDisplayItems() {
        if (this.model==null) return;
        OrderModel order = (OrderModel) this.model;
        this.orderID.setText("ID#"+order.getID());
        this.orderStatus.setText(order.getStatus().name());
        this.dateOrdered.setText(Utils.convertDateToString(order.getDateOrdered()));
        this.dateDelivered.setText(Utils.convertDateToString(order.getDateDelivered()));
        this.setupButtons();
    }

    private MovieQuerier createMovieQuerier() {
        return new MovieQuerier(new MovieController(this.user), (list, controller) -> {
            list.removeIf((m) -> true);
            if (this.model==null) return;
            this.setupDisplayItems();
            list.addAll(((OrderModel) this.model).getMovies());
        });
    }

    public void onModelSelect(MouseEvent event) {
        MovieController controller = new MovieController(user);
        MovieModel selectedMovie = movieListView.getSelectionModel().getSelectedItem();
        if (selectedMovie==null) return;
        controller.setFocusModel(selectedMovie);
        controller.createPopup(ViewEnum.MOVIE_PAGE, event);
    }
}
