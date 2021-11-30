package org.videoco.controllers.users;

import javafx.event.ActionEvent;
import org.videoco.factories.Factory;
import org.videoco.factories.UserFactory;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.Model;
import org.videoco.utils.Utils;
import org.videoco.views.ViewType;

import java.util.HashMap;

public class EmployeeController extends UserController {

    public EmployeeController() {
    }



    //-----------------Database Functions---------------------
    private static final HashMap<String, Model> cache = new HashMap<>();
    public static boolean isCached;
    public static String databasePath = Utils.getResourcePath("/org.videoco/databases/employees.csv");
    public static String[] databaseHeader = new String[]{"id", "name", "email", "password", "type", "adminStatus"};
    public static String GlobalIDFieldName = "GlobalEmployeeID";

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
                case "type": userFactory.setType(record[i].strip());
                case "adminStatus": userFactory.setAdminStatus(record[i].strip());
            }
        }
    }
    @Override
    public String[] createRecord(Model model) {
        EmployeeModel user = (EmployeeModel) model;
        return new String[]{user.getID(), user.getName(), user.getEmail(), user.getPassword(), user.getType(), user.getAdminStatus()};
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
        this.cacheDatabase();
        return EmployeeController.cache;
    }

    @Override
    public void clearCache() {
        EmployeeController.cache.clear();
        isCached = false;
    }

    @Override
    public void transitionToHomeView(ActionEvent event) {
        //TODO (or make EmployeeControlelr abstract and implement this for its concrete subclasses)
    }

    @Override
    public boolean isValidTransition(ViewType view) {
        //TODO (or make EmployeeControlelr abstract and implement this for its concrete subclasses)
        return true;
    }
}
