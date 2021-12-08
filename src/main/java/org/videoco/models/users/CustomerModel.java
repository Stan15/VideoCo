package org.videoco.models.users;

import org.videoco.controllers.users.CustomerController;
import org.videoco.controllers.users.UserType;

public class CustomerModel extends UserModel {

    public String phoneNumber;
    public String loyaltyPoints;
    public String bankAccountNumber;
    public String shippingAddress;
    public String billingAddress;

    public CustomerModel() {
        this.type = UserType.CUSTOMER;
    }
    public CustomerModel(String name, String id, String email, String password) {
        super(name, id, email, password);
        this.type = UserType.CUSTOMER;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(String loyaltyPoints) {
        if (loyaltyPoints==null||loyaltyPoints.isBlank()) loyaltyPoints = "0";
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }


    @Override
    public org.videoco.controllers.users.UserController createController() {
        //TODO switch statement to decide which controller to use
        org.videoco.controllers.users.CustomerController controller = new org.videoco.controllers.users.CustomerController();
        controller.setFocusModel(new CustomerController().getModelFromDB(this.getDatabaseKey()));
        controller.setUser((CustomerModel) new CustomerController().getModelFromDB(this.getDatabaseKey()));
        return controller;
    }
}
