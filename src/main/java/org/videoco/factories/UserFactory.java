package org.videoco.factories;

import org.videoco.controllers.users.CustomerController;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;

public class UserFactory implements Factory {
    String name;
    String id;
    String email;
    String password;
    String type;

    String adminStatus;

    String phoneNumber;

    String bankAccountNumber;
    String loyaltyPoints;
    String shippingAddress;
    String billingAddress;
    String placedOrderIDs;
    String receivedOrderIDs;

    public void setCommonAttrs(String type, String name, String id, String email, String password) {
        this.type = type;
        this.name = name;
        this.id = id;
        this.email = email;
        this.password = password;
    }

    @Override
    public UserModel createModel() {
        switch (type) {
            case "Customer":
                CustomerModel cmodel = new CustomerModel(name, id, email, password);
                cmodel.setBillingAddress(this.billingAddress);
                cmodel.setBankAccountNumber(this.bankAccountNumber);
                cmodel.setLoyaltyPoints(this.loyaltyPoints);
                cmodel.setPlacedOrderIDs(this.placedOrderIDs);
                cmodel.setReceivedOrderIDs(this.receivedOrderIDs);
                cmodel.setShippingAddress(this.shippingAddress);
                cmodel.setPhoneNumber(this.phoneNumber);
                return cmodel;
            default:
                EmployeeModel emodel = new EmployeeModel(type, name, id, email, password);
                emodel.setAdminStatus(this.adminStatus);
                return emodel;
        }
    }

    public void setType(String type) {
        this.type = type;
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

    public void setPlacedOrderIDs(String placedOrderIDs) {
        this.placedOrderIDs = placedOrderIDs;
    }

    public void setReceivedOrderIDs(String receivedOrderIDs) {
        this.receivedOrderIDs = receivedOrderIDs;
    }
}
