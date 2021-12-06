package org.videoco.controllers.users.employee;

import org.videoco.controllers.users.UserController;
import org.videoco.controllers.users.UserQuerier;
import org.videoco.controllers.users.UserType;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;
import org.videoco.utils.database_queries.DBQuerier;

import java.util.Objects;

public class EmployeeQuerier extends UserQuerier {
    public EmployeeQuerier(EmployeeController controller, ListSource<UserModel> source) {
        super(controller, new EmployeeQuery(""), source);
    }

    public EmployeeQuerier(EmployeeController controller) {
        this(controller, DBQuerier::defaultListSource);
    }

    @Override
    public boolean filterPredicate(UserModel user) {
        AdminStatus status = ((EmployeeModel) user).getAdminStatus();
        boolean filterStatus = ((EmployeeQuery) this.query).getAdminStatus().equals("ALL") || Objects.equals(((EmployeeQuery) this.query).getAdminStatus(), status.name());
        return super.filterPredicate(user) && filterStatus;
    }
}
