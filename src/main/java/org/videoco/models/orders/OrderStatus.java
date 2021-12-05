package org.videoco.models.orders;

public enum OrderStatus {
    DRAFT(1), PLACED(2), CANCELLED(3), SHIPPED(4), DELIVERED(5);

    public final int ordering;
    OrderStatus(int ordering) {
        this.ordering = ordering;
    }
}
