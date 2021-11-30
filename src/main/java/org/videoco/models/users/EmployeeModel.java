package org.videoco.models.users;

import org.videoco.controllers.users.EmployeeController;

public class EmployeeModel extends UserModel {
    private String adminStatus;
    public EmployeeModel() {
        this.type = "Employee";
    }
    public EmployeeModel(String type, String name, String id, String email, String password) {
        super(name, id, email, password);
        this.type = type;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String status) {
        this.adminStatus = status;
    }

    @Override
    public EmployeeController createController() {
        //TODO switch statement to decide which controller to use
        EmployeeController controller = new EmployeeController();
        controller.setFocusModel(this);
        return controller;
    }
}