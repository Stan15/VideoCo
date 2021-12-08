package org.videoco.controllers.admin;

import org.videoco.controllers.database.MetadataFields;
import org.videoco.controllers.users.employee.EmployeeController;
import org.videoco.controllers.users.UserType;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminController extends EmployeeController {
    protected static final HashSet<String> employeeRegisterCodes = new HashSet<>();
    protected static boolean areRegisterCodesCached = false;

    protected AdminController() {}


    protected static void cacheEmployeeRegisterCodes() {
        employeeRegisterCodes.clear();
        String codes = getMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES);
        if (codes==null) {
            writeMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES, "");
        }else {
            Pattern p = Pattern.compile("[0-9a-zA-Z:]+");
            Matcher m = p.matcher(codes);
            while (m.find()) {
                employeeRegisterCodes.add(m.group());
            }
        }
        areRegisterCodesCached = true;
    }

    public static boolean isValidEmployeeRegistrationCode(String code) {
        if (!areRegisterCodesCached)
            cacheEmployeeRegisterCodes();
        return employeeRegisterCodes.contains(code.strip());
    }

    public static UserType getEmployeeType(String code) {
        try {
            String type = code.split(":")[0];
            return UserType.valueOf(type);
        }catch (Exception e) {
            return null;
        }
    }

    public static void useEmployeeRegisterCode(String code) {
        String codes = getMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES);
        employeeRegisterCodes.remove(code);
        if (codes==null) {
            writeMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES, "");
        } else {
            writeMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES, codes.replaceAll("(^|-)"+code+"($|-)", ""));
        }
    }
}
