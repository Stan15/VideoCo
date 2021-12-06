package org.videoco.models.users;

import org.videoco.controllers.users.employee.EmployeeController;
import org.videoco.controllers.users.UserController;
import org.videoco.controllers.users.UserType;

public class EmployeeModel extends UserModel {
    private AdminStatus adminStatus;
    public EmployeeModel() {
        this.type = UserType.EMPLOYEE;
    }
    public EmployeeModel(UserType type, String name, String id, String email, String password) {
        super(name, id, email, password);
        if (type==UserType.CUSTOMER) {
            new Exception("Wrong user type used to instantiate employee.").printStackTrace();
            this.type = UserType.EMPLOYEE;
        }else {
            this.type = type;
        }
        this.adminStatus = AdminStatus.NON_ADMIN;
    }

    public AdminStatus getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(AdminStatus status) {
        if (status==null) this.adminStatus = AdminStatus.NON_ADMIN;
        else this.adminStatus = status;
    }

    @Override
    public UserController createController() {
        //TODO switch statement to decide which controller to use
        EmployeeController controller = new EmployeeController();
        controller.setFocusModel(this);
        controller.setUser(this);
        return controller;
    }
}

