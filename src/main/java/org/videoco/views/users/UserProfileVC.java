package org.videoco.views.users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.videoco.controllers.users.UserController;
import org.videoco.factories.UserFactory;
import org.videoco.models.Model;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;
import org.videoco.views.ViewController;
import org.videoco.views.ViewEnum;

public class UserProfileVC extends ViewController {
    @FXML TextField name;
    @FXML TextField email;
    @FXML TextField password;
    @FXML Label id;

    @FXML ButtonBar buttonBar;
    @FXML Button viewOrdersBttn;
    @FXML Button updateAccountBttn;
    @FXML Button deleteAccountBttn;

    private void setupButtons() {
        if (!(this.model instanceof UserModel)) this.model = this.user;
        this.buttonBar.getButtons().removeIf((b) -> true);
        if (this.model instanceof CustomerModel) {
            this.buttonBar.getButtons().add(viewOrdersBttn);
        }
        this.buttonBar.getButtons().add(updateAccountBttn);
        this.buttonBar.getButtons().add(deleteAccountBttn);
    }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        this.setupButtons();
        this.setupFields();
    }

    public void setupFields() {
        if (!(this.model instanceof UserModel)) this.model = this.user;
        this.name.setText(((UserModel) this.model).getName());
        this.email.setText(((UserModel) this.model).getEmail());
        this.password.setText(((UserModel) this.model).getPassword());
        this.id.setText("ID#"+((UserModel)this.model).getID());
    }

    @Override
    public void setUser(UserModel user) {
        super.setUser(user);
        this.setupButtons();
        this.setupFields();
    }

    @FXML
    public void updateAccount(ActionEvent event) {
        if (!(this.model instanceof UserModel)) this.model = this.user;
        UserController controller = ((UserModel) this.model).createController();
        UserFactory factory = new UserFactory(((UserModel)this.model));
        setupUserFactory(factory);
        String error = factory.findErrorInRequiredFields();
        if (error==null || error.isBlank()) {
            if (controller.hasDatabaseEntry(this.model.getDatabaseKey())) {
                if (controller.updateRecord(this.model.getDatabaseKey(), factory)!=null) {
                    this.createAlert(Alert.AlertType.INFORMATION, "Account information updated.");
                    return;
                } else {
                    error = "Something went wrong... couldn't update your account information.";
                }
            } else {
                error = "Movie could not be found in our database.";
            }
        }
        this.createAlert(Alert.AlertType.ERROR, error);
    }

    public void setupUserFactory(UserFactory factory) {
        factory.setName(this.name.getText());
        factory.setEmail(this.email.getText());
        factory.setPassword(this.password.getText());
    }

    @FXML
    public void viewOrders(ActionEvent event) {
        if (!(this.model instanceof UserModel)) this.model = this.user;
        ((UserModel)this.model).createController().createPopup(ViewEnum.ORDER_BROWSER, event);
    }
    @FXML
    public void deleteAccount(ActionEvent event) {
        if (!(this.model instanceof UserModel)) this.model = this.user;
        if (this.model instanceof EmployeeModel e && e.getAdminStatus() == AdminStatus.SYSTEM_ADMIN) {
            this.createAlert(Alert.AlertType.ERROR, "Cannot delete system admin account.");
            return;
        }
        this.createAlert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete your account?", (result) -> {
            if (result.isPresent() && result.get()== ButtonType.OK) {
                ((UserModel) this.model).createController().removeDBRecord(((UserModel) this.model).getDatabaseKey());
                this.createAlert(Alert.AlertType.INFORMATION, "Account successfully deleted.");
                UserController.logout(event);
            }
        });
    }
}
