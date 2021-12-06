package org.videoco.views.users;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import org.videoco.controllers.admin.SystemAdminController;
import org.videoco.controllers.users.UserQuerier;
import org.videoco.controllers.users.employee.EmployeeController;
import org.videoco.controllers.users.UserType;
import org.videoco.controllers.users.employee.EmployeeQuerier;
import org.videoco.controllers.users.employee.EmployeeQuery;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;
import org.videoco.views.DatabaseBrowser;
import org.videoco.views.ViewEnum;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeBrowserVC extends DatabaseBrowser<UserModel> {
    @FXML ChoiceBox<UserType> employeeType;
    @FXML ChoiceBox<String> adminStatusChoiceBox;
    @FXML TableColumn<EmployeeModel, String> id;
    @FXML TableColumn<EmployeeModel, String> name;
    @FXML TableColumn<EmployeeModel, String> email;
    @FXML TableColumn<EmployeeModel, String> adminStatusTableColumn;

    @Override
    public void setUser(UserModel user) {
        this.querier = this.createQuerier();
        super.setUser(user);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        employeeType.getItems().removeIf((c) -> true);
        employeeType.setValue(UserType.EMPLOYEE);
        for (UserType type : UserType.values()) {
            if (type==UserType.CUSTOMER) continue;
            employeeType.getItems().add(type);
        }
        employeeType.getSelectionModel().selectedItemProperty().addListener(
                (observable) -> this.updateQuery()
        );

        adminStatusChoiceBox.getItems().removeIf((c) -> true);
        adminStatusChoiceBox.setValue("ALL");
        adminStatusChoiceBox.getItems().add("ALL");
        for (AdminStatus status : AdminStatus.values()) {
            adminStatusChoiceBox.getItems().add(status.name());
        }
        adminStatusChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable) -> this.updateQuery()
        );
    }

    @Override
    protected void setColumns() {
        id.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getID()));
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        email.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEmail()));
        adminStatusTableColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAdminStatus().name()));
    }

    @Override
    protected UserQuerier createQuerier() {
        return new EmployeeQuerier(new EmployeeController((EmployeeModel) this.user));
    }

    @Override
    protected EmployeeQuery getQuery() {
        return new EmployeeQuery(this.searchBar.getText(), this.employeeType.getSelectionModel().getSelectedItem(), this.adminStatusChoiceBox.getSelectionModel().getSelectedItem());
    }

    @Override
    public void onModelSelect(MouseEvent event) {
        SystemAdminController controller = new SystemAdminController(this.user);
        controller.setFocusModel(this.tableView.getSelectionModel().getSelectedItem());
        controller.createPopup(ViewEnum.PRIVILEGED_EMPLOYEE_PROFILE_EDITOR, event);
    }
}
