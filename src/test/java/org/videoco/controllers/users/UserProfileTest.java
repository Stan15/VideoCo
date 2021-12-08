package org.videoco.controllers.users;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.videoco.Main;
import org.videoco.controllers.users.employee.EmployeeController;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.EmployeeModel;
import org.videoco.views.ViewEnum;
import org.videoco.views.users.UserProfileVC;

import java.util.Objects;

public class UserProfileTest extends ApplicationTest {
    FXMLLoader loader;
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
        UserProfileVC viewController =  loader.getController();
        viewController.setUser(this.customerModel);
        viewController.setModel(this.customerModel);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.loader = new FXMLLoader(Objects.requireNonNull(Main.class.getResource(ViewEnum.USER_PROFILE.src)));
        Parent mainNode = loader.load();
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }
}
