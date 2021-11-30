package org.videoco.controllers.users;

import javafx.event.ActionEvent;
import org.videoco.controllers.admin.AdminController;
import org.videoco.controllers.database.DatabaseController;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;
import org.videoco.views.ViewType;


public abstract class UserController extends DatabaseController {
    private static UserModel currentUser;
    public static AuthPackage registerUser(String name, String email, String password, String employeeRegistrationCode) {
        DatabaseController customerController = new CustomerController();
        DatabaseController employeeController = new EmployeeController();

        email = email.strip().toLowerCase();
        if (customerController.hasDatabaseEntry(email) || employeeController.hasDatabaseEntry(email)) return new AuthPackage(null, "That username is taken. Try another.");

        UserFactory userFactory = new UserFactory();
        UserModel user;
        if (!employeeRegistrationCode.isBlank()) {
            if (!AdminController.isValidEmployeeRegistrationCode(employeeRegistrationCode))
                return new AuthPackage(null, "Invalid employee registration code.");

            AdminController.useEmployeeRegisterCode(employeeRegistrationCode);
            userFactory.setCommonAttrs(new EmployeeModel().getType(), name, getNewID(EmployeeController.GlobalIDFieldName), email, password);
            user = userFactory.createModel();
            employeeController.addModelToDB(user);
        } else {
            userFactory.setCommonAttrs(new CustomerModel().getType(), name, getNewID(CustomerController.GlobalIDFieldName), email, password);
            user = userFactory.createModel();
            customerController.addModelToDB(user);
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

    public static void logout(ActionEvent event) {
        if (currentUser == null) return;
        try {
            currentUser.createController().transition(ViewType.LOGIN, event);
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

