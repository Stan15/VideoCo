package org.videoco.controllers.users;

import javafx.event.ActionEvent;
import org.videoco.controllers.database.MetadataFields;
import org.videoco.controllers.movies.MovieController;
import org.videoco.controllers.orders.OrderController;
import org.videoco.factories.Factory;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.Model;
import org.videoco.models.users.CustomerModel;
import org.videoco.models.users.EmployeeModel;
import org.videoco.utils.Utils;
import org.videoco.utils.observer.Observer;
import org.videoco.utils.observer.events.VCOEvent;
import org.videoco.views.ViewEnum;
import org.videoco.views.sidebar.info.SidebarInfoItem;
import org.videoco.views.sidebar.switcher.SidebarSwitcherItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerController extends org.videoco.controllers.users.UserController {

    public CustomerController() {}
    public CustomerController(CustomerModel model) {
        super();
    }



    //-----------------Database Functions---------------------
    private static final HashMap<String, Model> cache = new HashMap<>();
    private static boolean isCached = false;
    public static String databasePath = Utils.getResourcePath("/org.videoco/databases/customers.csv");
    public static String[] databaseHeader = new String[]{"id", "name", "email", "password", "number", "loyalty-points", "bank-account-number", "shipping-address", "billing-address"};
    @Override
    public String getGlobalIDFieldName() {
        return MetadataFields.GLOBAL_CUSTOMER_ID.name();
    }

    @Override
    public List<Model> getModels() {
        try {
            //only admins can get a list of customers
            if (((EmployeeModel) this.user).getAdminStatus().level>= AdminStatus.ADMIN.level) {
                return super.getModels();
            }
        }catch (ClassCastException ignored) {}
        return new ArrayList<>();
    }

    @Override
    public void readRecordIntoFactory(String[] record, Factory factory) throws Exception {
        UserFactory userFactory = (UserFactory) factory;
        if (record.length!=databaseHeader.length) throw new Exception("Invalid record. Cannot create a user instance.");
        for (int i=0; i<record.length; i++) {
            switch (databaseHeader[i]) {
                case "id" -> userFactory.setID(record[i].strip());
                case "name" -> userFactory.setName(record[i].strip());
                case "email" -> userFactory.setEmail(record[i].strip());
                case "password" -> userFactory.setPassword(record[i].strip());
                case "number" -> userFactory.setPhoneNumber(record[i].strip());
                case "loyalty-points" -> userFactory.setLoyaltyPoints(record[i].strip());
                case "bank-account-number" -> userFactory.setBankAccountNumber(record[i].strip());
                case "shipping-address" -> userFactory.setShippingAddress(record[i].strip());
                case "billing-address" -> userFactory.setBillingAddress(record[i].strip());
            }
        }
    }
    @Override
    public String[] createRecord(Model model) {
        CustomerModel user = (CustomerModel) model;
        return new String[]{user.getID(), user.getName(), user.getEmail(), user.getPassword(), user.getPhoneNumber(), user.getLoyaltyPoints(), user.getBankAccountNumber(), user.getShippingAddress(), user.getBillingAddress()};
    }

    @Override
    public String getDatabasePath() {
        return org.videoco.controllers.users.CustomerController.databasePath;
    }

    @Override
    public String[] getDatabaseHeader() {
        return org.videoco.controllers.users.CustomerController.databaseHeader;
    }

    @Override
    public HashMap<String, Model> getCache() {
        if (org.videoco.controllers.users.CustomerController.isCached) return org.videoco.controllers.users.CustomerController.cache;
        org.videoco.controllers.users.CustomerController.isCached = true;
        UserFactory factory = new UserFactory();
        factory.setType(new CustomerModel().getType());
        this.cacheDatabase(factory);
        return org.videoco.controllers.users.CustomerController.cache;
    }

    @Override
    public void clearCache() {
        org.videoco.controllers.users.CustomerController.cache.clear();
        isCached = false;
    }

    @Override
    public void transitionToHomeView(ActionEvent event) {
        transition(ViewEnum.MOVIE_BROWSER, event);
    }

    @Override
    public List<SidebarSwitcherItem> getSidebarSwitcherItems() {
        OrderController orderController = new OrderController(this.user);
        MovieController movieController = new MovieController(this.user);
        orderController.setFocusModel(orderController.getDraftOrder());
        List<SidebarSwitcherItem> switchers = new ArrayList<>();
        switchers.add(new SidebarSwitcherItem("Movies", ViewEnum.MOVIE_BROWSER, movieController));
        switchers.add(new SidebarSwitcherItem("Cart", ViewEnum.ORDER_PAGE, orderController));
        switchers.add(new SidebarSwitcherItem("Your Orders", ViewEnum.ORDER_BROWSER, orderController));
        return switchers;
    }

    @Override
    public List<SidebarInfoItem> getSidebarInfoItems() {
        List<SidebarInfoItem> infoItems = new ArrayList<>();
        infoItems.add(new SidebarInfoItem(this, "Loyalty points", (model) -> ((CustomerModel) model).getLoyaltyPoints()));
        return infoItems;
    }

    @Override
    public boolean isValidTransition(ViewEnum view) {
        return true;
//        return switch (view) {
//            case MOVIE_BROWSER -> true;
//            default -> false;
//        };
    }

    private static final Map<VCOEvent, List<Observer<VCOEvent, Model>>> OBSERVERS = new HashMap<>();
    @Override
    public Map<VCOEvent, List<Observer<VCOEvent, Model>>> getObservers() {
        return org.videoco.controllers.users.CustomerController.OBSERVERS;
    }

    public void decrementLoyaltyPoints(int amount) {
        UserFactory userFactory = new UserFactory(this.user);
        userFactory.setLoyaltyPoints(String.valueOf(Integer.parseInt(((CustomerModel) this.user).getLoyaltyPoints())-amount));
        new CustomerController((CustomerModel) this.user).updateRecord(this.user.getDatabaseKey(), userFactory);
    }
    public void incrementLoyaltyPoints(int amount) {
        UserFactory userFactory = new UserFactory(this.user);
        userFactory.setLoyaltyPoints(String.valueOf(Integer.parseInt(((CustomerModel) this.user).getLoyaltyPoints())+amount));
        new CustomerController((CustomerModel) this.user).updateRecord(this.user.getDatabaseKey(), userFactory);
    }
}
