package org.videoco.controllers.admin;

import org.videoco.controllers.database.MetadataFields;
import org.videoco.controllers.users.EmployeeController;
import org.videoco.controllers.users.UserType;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.EmployeeModel;

import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminController extends EmployeeController {
    private static final HashSet<String> employeeRegisterCodes = new HashSet<>();
    private static boolean areRegisterCodesCached = false;

    public static String createEmployeeRegistrationCode(EmployeeModel employee, UserType type) {
        if (employee.getAdminStatus().level < AdminStatus.SYSTEM_ADMIN.level) {
            new Exception("Unauthorized access.").printStackTrace();
            return null;
        }
        if (!areRegisterCodesCached)
            cacheEmployeeRegisterCodes();
        Random rnd = new Random();
        String code;
        boolean singleRun = getMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES)==null || getMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES).isBlank();
        do {
            code = String.valueOf(rnd.nextInt(1000000000));
            if (singleRun) break;
        }while (!isValidEmployeeRegistrationCode(code));
        String codes = getMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES);
        if (codes==null) {
            codes = code;
        }else {
            codes = "-"+type.name()+":"+code;
        }
        writeMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES, codes);
        employeeRegisterCodes.add(code);
        return code;
    }

    private static void cacheEmployeeRegisterCodes() {
        employeeRegisterCodes.clear();
        String codes = getMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES);
        if (codes==null) {
            writeMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES, "");
        }else {
            Pattern p = Pattern.compile("[0-9a-zA-Z]+");
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
            e.printStackTrace();
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
