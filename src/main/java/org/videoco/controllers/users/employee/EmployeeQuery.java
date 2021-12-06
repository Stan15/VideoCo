package org.videoco.controllers.users.employee;

import org.videoco.controllers.users.UserQuery;
import org.videoco.controllers.users.UserType;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;

import java.util.Objects;

public class EmployeeQuery extends UserQuery {
    private final String adminStatus;
    public EmployeeQuery(String content) {
        this(content, UserType.EMPLOYEE, "ALL");
    }
    public EmployeeQuery(String content, UserType type, String adminStatus) {
        super(content, type);
        this.adminStatus = adminStatus;
    }

    @Override
    public int matchScore(UserModel user) {
        if (!Objects.equals(this.adminStatus, "ALL") && !this.adminStatus.equals(((EmployeeModel) user).getAdminStatus().name())) return 0;
        return super.matchScore(user);
    }

    public String getAdminStatus() {
        return adminStatus;
    }
}
