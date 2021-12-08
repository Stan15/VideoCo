package org.videoco.controllers.queriers;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.videoco.controllers.movies.MovieController;
import org.videoco.controllers.movies.MovieQuerier;
import org.videoco.controllers.movies.MovieQuery;
import org.videoco.controllers.orders.OrderController;
import org.videoco.controllers.orders.OrderControllerTest;
import org.videoco.controllers.orders.OrderQuerier;
import org.videoco.controllers.orders.OrderQuery;
import org.videoco.controllers.users.*;
import org.videoco.controllers.users.employee.EmployeeController;
import org.videoco.controllers.users.employee.EmployeeQuerier;
import org.videoco.controllers.users.employee.EmployeeQuery;
import org.videoco.models.MovieModel;
import org.videoco.models.orders.OrderStatus;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QuerierTest {
    @BeforeAll
    public static void setupSystemAdmin() {
        new AuthenticationTest().setSysAdminController();
    }

    @Test
    public void testMovieQuerier() {
        MovieQuerier querier = new MovieQuerier(new MovieController());
        querier.setQuery(new MovieQuery("spid"));
        ObservableList<MovieModel> list = querier.getQueriedList();
        assertTrue(list.get(0).getTitle().toLowerCase().contains("spid"));
    }

    @Test
    public void testEmployeeQuerier() {
        new AuthenticationTest().setup();
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        UserController.AuthPackage authPackage = UserController.registerUser(name, email, password, AuthenticationTest.sysAdminController.createEmployeeRegistrationCode(UserType.EMPLOYEE));
        EmployeeQuerier querier = new EmployeeQuerier(new EmployeeController((EmployeeModel) authPackage.getUserModel()));
        querier.setQuery(new EmployeeQuery("hei"));
        ObservableList<UserModel> list = querier.getQueriedList();
        assertTrue(list.get(0).getName().toLowerCase().contains("stanl"));
    }

    @Test
    public void testUserQuerier() {
        new AuthenticationTest().setup();
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        UserController.AuthPackage authPackage = UserController.registerUser(name, email, password, "");
        UserQuerier querier = new UserQuerier(new CustomerController(authPackage.getUserModel()));
        querier.setQuery(new UserQuery("hei"));
        ObservableList<UserModel> list = querier.getQueriedList();
        assertTrue(list.get(0).getName().toLowerCase().contains("stanl"));
    }

    @Test
    public void testOrderQuerier() {
        MovieController movieController = new MovieController(OrderControllerTest.orderController.getUser());

        MovieModel movie = (MovieModel) movieController.getModels().get(0);
        movie.setAmountInStock("1");
        OrderControllerTest.orderController.addToCart(movie);

        OrderQuerier querier = new OrderQuerier(new OrderController());
        querier.setQuery(new OrderQuery(movie.getTitle().substring(0, 4), OrderStatus.DRAFT.name()));
        assertEquals(querier.getQueriedList().get(0).getMovies().get(0).getTitle(), movie.getTitle());
    }
}
