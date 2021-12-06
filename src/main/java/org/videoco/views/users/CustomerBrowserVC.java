package org.videoco.views.users;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import org.videoco.controllers.admin.SystemAdminController;
import org.videoco.controllers.users.CustomerController;
import org.videoco.controllers.users.UserQuerier;
import org.videoco.controllers.users.UserQuery;
import org.videoco.controllers.users.UserType;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;
import org.videoco.views.DatabaseBrowser;
import org.videoco.views.ViewEnum;


public class CustomerBrowserVC extends DatabaseBrowser<UserModel> {
    @FXML TableColumn<CustomerModel, String> id;
    @FXML TableColumn<CustomerModel, String> name;
    @FXML TableColumn<CustomerModel, String> email;

    @Override
    public void setUser(UserModel user) {
        this.querier = this.createQuerier();
        super.setUser(user);
    }

    @Override
    protected void setColumns() {
        id.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getID()));
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        email.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getEmail()));
    }

    @Override
    protected UserQuerier createQuerier() {
        return new UserQuerier(new CustomerController(this.user));
    }

    @Override
    protected UserQuery getQuery() {
        return new UserQuery(this.searchBar.getText(), UserType.CUSTOMER);
    }

    @Override
    public void onModelSelect(MouseEvent event) {
        CustomerController controller = new CustomerController(this.user);
        controller.setFocusModel(this.tableView.getSelectionModel().getSelectedItem());
        controller.createPopup(ViewEnum.USER_PROFILE, event);
    }
}
