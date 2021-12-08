package org.videoco.controllers.users;

import com.opencsv.CSVWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.videoco.controllers.admin.AdminController;
import org.videoco.controllers.admin.SystemAdminController;
import org.videoco.controllers.database.DatabaseController;
import org.videoco.controllers.orders.OrderController;
import org.videoco.controllers.users.employee.EmployeeController;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;
import org.videoco.utils.Utils;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {
    public static SystemAdminController sysAdminController;
    @BeforeAll
    static void pointToTestDatabases() {
        CustomerController.databasePath = Utils.getResourcePath("/org.videoco/databases/customers.csv");//.replaceAll("(?<=[/\\\\])databases(?=[/\\\\])", "test-databases");
        EmployeeController.databasePath = Utils.getResourcePath("/org.videoco/databases/employees.csv");//.replaceAll("(?<=[/\\\\])databases(?=[/\\\\])", "test-databases");
        DatabaseController.metadataPath = Utils.getResourcePath("/org.videoco/databases/metadata.json");//.replaceAll("(?<=[/\\\\])databases(?=[/\\\\])", "test-databases");
    }
    @BeforeEach
    public void setup() {
        this.resetDatabases();
        this.setSysAdminController();
    }

    public void setSysAdminController() {
        UserFactory factory = new UserFactory();
        factory.setName("stanley ihe");
        factory.setEmail("stan@mail.com");
        factory.setPassword("password");
        factory.setType("EMPLOYEE");
        factory.setAdminStatus("SYSTEM_ADMIN");
        EmployeeModel employee = (EmployeeModel) new EmployeeController().addDBRecord(factory);
        sysAdminController = new SystemAdminController(employee);
    }
    public void resetDatabases() {
        try {
            CSVWriter writerCustomer = new CSVWriter(new FileWriter(CustomerController.databasePath, false));
            CSVWriter writerEmployee = new CSVWriter(new FileWriter(EmployeeController.databasePath, false));
            new CustomerController().clearCache();
            new EmployeeController().clearCache();
            writerCustomer.close();
            writerEmployee.close();
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void testLogin() {
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        UserController.AuthPackage authPackage = UserController.registerUser(name, email, password, "");

        UserController.AuthPackage package2 = UserController.login(email, password);
        assertEquals(package2.user.getDatabaseKey(), authPackage.user.getDatabaseKey());
    }

    @Test
    public void testInvalidLogin() {
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        UserController.AuthPackage authPackage = UserController.registerUser(name, email, password, "");

        UserController.AuthPackage package2 = UserController.login(email+"hi", password);
        assertNull(package2.getUserModel());
    }

    @Test
    public void testRegisterCustomer() {
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        UserController.registerUser(name, email, password, "");

        String customerDatabase = Utils.readFileToString(CustomerController.databasePath);
        String employeeDatabase = Utils.readFileToString(EmployeeController.databasePath);

        Pattern p = Pattern.compile(name.toLowerCase()+"[^\r\n]*"+email.toLowerCase()+"[^\r\n]*"+password+"[^\r\n]*");
        assertTrue(p.matcher(customerDatabase).find(), "Customer should be registered into customer database.");
        assertFalse(p.matcher(employeeDatabase).find(), "Customer should not be registered into employee database.");

        CustomerController customerController = new CustomerController();
        EmployeeController employeeController = new EmployeeController();
        assertTrue(customerController.hasDatabaseEntry(email.toLowerCase()));
        assertFalse(employeeController.hasDatabaseEntry(email.toLowerCase()));
    }

    @Test
    public void testRegisterExistingCustomer() {
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        UserController.registerUser(name, email, password, "");
        name = "stanford pines";
        password = "mypass345";
        UserController.AuthPackage authPackage =  UserController.registerUser(name, email, password, "");

        assertFalse(authPackage.errorMsg.isBlank(), "An error message should be displayed if a user registers with an existing email.");
        assertNull(authPackage.user, "A user should not be registered when they use an existing email.");

        String database = Utils.readFileToString(CustomerController.databasePath);
        Pattern p = Pattern.compile(name.toLowerCase()+"[^\r\n]*"+email.toLowerCase()+"[^\r\n]*"+password+"[^\r\n]*");
        assertFalse(p.matcher(database).find(), "User should not be registered into the database with an already existing email.");
    }

    @Test
    public void testRegisterEmployee() {
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        //TODO i have to create a systemadmin object so I can use the SystemAdminController
        UserController.registerUser(name, email, password, sysAdminController.createEmployeeRegistrationCode(UserType.EMPLOYEE));
        assertNull(UserController.registerUser(name, email, password, sysAdminController.createEmployeeRegistrationCode(UserType.EMPLOYEE)).user);

        String customerDatabase = Utils.readFileToString(CustomerController.databasePath);
        String employeeDatabase = Utils.readFileToString(EmployeeController.databasePath);

        Pattern p = Pattern.compile(name.toLowerCase()+"[^\r\n]*"+email.toLowerCase()+"[^\r\n]*"+password+"[^\r\n]*");
        assertTrue(p.matcher(employeeDatabase).find(), "Employee should be registered into employee database.");
        assertFalse(p.matcher(customerDatabase).find(), "Employee should not be registered into customer database.");

        EmployeeController employeeController = new EmployeeController();
        CustomerController customerController = new CustomerController();
        assertTrue(employeeController.hasDatabaseEntry(email.toLowerCase()));
        assertFalse(customerController.hasDatabaseEntry(email.toLowerCase()));
    }

    @Test
    public void testRegisterExistingEmployee() {
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        UserController.registerUser(name, email, password, this.sysAdminController.createEmployeeRegistrationCode(UserType.EMPLOYEE));
        name = "stanford pines";
        password = "mypass345";
        UserController.AuthPackage authPackage =  UserController.registerUser(name, email, password, this.sysAdminController.createEmployeeRegistrationCode(UserType.EMPLOYEE));

        assertFalse(authPackage.errorMsg.isBlank(), "An error message should be displayed if a user registers with an existing email.");
        assertNull(authPackage.user, "A user should not be registered when they use an existing email.");

        String database = Utils.readFileToString(EmployeeController.databasePath);
        Pattern p = Pattern.compile(name.toLowerCase()+"[^\r\n]*"+email.toLowerCase()+"[^\r\n]*"+password+"[^\r\n]*");
        assertFalse(p.matcher(database).find(), "User should not be registered into the database with an already existing email.");

    }


    @Test
    public void testInvalidEmployeeRegistrationCode() {
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        String validCode = this.sysAdminController.createEmployeeRegistrationCode(UserType.EMPLOYEE);
        String invalidCode = validCode.replace(validCode.charAt(0), validCode.charAt(0)=='1' ? '0' : '1'); //to ensure that it is different
        UserController.AuthPackage authPackage =  UserController.registerUser(name, email, password, invalidCode);

        assertFalse(authPackage.errorMsg.isBlank(), "An error message should be displayed if a user tries to register with an incorrect employee registration code.");
        assertNull(authPackage.user, "A user should not be registered when they use an incorrect employee registration code.");
    }

    @Test
    public void testNonReusableEmployeeRegistrationCode() {
        String name = "Stanley heio";
        String email = "stantheio@mail.com";
        String password = "password123";
        String employeeRegistrationCode = this.sysAdminController.createEmployeeRegistrationCode(UserType.EMPLOYEE);
        UserController.registerUser(name, email, password, employeeRegistrationCode);
        name = "stanford pines";
        email = "thepines@mail.com";
        password = "mypass345";
        UserController.AuthPackage authPackage =  UserController.registerUser(name, email, password, employeeRegistrationCode);

//        assertFalse(authPackage.errorMsg.isBlank(), "An error message should be displayed if a user tries to register with an already-used employee registration code..");
        assertNull(authPackage.user, "A user should not be registered when they use an already-used employee registration code.");

        String database = Utils.readFileToString(EmployeeController.databasePath);
        Pattern p = Pattern.compile(name.toLowerCase()+"[^\r\n]*"+email.toLowerCase()+"[^\r\n]*"+password+"[^\r\n]*");
        assertFalse(p.matcher(database).find(), "User should not be registered into the database with an already-used employee registration code.");

        EmployeeController employeeController = new EmployeeController();
        assertFalse(employeeController.hasDatabaseEntry(email.toLowerCase()), "An already-used employee registration code should be removed from cache.");
    }
}
