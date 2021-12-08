package org.videoco.controllers.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.videoco.controllers.users.employee.EmployeeController;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.EmployeeModel;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeControllerTest {
    EmployeeModel employeeModel;
    @BeforeEach
    public void setup() {
        new AuthenticationTest().setup();

        UserFactory factory = new UserFactory();
        factory.setName("stanley ihe");
        factory.setEmail("ihe@mail.com");
        factory.setPassword("password");
        factory.setType(UserType.EMPLOYEE);
        this.employeeModel = (EmployeeModel) new EmployeeController().addDBRecord(factory);
    }
    @Test
    public void testCacheDatabase() {
        EmployeeController controller = (EmployeeController) this.employeeModel.createController();
        controller.getCache().clear();
        controller.cacheDatabase(new UserFactory());
        assertTrue(controller.getCache().containsKey(this.employeeModel.getDatabaseKey()), "Employee model should be cached in customer database cache.");
    }
}
