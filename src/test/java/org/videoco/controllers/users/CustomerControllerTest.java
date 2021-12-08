package org.videoco.controllers.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.CustomerModel;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerControllerTest {
    CustomerModel customerModel;
    @BeforeEach
    public void setup() {
        new AuthenticationTest().setup();

        UserFactory factory = new UserFactory();
        factory.setName("stanley ihe");
        factory.setEmail("ihe@mail.com");
        factory.setPassword("password");
        factory.setType(UserType.CUSTOMER);
        this.customerModel = (CustomerModel) new CustomerController().addDBRecord(factory);
    }

    @Test
    public void testCacheDatabase() {
        CustomerController controller = (CustomerController) this.customerModel.createController();
        controller.getCache().clear();
        controller.cacheDatabase(new UserFactory());
        assertTrue(controller.getCache().containsKey(this.customerModel.getDatabaseKey()), "Customer model should be cached in customer database cache.");
    }
}
