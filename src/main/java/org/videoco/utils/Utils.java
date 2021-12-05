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

    private static String URLPattern = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\\\+&amp;%\\$#_]*)?";
    public static boolean isValidResource(String resource) {
        if (resource==null) return false;
        try {
            if (resource.matches(URLPattern)) new URL(resource).openStream();
            else new FileInputStream(resource);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public static InputStream createImageInputStream(String resource) throws IOException {
        try {
            if (resource.matches(URLPattern))
                return new URL(resource).openStream();
            else
                return new FileInputStream(resource);
        }catch (Exception e) {
            return new FileInputStream(brokenImage);
        }
    }

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
