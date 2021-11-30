package org.videoco.controllers.database;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.json.JSONObject;
import org.videoco.controllers.Controller;
import org.videoco.factories.Factory;
import org.videoco.factories.UserFactory;
import org.videoco.models.Model;
import org.videoco.utils.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public abstract class DatabaseController extends Controller {
    public static String metadataPath = Utils.getResourcePath("/org.videoco/databases/metadata.json");

    public static boolean writeMetadata(String field, String value) {
        try {
            String jsonStr = Utils.readFileToString(metadataPath);
            JSONObject obj;
            if (jsonStr.isBlank()) obj = new JSONObject();
            else obj = new JSONObject(jsonStr);
            obj.put(field, value);
            FileWriter fw = new FileWriter(metadataPath);
            fw.write(obj.toString());
            fw.flush();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getMetadata(String fieldName) {
        try {
            String jsonStr = Utils.readFileToString(metadataPath);
            JSONObject obj;
            if (jsonStr.isBlank()) obj = new JSONObject();
            else obj = new JSONObject(jsonStr);

            if (obj.has(fieldName)) return (String) obj.get(fieldName);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNewID(String globalIDName) {
        int newGlobalID = 0;
        try {
            String jsonStr = Utils.readFileToString(metadataPath);
            JSONObject obj;
            if (jsonStr.isBlank()) obj = new JSONObject();
            else obj = new JSONObject(jsonStr);

            Object id;
            if (obj.has(globalIDName)) id = obj.get(globalIDName);
            else id = "0";

            String currentGlobalID = id.toString();
            newGlobalID = Integer.parseInt(currentGlobalID) + 1;
            obj.put(globalIDName, newGlobalID);

            FileWriter fw = new FileWriter(metadataPath);
            fw.write(obj.toString());
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(newGlobalID);
    }

    public void cacheDatabase() {
        try (CSVReader reader = new CSVReader(new FileReader(this.getDatabasePath()))){
            reader.readNext(); //skip headers

            String[] nextRecord;
            while ((nextRecord = reader.readNext()) != null) {
                Factory factory = new UserFactory();
                this.readRecordIntoFactory(nextRecord, factory);
                Model model = factory.createModel();
                this.getCache().put(model.getDatabaseKey(), model);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void readRecordIntoFactory(String[] record, Factory factory) throws Exception;
    public abstract String[] createRecord(Model model);
    public abstract String getDatabasePath();
    public abstract String[] getDatabaseHeader();
    public abstract HashMap<String, Model> getCache();
    public abstract void clearCache();

    public boolean addModelToDB(Model model) {
        if (this.getCache().containsKey(model.getDatabaseKey())) return false;
        this.getCache().put(model.getDatabaseKey(), model);
        this.writeCacheToDatabase();
        return true;
    }

    public Model getModelFromDB(String databaseKey) {
        if (!this.hasDatabaseEntry(databaseKey)) return null;
        return this.getCache().get(databaseKey);
    }

    public boolean hasDatabaseEntry(String databaseKey) {
        return this.getCache().containsKey(databaseKey);
    }

    public void writeCacheToDatabase() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(this.getDatabasePath()))){
            writer.writeNext(this.getDatabaseHeader());

            for (Model model : this.getCache().values()) {
                writer.writeNext(this.createRecord(model));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
