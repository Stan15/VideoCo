package org.videoco.controllers.orders;

import org.videoco.models.orders.OrderModel;
import org.videoco.models.orders.OrderStatus;
import org.videoco.utils.database_queries.DBQuerier;

import java.util.Objects;

public class OrderQuerier extends DBQuerier<OrderModel> {
    public OrderQuerier(OrderController controller, ListSource<OrderModel> source) {
        super(controller, new OrderQuery("", "ALL"), source);
    }
    public OrderQuerier(OrderController controller) {
        this(controller, DBQuerier::defaultListSource);
    }
    @Override
    public boolean filterPredicate(OrderModel order) {
        OrderStatus stat = OrderStatus.valueOf(order.getStatus().name());
        boolean filterStatus = Objects.equals(((OrderQuery) this.query).getStatus(), "ALL") || OrderStatus.valueOf(((OrderQuery) this.query).getStatus())==stat;
        return super.filterPredicate(order) && filterStatus;
    }
}
