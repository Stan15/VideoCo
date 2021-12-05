package org.videoco.models.orders;

import org.videoco.models.Model;
import org.videoco.models.MovieModel;

import java.util.ArrayList;
import java.util.Date;

public class OrderModel extends Model {
    private String customerID;
    private ArrayList<MovieModel> movies = new ArrayList<>();
    private OrderStatus status;
    private Date dateOrdered;
    private Date dateDelivered;

    public boolean getPaidWithLoyaltyPoints() {
        return paidWithLoyaltyPoints;
    }

    public void setPaidWithLoyaltyPoints(boolean paidWithLoyaltyPoints) {
        this.paidWithLoyaltyPoints = paidWithLoyaltyPoints;
    }

    private boolean paidWithLoyaltyPoints;

    public Date getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(Date dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public Date getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(Date dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public OrderModel(String id, String customerID, ArrayList<MovieModel> movies, OrderStatus status) {
        this.setID(id);
        this.setCustomerID(customerID);
        this.setMovies(movies);
        this.setStatus(status);
    }

    @Override
    public String getDatabaseKey() {
        return this.id;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setMovies(ArrayList<MovieModel> movies) {
        this.movies = movies;
    }

    public ArrayList<MovieModel> getMovies() {
        return movies;
    }

    public String getMoviesStr() {
        StringBuilder moviesStr = new StringBuilder();
        for (MovieModel movie : this.movies) {
            if (!moviesStr.toString().isBlank()) moviesStr.append("-");
            moviesStr.append(movie.getID());
        }
        return moviesStr.toString();
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
