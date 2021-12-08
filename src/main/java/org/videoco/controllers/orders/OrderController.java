package org.videoco.controllers.orders;

import org.videoco.controllers.database.MetadataFields;
import org.videoco.controllers.database.DatabaseController;
import org.videoco.controllers.movies.MovieController;
import org.videoco.controllers.users.CustomerController;
import org.videoco.factories.Factory;
import org.videoco.factories.OrderFactory;
import org.videoco.models.Model;
import org.videoco.models.MovieModel;
import org.videoco.models.orders.OrderModel;
import org.videoco.models.orders.OrderStatus;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.UserModel;
import org.videoco.utils.Utils;
import org.videoco.utils.observer.Observer;
import org.videoco.utils.observer.events.OrderEvent;
import org.videoco.utils.observer.events.VCOEvent;
import org.videoco.views.ViewEnum;

import java.util.*;

public class OrderController extends DatabaseController {
    private List<OrderModel> userOrders = new ArrayList<>();
    public static final int loyaltyPointsPerOrder = 1;
    public OrderController() {}
    public OrderController(UserModel user) {
        super();
        this.setUser(user);
    }

    @Override
    public void setUser(UserModel user) {
        this.user = user;
        if (this.user==null) return;
        this.userOrders = new ArrayList<>();
        for (Model model : this.getCache().values()) {
            OrderModel order = ((OrderModel) model);
            if (order.getCustomerID().equals(user.getID()))
                this.userOrders.add(order);
        }
    }

    public boolean addToCart(MovieModel movie) {
        if (this.user==null || !(user instanceof CustomerModel)) throw new Error("Only customers can remove items from cart.");
        OrderModel draftOrder = this.getDraftOrder();
        if (Integer.parseInt(movie.getAmountInStock())<=0) return false;
        if (draftOrder==null) {
            OrderFactory factory = new OrderFactory();
            ArrayList<MovieModel> movies = new ArrayList<>();
            movies.add(movie);
            factory.setMovies(movies);
            factory.setCustomerID(this.user.getID());
            factory.setStatus(OrderStatus.DRAFT);
            this.focusModel = this.addDBRecord(factory);
        }else {
            OrderFactory factory = new OrderFactory(draftOrder);
            if (!factory.addMovie(movie)) return false;
            this.focusModel = this.updateRecord(draftOrder.getDatabaseKey(), factory);
        }
        emit(OrderEvent.ADD_ITEM, movie);
        return true;
    }

    public boolean removeFromCart(MovieModel movie) {
        if (this.user==null || !(user instanceof CustomerModel)) {
            new Exception("Only customers can add items to cart.").printStackTrace();
            return false;
        }
        OrderModel draftOrder = this.getDraftOrder();
        if (draftOrder==null) return false;
        else {
            OrderFactory factory = new OrderFactory(draftOrder);
            if (!factory.removeMovie(movie)) return false;
            this.focusModel = this.updateRecord(draftOrder.getDatabaseKey(), factory);
        }
        emit(OrderEvent.REMOVE_ITEM, movie);
        return true;
    }

    public String placeOrder(boolean useLoyaltyPoints) {
        OrderModel draftOrder = this.getDraftOrder();
        if (draftOrder==null || draftOrder.getMovies().size()<=0) return "No movies added to order.";
        draftOrder.setStatus(OrderStatus.PLACED);
        draftOrder.setDateOrdered(new Date());
        OrderModel order = (OrderModel) this.updateRecord(draftOrder.getDatabaseKey(), new OrderFactory(draftOrder));
        MovieController controller = new MovieController(this.user);
        for (MovieModel movie : order.getMovies()) {
            if (Integer.parseInt(movie.getAmountInStock())<=0) return "Movie \""+movie.getTitle()+"\" is out of stock";
        }
        for (MovieModel movie : order.getMovies()) {
            controller.decrementMovieStock(movie);
        }
        if (useLoyaltyPoints) {
            OrderFactory factory = new OrderFactory(order);
            ((CustomerController) this.user.createController()).decrementLoyaltyPoints(loyaltyPointsPerOrder);
            factory.setPaidWithLoyaltyPoints("true");
            this.updateRecord(order.getDatabaseKey(), factory);
        }
        this.focusModel = null;

        emit(OrderEvent.PLACED, order);
        return null;
    }

    public String cancelOrder(String databaseKey) {
        OrderModel order = (OrderModel) this.getModelFromDB(databaseKey);
        if (order==null) return "Order does not exist on our database.";
        if (order.getStatus().ordering>=OrderStatus.DELIVERED.ordering) {
            return "Cannot cancel an order that has already been delivered.";
        }
        order.setStatus(OrderStatus.CANCELLED);
        order = (OrderModel) this.updateRecord(order.getDatabaseKey(), new OrderFactory(order));
        MovieController controller = new MovieController(this.user);
        for (MovieModel movie : order.getMovies()) {
            controller.incrementMovieStock(movie);
        }
        if (order.getPaidWithLoyaltyPoints()) ((CustomerController) this.user.createController()).incrementLoyaltyPoints(loyaltyPointsPerOrder);
        emit(OrderEvent.CANCELLED, order);
        return null;
    }

    public OrderModel getDraftOrder() {
        if (this.user==null || !(user instanceof CustomerModel)) return null;
        if (this.focusModel!=null) return (OrderModel) this.focusModel;
        for (OrderModel order : this.userOrders) {
            if (order.getStatus()== OrderStatus.DRAFT) {
                this.focusModel = order;
                return order;
            }
        }
        return null;
    }

    public List<OrderModel> getUserOrders() {
        return this.userOrders;
    }


    //-------Controller functions-----------
    @Override
    public boolean isValidTransition(ViewEnum view) {
        if (this.user==null) return false;
        return true;
        //TODO
//        return false;
    }

    //-------Database Functions---------------
    public static HashMap<String, Model> cache = new HashMap<>();
    public static final String databasePath = Utils.getResourcePath("/org.videoco/databases/orders.csv");
    public static boolean isCached = false;
    public static String[] databaseHeader = new String[]{"id", "customer-id", "movies", "status", "date-ordered", "date-delivered", "paid-with-loyalty-points"};
    @Override
    public String getGlobalIDFieldName() {
        return MetadataFields.GLOBAL_ORDER_ID.name();
    }

    @Override
    public Model addDBRecord(Factory factory) {
        if (this.user==null || !(user instanceof CustomerModel)) {
            new Exception("Only customers can add orders to the database.").printStackTrace();
            return null;
        }
        Model model = super.addDBRecord(factory);
        if (model!=null) this.userOrders.add((OrderModel) model);
        return model;
    }

    @Override
    public boolean removeDBRecord(String databaseKey) {
        OrderModel order = (OrderModel) this.getModelFromDB(databaseKey);
        boolean success = super.removeDBRecord(databaseKey);
        if (success) this.userOrders.remove(order);
        return success;
    }

    @Override
    public void readRecordIntoFactory(String[] record, Factory factory) throws Exception {
        OrderFactory orderFactory = (OrderFactory) factory;
        if (record.length!=databaseHeader.length) throw new Exception("Invalid record. Cannot create an Order instance.");
        for (int i=0; i<record.length; i++) {
            switch (databaseHeader[i]) {
                case "id" -> orderFactory.setID(record[i].strip());
                case "customer-id" -> orderFactory.setCustomerID(record[i].strip());
                case "movies" -> orderFactory.setMovies(record[i].strip());
                case "status" -> orderFactory.setStatus(record[i].strip());
                case "date-ordered" -> orderFactory.setDateOrdered(record[i].strip());
                case "date-delivered" -> orderFactory.setDateDelivered(record[i].strip());
                case "paid-with-loyalty-points" -> orderFactory.setPaidWithLoyaltyPoints(record[i].strip());
            }
        }
    }

    @Override
    public String[] createRecord(Model model) {
        OrderModel order = (OrderModel) model;
        return new String[]{order.getID(), order.getCustomerID(), order.getMoviesStr(), order.getStatus().name(), Utils.convertDateToDTString(order.getDateOrdered()), Utils.convertDateToDTString(order.getDateDelivered()), String.valueOf(order.getPaidWithLoyaltyPoints())};
    }

    @Override
    public String getDatabasePath() {
        return OrderController.databasePath;
    }

    @Override
    public String[] getDatabaseHeader() {
        return OrderController.databaseHeader;
    }

    @Override
    public HashMap<String, Model> getCache() {
        if (OrderController.isCached) return OrderController.cache;
        OrderController.isCached = true;
        this.cacheDatabase(new OrderFactory());
        return OrderController.cache;
    }

    @Override
    public void clearCache() {
        OrderController.cache.clear();
        OrderController.isCached = false;
    }

    //---------Observer functions--------------
    private static final Map<VCOEvent, List<Observer<VCOEvent, Model>>> OBSERVERS = new HashMap<>();
    @Override
    public Map<VCOEvent, List<Observer<VCOEvent, Model>>> getObservers() {
        return OrderController.OBSERVERS;
    }
}
