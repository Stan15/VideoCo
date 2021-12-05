package org.videoco.factories;

import org.videoco.controllers.users.EmployeeController;
import org.videoco.controllers.users.UserType;
import org.videoco.models.Model;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;

public class UserFactory extends Factory {
    String id;
    String name;
    String email;
    String password;

    UserType type;
    AdminStatus adminStatus;

    String phoneNumber;
    String bankAccountNumber;
    String loyaltyPoints;
    String shippingAddress;
    String billingAddress;

    public UserFactory(){}
    public UserFactory(UserModel user) {
        super(user);
    }

    public void setCommonAttrs(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @Override
    public void copy(Model model) {
        if (!(model instanceof UserModel m)) return;
        this.setID(m.getID());
        this.setName(m.getName());
        this.setEmail(m.getEmail());
        this.setPhoneNumber(m.getPassword());
        if (m instanceof CustomerModel c) {
            this.setPhoneNumber(c.getPhoneNumber());
            this.setBankAccountNumber(c.getBankAccountNumber());
            this.setLoyaltyPoints(c.getLoyaltyPoints());
            this.setShippingAddress(c.getShippingAddress());
            this.setBillingAddress(c.getBillingAddress());
        } else if (m instanceof EmployeeModel e) {
            this.setType(e.getType());
            this.setAdminStatus(e.getAdminStatus());
        }
    }

    @Override
    public UserModel createModel() {
        if (this.findErrorInRequiredFields()!=null) return null;
        switch (type) {
            case CUSTOMER -> {
                if (this.id==null || !this.id.matches("-?\\d+(\\.\\d+)?")) this.setID(new org.videoco.controllers.users.CustomerController().getNewID());
                CustomerModel cmodel = new CustomerModel(name, id, email, password);
                cmodel.setBillingAddress(this.billingAddress);
                cmodel.setBankAccountNumber(this.bankAccountNumber);
                cmodel.setLoyaltyPoints(this.loyaltyPoints);
                cmodel.setShippingAddress(this.shippingAddress);
                cmodel.setPhoneNumber(this.phoneNumber);
                return cmodel;
            } default -> {
                if (this.id==null || !this.id.matches("-?\\d+(\\.\\d+)?")) this.setID(new EmployeeController().getNewID());
                EmployeeModel emodel = new EmployeeModel(type, name, id, email, password);
                emodel.setAdminStatus(this.adminStatus);
                return emodel;
            }
        }
    }

    @Override
    public String findErrorInRequiredFields() {
        if (this.name==null || this.name.isBlank()) return "Missing name field";
        if (this.email==null || this.email.isBlank()) return "Missing email field";
        if (this.password==null || this.password.isBlank()) return "Missing password field";
        return null;
    }

    @Override
    public String getDatabaseKeyField() {
        return this.email;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = UserType.valueOf(type);
    }

    public void setID(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    //-----------employees------------

    public void setAdminStatus(String status) {
        if (status!=null) this.adminStatus = AdminStatus.valueOf(status);
    }
    public void setAdminStatus(AdminStatus status) {
        this.adminStatus = status;
    }

    //-----------customers-------------
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setLoyaltyPoints(String points) {
        this.loyaltyPoints = points;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }
}
