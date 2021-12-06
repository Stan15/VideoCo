package org.videoco.views.users;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.videoco.controllers.admin.AdminController;
import org.videoco.controllers.admin.SystemAdminController;
import org.videoco.controllers.users.UserType;
import org.videoco.models.orders.OrderStatus;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.EmployeeModel;
import org.videoco.views.ViewController;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeIDGeneratorVC extends ViewController implements Initializable {
    @FXML
    ChoiceBox<UserType> employeeTypes;
    @FXML
    TextField registrationCode;

    @FXML
    public void generateEmployeeRegistrationCode() {
        if (((EmployeeModel) this.user).getAdminStatus().level< AdminStatus.SYSTEM_ADMIN.level) {
            this.createAlert(Alert.AlertType.ERROR, "Invalid Operation. Unauthorized Access.");
        }
        String code = new SystemAdminController(this.user).createEmployeeRegistrationCode(this.employeeTypes.getSelectionModel().getSelectedItem());
        this.registrationCode.setText(code);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employeeTypes.setValue(UserType.EMPLOYEE);
        for (UserType type : UserType.values()) {
            if (type!=UserType.CUSTOMER)
                employeeTypes.getItems().add(type);
        }
    }
}
