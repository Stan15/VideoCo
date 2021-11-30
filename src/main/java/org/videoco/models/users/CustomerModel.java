package org.videoco.models.users;

import org.videoco.controllers.users.CustomerController;

public class CustomerModel extends UserModel {
    public String phoneNumber;
    public String loyaltyPoints;
    public String bankAccountNumber;
    public String shippingAddress;
    public String billingAddress;
    public String placedOrderIDs;
    public String receivedOrderIDs;

    public CustomerModel() {
        this.type = "Customer";
    }
    public CustomerModel(String name, String id, String email, String password) {
        super(name, id, email, password);
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

    public String getPlacedOrderIDs() {
        return placedOrderIDs;
    }

    public void setPlacedOrderIDs(String placedOrderIDs) {
        this.placedOrderIDs = placedOrderIDs;
    }

    public String getReceivedOrderIDs() {
        return receivedOrderIDs;
    }

    public void setReceivedOrderIDs(String receivedOrderIDs) {
        this.receivedOrderIDs = receivedOrderIDs;
    }

    @Override
    public CustomerController createController() {
        //TODO switch statement to decide which controller to use
        CustomerController controller = new CustomerController();
        controller.setFocusModel(this);
        return controller;
    }
}
