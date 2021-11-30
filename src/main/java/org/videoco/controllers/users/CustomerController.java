package org.videoco.controllers.users;

import javafx.event.ActionEvent;
import javafx.event.Event;
import org.videoco.factories.Factory;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.Model;
import org.videoco.utils.Utils;
import org.videoco.views.ViewType;

import java.util.HashMap;

import static org.videoco.views.ViewType.MovieBrowser;

public class CustomerController extends UserController {

    public CustomerController() {}
    public CustomerController(CustomerModel model) {
        super();
    }



    //-----------------Database Functions---------------------
    private static final HashMap<String, Model> cache = new HashMap<>();
    private static boolean isCached = false;
    public static String databasePath = Utils.getResourcePath("/org.videoco/databases/customers.csv");
    public static String[] databaseHeader = new String[]{"id", "name", "email", "password", "number", "loyalty_points", "bank_account_number", "shipping_address", "billing_address", "placed_order_ids", "received_order_ids"};
    public static String GlobalIDFieldName = "GlobalCustomerID";
    @Override
    public void readRecordIntoFactory(String[] record, Factory factory) throws Exception {
        UserFactory userFactory = (UserFactory) factory;
        if (record.length!=databaseHeader.length) throw new Exception("Invalid record. Cannot create a user instance.");
        for (int i=0; i<record.length; i++) {
            switch (databaseHeader[i]) {
                case "id": userFactory.setType(record[i].strip());
                case "name": userFactory.setType(record[i].strip());
                case "email": userFactory.setType(record[i].strip());
                case "password": userFactory.setType(record[i].strip());
                case "number": userFactory.setType(record[i].strip());
                case "loyalty_points": userFactory.setLoyaltyPoints(record[i].strip());
                case "bank_account_number": userFactory.setBankAccountNumber(record[i].strip());
                case "shipping_address": userFactory.setShippingAddress(record[i].strip());
                case "billing_address": userFactory.setBillingAddress(record[i].strip());
                case "placed_order_ids": userFactory.setPlacedOrderIDs(record[i].strip());
                case "received_order_ids": userFactory.setReceivedOrderIDs(record[i].strip());
            }
        }
    }
    @Override
    public String[] createRecord(Model model) {
        CustomerModel user = (CustomerModel) model;
        return new String[]{user.getID(), user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getLoyaltyPoints(), user.getBankAccountNumber(), user.getShippingAddress(), user.getBillingAddress(), user.getPlacedOrderIDs(), user.getReceivedOrderIDs()};
    }

    @Override
    public String getDatabasePath() {
        return CustomerController.databasePath;
    }

    @Override
    public String[] getDatabaseHeader() {
        return CustomerController.databaseHeader;
    }

    @Override
    public HashMap<String, Model> getCache() {
        if (CustomerController.isCached) return CustomerController.cache;
        CustomerController.isCached = true;
        this.cacheDatabase();
        return CustomerController.cache;
    }

    @Override
    public void clearCache() {
        CustomerController.cache.clear();
        isCached = false;
    }

    @Override
    public void transitionToHomeView(ActionEvent event) {
        try {
            transition(ViewType.MovieBrowser, event);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValidTransition(ViewType view) {
        return switch (view) {
            case MovieBrowser -> true;
            default -> false;
        };
    }
}
