package org.videoco.controllers.users.employee;

import javafx.event.ActionEvent;
import javafx.event.Event;
import org.videoco.controllers.admin.SystemAdminController;
import org.videoco.controllers.database.MetadataFields;
import org.videoco.controllers.movies.MovieController;
import org.videoco.controllers.users.UserController;
import org.videoco.factories.Factory;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.Model;
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

public class EmployeeController extends UserController {

    public EmployeeController() {}
    public EmployeeController(EmployeeModel user) {
        this.setUser(user);
    }




    //-----------------Database Functions---------------------
    private static final HashMap<String, Model> cache = new HashMap<>();
    public static boolean isCached;
    public static String databasePath = Utils.getResourcePath("/org.videoco/databases/employees.csv");
    public static String[] databaseHeader = new String[]{"id", "name", "email", "password", "type", "admin-status"};
    @Override
    public String getGlobalIDFieldName() {
        return MetadataFields.GLOBAL_EMPLOYEE_ID.name();
    }

    @Override
    public List<Model> getModels() {
        //only system admin can get a list of employees
        try {
            if (((EmployeeModel) this.user).getAdminStatus().level>=AdminStatus.SYSTEM_ADMIN.level) {
                return super.getModels();
            }
        }catch (Exception ignored) {}
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
                case "type" -> userFactory.setType(record[i].strip());
                case "admin-status" -> userFactory.setAdminStatus(record[i].strip());
            }
        }
    }
    @Override
    public String[] createRecord(Model model) {
        EmployeeModel user = (EmployeeModel) model;
        return new String[]{user.getID(), user.getName(), user.getEmail(), user.getPassword(), user.getType().name(), user.getAdminStatus().name()};
    }

    @Override
    public String getDatabasePath() {
        return EmployeeController.databasePath;
    }

    @Override
    public String[] getDatabaseHeader() {
        return EmployeeController.databaseHeader;
    }

    @Override
    public HashMap<String, Model> getCache() {
        if (EmployeeController.isCached) return EmployeeController.cache;
        EmployeeController.isCached = true;
        this.cacheDatabase(new UserFactory());
        return EmployeeController.cache;
    }

    @Override
    public void clearCache() {
        EmployeeController.cache.clear();
        isCached = false;
    }

    @Override
    public void transitionToHomeView(Event event) {
        //TODO (or make EmployeeControlelr abstract and implement this for its concrete subclasses)
        if (((EmployeeModel) this.user).getAdminStatus().level>=AdminStatus.ADMIN.level) {
            transition(ViewEnum.MOVIE_DB_BROWSER, event);
        } else {
            transition(ViewEnum.MOVIE_BROWSER, event);
        }
    }

    @Override
    public List<SidebarSwitcherItem> getSidebarSwitcherItems() {
        List<SidebarSwitcherItem> switchers = new ArrayList<>();
        MovieController movieController = new MovieController(this.user);
        if (this.user instanceof EmployeeModel && ((EmployeeModel) this.user).getAdminStatus()==AdminStatus.SYSTEM_ADMIN) {
            switchers.add(new SidebarSwitcherItem("Movies", ViewEnum.MOVIE_DB_BROWSER, movieController));
            SystemAdminController sysAdminController = new SystemAdminController(this.user);
            switchers.add(new SidebarSwitcherItem("Codes", ViewEnum.EMPLOYEE_ID_GENERATOR, sysAdminController));
            switchers.add(new SidebarSwitcherItem("Customers", ViewEnum.CUSTOMER_BROWSER, sysAdminController));
            switchers.add(new SidebarSwitcherItem("Employees", ViewEnum.EMPLOYEE_BROWSER, sysAdminController));
        }
        return switchers;
    }

    @Override
    public List<SidebarInfoItem> getSidebarInfoItems() {
        List<SidebarInfoItem> infoItems = new ArrayList<>();
        if (((EmployeeModel) this.user).getAdminStatus().level>=AdminStatus.ADMIN.level) {
            infoItems.add(new SidebarInfoItem(this, "Admin status", (model) -> ((EmployeeModel) model).getAdminStatus().name()));
        }
        return infoItems;
    }

    @Override
    public boolean isValidTransition(ViewEnum view) {
        //TODO (or make EmployeeControlelr abstract and implement this for its concrete subclasses)
        return true;
    }


    private static final Map<VCOEvent, List<Observer<VCOEvent, Model>>> OBSERVERS = new HashMap<>();
    @Override
    public Map<VCOEvent, List<Observer<VCOEvent, Model>>> getObservers() {
        return EmployeeController.OBSERVERS;
    }
}
