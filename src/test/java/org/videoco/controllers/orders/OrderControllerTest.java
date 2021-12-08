package org.videoco.controllers.orders;

import com.opencsv.CSVWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.videoco.controllers.movies.MovieController;
import org.videoco.controllers.users.AuthenticationTest;
import org.videoco.controllers.users.CustomerController;
import org.videoco.controllers.users.UserType;
import org.videoco.factories.OrderFactory;
import org.videoco.factories.UserFactory;
import org.videoco.models.MovieModel;
import org.videoco.models.orders.OrderModel;
import org.videoco.models.orders.OrderStatus;
import org.videoco.models.users.CustomerModel;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class OrderControllerTest {
    public static OrderController orderController;
    @BeforeEach
    public void initializeUser() throws IOException {
        new AuthenticationTest().setup();
        CSVWriter writerOrders = new CSVWriter(new FileWriter(OrderController.databasePath, false));
        new OrderController().clearCache();
        writerOrders.close();

        UserFactory factory = new UserFactory();
        factory.setName("stanley ihe");
        factory.setEmail("ihe@mail.com");
        factory.setPassword("password");
        factory.setType(UserType.CUSTOMER);
        CustomerModel customer = (CustomerModel) new CustomerController().addDBRecord(factory);
        orderController = new OrderController(customer);
    }

    @Test
    public void testAddToCart() {
        MovieController movieController = new MovieController(orderController.getUser());
        MovieModel movie = (MovieModel) movieController.getModels().get(0);
        MovieModel movie2 = (MovieModel) movieController.getModels().get(1);

        OrderController localOrderController = new OrderController(orderController.getUser());
        movie.setAmountInStock("1");
        assertTrue(localOrderController.addToCart((MovieModel) new MovieController().getModelFromDB(movie.getDatabaseKey())));
        movie.setAmountInStock("0");
        assertFalse(localOrderController.addToCart((MovieModel) new MovieController().getModelFromDB(movie.getDatabaseKey())), "Should not be able to add movie out of stock");

        assertTrue(localOrderController.addToCart((MovieModel) new MovieController().getModelFromDB(movie2.getDatabaseKey())));
        assertTrue(localOrderController.getDraftOrder().getMovies().contains(movie));
    }

    @Test
    public void testRemoveFromCart() {
        MovieController movieController = new MovieController(orderController.getUser());
        OrderController localOrderController = new OrderController(orderController.getUser());

        MovieModel movie = (MovieModel) movieController.getModels().get(0);
        movie.setAmountInStock("1");
        assertFalse(localOrderController.removeFromCart(movie));
        localOrderController.addToCart(movie);
        assertTrue(localOrderController.removeFromCart(movie));
    }

    @Test
    public void testPlaceOrder() {
        MovieController movieController = new MovieController(orderController.getUser());

        MovieModel movie = (MovieModel) movieController.getModels().get(0);
        movie.setAmountInStock("1");

        assertNotNull(orderController.placeOrder(false), "Error message expected when no items added to cart.");
        orderController.addToCart(movie);
        OrderModel order = orderController.getDraftOrder();
        assertNull(orderController.placeOrder(false), "No error messages expected.");
        order = (OrderModel) orderController.getModelFromDB(order.getDatabaseKey());
        assertEquals(order.getStatus(), OrderStatus.PLACED);

        assertFalse(orderController.addToCart((MovieModel) movieController.getModelFromDB(movie.getDatabaseKey())), "Users should not be able to add out-of-stock movies to their order");
    }

    @Test
    public void testIncrementLoyaltyPoints() {
        MovieController movieController = new MovieController(orderController.getUser());

        MovieModel movie = (MovieModel) movieController.getModels().get(0);
        movie.setAmountInStock("1");

        CustomerModel customer = (CustomerModel) orderController.getUser();
        int initialLoyaltyPoints = 5;
        customer.setLoyaltyPoints(String.valueOf(initialLoyaltyPoints));
        orderController.addToCart(movie);
        orderController.placeOrder(true);
        assertTrue(OrderController.loyaltyPointsPerOrder>0, "User should be charged a certain amount when they buy movies using loyalty points.");
        customer = (CustomerModel) new CustomerController().getModelFromDB(customer.getDatabaseKey());
        assertEquals(initialLoyaltyPoints-OrderController.loyaltyPointsPerOrder, Integer.parseInt(customer.getLoyaltyPoints()), "The loyalty points of the customer should be decremented after placing an order using loyalty points.");
    }

    @Test
    public void testDecrementLoyaltyPoints() {
        MovieController movieController = new MovieController(orderController.getUser());

        MovieModel movie = (MovieModel) movieController.getModels().get(0);
        movie.setAmountInStock("1");

        CustomerModel customer = (CustomerModel) orderController.getUser();
        int initialLoyaltyPoints = 5;
        customer.setLoyaltyPoints(String.valueOf(initialLoyaltyPoints));
        orderController.addToCart(movie);
        OrderModel order = orderController.getDraftOrder();
        orderController.placeOrder(true);
        customer = (CustomerModel) new CustomerController().getModelFromDB(customer.getDatabaseKey());
        assertEquals(initialLoyaltyPoints-OrderController.loyaltyPointsPerOrder, Integer.parseInt(customer.getLoyaltyPoints()), "The loyalty points of the customer should be decremented after placing an order using loyalty points.");

        orderController.cancelOrder(order.getDatabaseKey());
        customer = (CustomerModel) new CustomerController().getModelFromDB(customer.getDatabaseKey());
        assertEquals(initialLoyaltyPoints, Integer.parseInt(customer.getLoyaltyPoints()), "The loyalty points of the customer should be decremented after placing an order using loyalty points.");
    }

    @Test
    public void testCancelOrder() {
        MovieController movieController = new MovieController(orderController.getUser());

        MovieModel movie = (MovieModel) movieController.getModels().get(0);
        movie.setAmountInStock("1");
        orderController.addToCart(movie);
        OrderModel order = orderController.getDraftOrder();
        orderController.placeOrder(false);
        assertNull(orderController.cancelOrder(order.getDatabaseKey()));
        order = (OrderModel) orderController.getModelFromDB(order.getDatabaseKey());
        assertEquals(order.getStatus(), OrderStatus.CANCELLED);
    }

    @Test
    public void testCacheDatabase() {
        MovieController movieController = new MovieController(orderController.getUser());

        MovieModel movie = (MovieModel) movieController.getModels().get(0);
        movie.setAmountInStock("1");
        orderController.addToCart(movie);
        OrderModel order = orderController.getDraftOrder();
        orderController.placeOrder(false);
        orderController.getCache().clear();
        orderController.cacheDatabase(new OrderFactory());
        assertNotNull(orderController.getModelFromDB(order.getDatabaseKey()));
    }
}
