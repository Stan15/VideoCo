package org.videoco.factories;

import org.videoco.controllers.movies.MovieController;
import org.videoco.controllers.orders.OrderController;
import org.videoco.models.Model;
import org.videoco.models.MovieModel;
import org.videoco.models.orders.OrderModel;
import org.videoco.models.orders.OrderStatus;
import org.videoco.utils.Utils;

import java.util.ArrayList;
import java.util.Date;

public class OrderFactory extends Factory {
    private String id;
    private String customerID;
    private ArrayList<MovieModel> movies;
    private OrderStatus status;
    private Date dateOrdered;

    private Date dateDelivered;
    private boolean paidWithLoyaltyPoints;

    public OrderFactory() {
        this.movies = new ArrayList<>();
    }
    public OrderFactory(OrderModel model) {
        super(model);
    }

    public boolean addMovie(MovieModel movie) {
        if (this.movies==null) this.movies = new ArrayList<>();
        if (this.movies.contains(movie)) return false;
        this.movies.add(movie);
        return true;
    }

    public boolean removeMovie(MovieModel movie) {
        if (this.movies==null) this.movies = new ArrayList<>();
        if (!this.movies.contains(movie)) return false;
        this.movies.remove(movie);
        return true;
    }
    @Override
    public void copy(Model model) {
        if (!(model instanceof OrderModel o)) return;
        this.setID(o.getID());
        this.setCustomerID(o.getCustomerID());
        this.setMovies(o.getMovies());
        this.setStatus(o.getStatus());
        this.setDateOrdered(o.getDateOrdered());
        this.setDateDelivered(o.getDateDelivered());
        this.setPaidWithLoyaltyPoints(o.getPaidWithLoyaltyPoints());
    }

    public void setDateOrdered(Date dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public void setDateDelivered(Date dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = Utils.convertStringToDate(dateOrdered);
    }

    public void setDateDelivered(String dateDelivered) {
        this.dateDelivered = Utils.convertStringToDate(dateDelivered);
    }

    public void setID(String id) {
        this.id = id;
    }

    @Override
    public Model createModel() {
        if (this.findErrorInRequiredFields()!=null) return null;
        if (this.id==null || !this.id.matches("-?\\d+(\\.\\d+)?")) this.setID(new OrderController().getNewID());
        OrderModel order = new OrderModel(id, customerID, movies, status);
        order.setDateOrdered(this.dateOrdered);
        order.setDateDelivered(this.dateDelivered);
        order.setPaidWithLoyaltyPoints(this.paidWithLoyaltyPoints);
        return order;
    }

    @Override
    public String findErrorInRequiredFields() {
        if (this.customerID==null || this.customerID.isBlank()) return "Missing customer ID.";
        if (this.movies==null || this.movies.isEmpty()) return "No movie added to order.";
        if (this.status==null) return "Missing order status.";
        return null;
    }

    @Override
    public String getDatabaseKeyField() {
        return this.id;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setMovies(String movieIDs) {
        String[] IDlist = movieIDs.split("-");
        this.movies = new ArrayList<>();
        MovieController controller = new MovieController();
        for (String movieID : IDlist) {
            this.movies.add((MovieModel) controller.getModelFromDB(movieID.strip()));
        }
    }

    public void setMovies(ArrayList<MovieModel> movies) {
        this.movies = movies;
    }

    public void setStatus(String status) {
        this.status = OrderStatus.valueOf(status);
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setPaidWithLoyaltyPoints(String boolStr) {
        this.paidWithLoyaltyPoints = Boolean.parseBoolean(boolStr);
    }

    public void setPaidWithLoyaltyPoints(boolean bool) {
        this.paidWithLoyaltyPoints = bool;
    }
}
