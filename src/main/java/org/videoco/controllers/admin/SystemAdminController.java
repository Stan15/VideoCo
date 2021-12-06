package org.videoco.controllers.admin;

import org.videoco.controllers.database.MetadataFields;
import org.videoco.controllers.users.UserType;
import org.videoco.models.users.AdminStatus;
import org.videoco.models.users.EmployeeModel;
import org.videoco.models.users.UserModel;

import java.util.Random;

public class SystemAdminController extends AdminController {

    //public for testing
    public SystemAdminController() {}
    public SystemAdminController(UserModel user) {
        this.setUser(user);
    }
    public static SystemAdminController getInstance(UserModel user) {
        if (user instanceof EmployeeModel e) {
            if (e.getAdminStatus().level>=AdminStatus.SYSTEM_ADMIN.level) return new SystemAdminController(user);
        }
        return null;
    }
    public String createEmployeeRegistrationCode(UserType type) {
        if (((EmployeeModel)this.user).getAdminStatus().level < AdminStatus.SYSTEM_ADMIN.level) {
            new Exception("Unauthorized access.").printStackTrace();
            return null;
        }
        if (!AdminController.areRegisterCodesCached)
            AdminController.cacheEmployeeRegisterCodes();
        Random rnd = new Random();
        String code = type.name()+":";
        boolean singleRun = getMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES)==null || getMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES).isBlank();
        do {
            code += String.valueOf(rnd.nextInt(1000000000));
            if (singleRun) break;
        }while (employeeRegisterCodes.contains(code.strip()));
        String codes;
        if (singleRun) {
            writeMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES, code);
        }else {
            codes = getMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES)+"-"+code;
            writeMetadata(MetadataFields.EMPLOYEE_REGISTRATION_CODES, codes);
        }
        AdminController.employeeRegisterCodes.add(code);
        return code;
    }
}
