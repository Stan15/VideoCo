package org.videoco.controllers.users;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.videoco.controllers.admin.AdminController;
import org.videoco.controllers.database.DatabaseController;
import org.videoco.controllers.users.employee.EmployeeController;
import org.videoco.factories.UserFactory;
import org.videoco.models.Model;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;
import org.videoco.views.ViewEnum;
import org.videoco.views.sidebar.info.SidebarInfoItem;
import org.videoco.views.sidebar.switcher.SidebarSwitcherItem;

import java.util.List;
import java.util.Objects;


public abstract class UserController extends DatabaseController {
    private static UserModel currentUser;

    public abstract void transitionToHomeView(Event event);
    public abstract List<SidebarSwitcherItem> getSidebarSwitcherItems();
    public abstract List<SidebarInfoItem> getSidebarInfoItems();

    public static EmployeeModel getSystemAdminModel() {
        for (Model user : new EmployeeController().getModels()) {
            if (user instanceof EmployeeModel e && e.getAdminStatus()== AdminStatus.SYSTEM_ADMIN)
                return ((EmployeeModel) user);
        }
        return null;
    }

    public static AuthPackage registerUser(String name, String email, String password, String employeeRegistrationCode) {
        DatabaseController customerController = new CustomerController();
        DatabaseController employeeController = new EmployeeController();

        email = email.strip().toLowerCase();
        if (customerController.hasDatabaseEntry(email) || employeeController.hasDatabaseEntry(email)) return new AuthPackage(null, "That username is taken. Try another.");

        UserFactory userFactory = new UserFactory();
        UserModel user;
        userFactory.setCommonAttrs(name, email, password);
        employeeRegistrationCode = employeeRegistrationCode.strip();
        if (!employeeRegistrationCode.isBlank()) {
            if (!AdminController.isValidEmployeeRegistrationCode(employeeRegistrationCode))
                return new AuthPackage(null, "Invalid employee registration code.");

            AdminController.useEmployeeRegisterCode(employeeRegistrationCode);
            userFactory.setType(AdminController.getEmployeeType(employeeRegistrationCode));
            user = (UserModel) employeeController.addDBRecord(userFactory);
        } else {
            userFactory.setType(new CustomerModel().getType());
            user = (UserModel) customerController.addDBRecord(userFactory);
        }
        currentUser = user;
        return new AuthPackage(user, "");
    }

    public static AuthPackage login(String email, String password) {
        DatabaseController customerController = new CustomerController();
        DatabaseController employeeController = new EmployeeController();

        email = email.strip().toLowerCase();
        if (!customerController.hasDatabaseEntry(email) && !employeeController.hasDatabaseEntry(email)) return new AuthPackage(null, "Couldn't find your account.");
        UserModel user = (UserModel) customerController.getModelFromDB(email);
        if (user==null) user = (UserModel) employeeController.getModelFromDB(email);
        if (!user.getPassword().equals(password)) return new AuthPackage(null, "Wrong password. Try again.");

        currentUser = user;
        return new AuthPackage(user, "");
    }

    public static void transitionToHomepage(Event event) {
        if (currentUser==null) return;
        currentUser.createController().transitionToHomeView(event);
    }

    public static void logout(ActionEvent event) {
        //we do not use Controller.transition() because it retains a reference to the user.
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(UserController.class.getResource(ViewEnum.LOGIN.src)));
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            currentUser = null;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class AuthPackage {
        String errorMsg;
        UserModel user;
        AuthPackage(UserModel user, String errorMsg) {
            this.user = user;
            this.errorMsg = errorMsg;
        }
        public String getErrorMsg() {
            return this.errorMsg;
        }
        public UserModel getUserModel() {
            return this.user;
        }
    }
}

