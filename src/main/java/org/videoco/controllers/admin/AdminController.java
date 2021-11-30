package org.videoco.controllers.admin;

import org.videoco.controllers.users.EmployeeController;

import java.util.HashSet;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminController extends EmployeeController {
    private static final HashSet<String> employeeRegisterCodes = new HashSet<>();
    private static final String registerCodesFieldName = "EmployeeRegistrationCodes";
    private static boolean areRegisterCodesCached = false;

    public String createEmployeeRegistrationCode() {
        if (!areRegisterCodesCached)
            cacheEmployeeRegisterCodes();
        Random rnd = new Random();
        String code;
        boolean singleRun = getMetadata(registerCodesFieldName)==null || getMetadata(registerCodesFieldName).isBlank();
        do {
            code = String.valueOf(rnd.nextInt(1000000000));
            if (singleRun) break;
        }while (!isValidEmployeeRegistrationCode(code));
        String codes = getMetadata(registerCodesFieldName);
        if (codes==null) {
            codes = code;
        }else {
            codes = "-"+code;
        }
        writeMetadata(registerCodesFieldName, codes);
        employeeRegisterCodes.add(code);
        return code;
    }

    private static void cacheEmployeeRegisterCodes() {
        employeeRegisterCodes.clear();
        String codes = getMetadata(registerCodesFieldName);
        if (codes==null) {
            writeMetadata(registerCodesFieldName, "");
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

    public static void useEmployeeRegisterCode(String code) {
        String codes = getMetadata(registerCodesFieldName);
        employeeRegisterCodes.remove(code);
        if (codes==null) {
            writeMetadata(registerCodesFieldName, "");
        } else {
            writeMetadata(registerCodesFieldName, codes.replaceAll("(^|-)"+code+"($|-)", ""));
        }
    }
}
