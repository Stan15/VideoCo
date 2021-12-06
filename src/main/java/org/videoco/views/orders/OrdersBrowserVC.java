package org.videoco.views.orders;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import org.videoco.controllers.orders.OrderController;
import org.videoco.controllers.orders.OrderQuerier;
import org.videoco.controllers.orders.OrderQuery;
import org.videoco.models.orders.OrderModel;
import org.videoco.models.orders.OrderStatus;
import org.videoco.utils.database_queries.DBQuerier;
import org.videoco.utils.database_queries.Query;
import org.videoco.views.DatabaseBrowser;
import org.videoco.views.ViewEnum;

import java.net.URL;
import java.util.ResourceBundle;

public class OrdersBrowserVC extends DatabaseBrowser<OrderModel> {
    @FXML ChoiceBox<String> orderStatusChoiceBox;
    @FXML TableColumn<OrderModel, String> orderIDColumn;
    @FXML TableColumn<OrderModel, String> movieCountColumn;
    @FXML TableColumn<OrderModel, String> orderStatusColumn;

    @Override
    protected void setColumns() {
        orderIDColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getID()));
        movieCountColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().getMovies().size())));
        orderStatusColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus().name()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources);

        orderStatusChoiceBox.getItems().removeAll();
        orderStatusChoiceBox.setValue("ALL");
        orderStatusChoiceBox.getItems().add("ALL");
        for (OrderStatus stat : OrderStatus.values()) {
            if (stat!=OrderStatus.DRAFT)
                orderStatusChoiceBox.getItems().add(stat.name());
        }
        orderStatusChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable) -> this.updateQuery()
        );
    }

    @Override
    protected DBQuerier<OrderModel> createQuerier() {
        return new OrderQuerier(new OrderController(this.user), (list, controller) -> {
            list.removeIf((m) -> true);
            for (OrderModel order : ((OrderController)controller).getUserOrders()) {
                if (order.getStatus()!=OrderStatus.DRAFT) list.add(order);
            }
        });
    }

    @Override
    protected Query<OrderModel> getQuery() {
        return new OrderQuery(this.searchBar.getText(), this.orderStatusChoiceBox.getSelectionModel().getSelectedItem());
    }

    @Override
    public void onModelSelect(MouseEvent event) {
        OrderController controller = new OrderController(user);
        controller.setFocusModel(this.tableView.getSelectionModel().getSelectedItem());
        controller.createPopup(ViewEnum.ORDER_PAGE, event);
    }
}
