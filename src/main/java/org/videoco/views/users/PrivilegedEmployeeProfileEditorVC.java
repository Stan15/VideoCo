package org.videoco.views.users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;

public class PrivilegedEmployeeProfileEditorVC extends UserProfileVC {
    @FXML ChoiceBox<AdminStatus> adminStatusChoiceBox;

    @Override
    public void updateAccount(ActionEvent event) {
        if (!(this.model instanceof UserModel)) this.model = this.user;
        if (this.model instanceof EmployeeModel e && e.getAdminStatus()==AdminStatus.SYSTEM_ADMIN && adminStatusChoiceBox.getSelectionModel().getSelectedItem()!=AdminStatus.SYSTEM_ADMIN) {
            this.createAlert(Alert.AlertType.ERROR, "Cannot change system admin.");
            return;
        }
        super.updateAccount(event);
    }

    @Override
    public void setupFields() {
        super.setupFields();
        adminStatusChoiceBox.getItems().removeIf((c) -> true);
        for (AdminStatus status : AdminStatus.values()) {
            adminStatusChoiceBox.getItems().add(status);
        }
        adminStatusChoiceBox.getSelectionModel().select(((EmployeeModel) this.user).getAdminStatus());
    }

    @Override
    public void setupUserFactory(UserFactory factory) {
        super.setupUserFactory(factory);
        factory.setAdminStatus(this.adminStatusChoiceBox.getSelectionModel().getSelectedItem());
    }
}
