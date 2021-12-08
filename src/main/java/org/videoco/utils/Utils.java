package org.videoco.utils;

import org.videoco.controllers.database.DatabaseController;
import org.videoco.controllers.database.MetadataFields;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Utils {
    public static String brokenImage = "/org.videoco/assets/broken-image.png";


    public static String readFileToString(String path) {
        String out = null;
        try {
            out = new String(Files.readAllBytes(Paths.get(path)));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(out);
    }

    public static String getResourcePath(String path) {
        try {
            path = Paths.get(
                    Objects.requireNonNull(Utils.class.getResource(path)
                    ).toURI()).toFile().getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Objects.requireNonNull(path);
    }

    public static Date convertStringToDate(String dateStr) {
        Date date = null;
        try {
            date = new SimpleDateFormat(Objects.requireNonNull(DatabaseController.getMetadata(MetadataFields.DATE_TIME_FORMAT))).parse(dateStr);
        } catch (ParseException e) {
            try {
                date = new SimpleDateFormat(Objects.requireNonNull(DatabaseController.getMetadata(MetadataFields.DATE_FORMAT))).parse(dateStr);
            }catch (ParseException ignored) {}
        }
        return date;
    }

    public static String convertDateToDTString(Date date) {
        if (date==null) return "";
        return new SimpleDateFormat(Objects.requireNonNull(DatabaseController.getMetadata(MetadataFields.DATE_TIME_FORMAT))).format(date);
    }

    public static String convertDateToString(Date date) {
        if (date==null) return "";
        return new SimpleDateFormat(Objects.requireNonNull(DatabaseController.getMetadata(MetadataFields.DATE_FORMAT))).format(date);
    }
}
