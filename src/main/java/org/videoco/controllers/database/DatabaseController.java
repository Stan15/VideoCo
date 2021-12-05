package org.videoco.controllers.database;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.json.JSONObject;
import org.videoco.controllers.Controller;
import org.videoco.factories.Factory;
import org.videoco.models.Model;
import org.videoco.utils.Utils;
import org.videoco.utils.database_queries.DBQuerier;
import org.videoco.utils.observer.Notifier;
import org.videoco.utils.observer.Observer;
import org.videoco.utils.observer.events.DBEvent;
import org.videoco.utils.observer.events.VCOEvent;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public abstract class DatabaseController extends Controller implements Notifier<VCOEvent, Model> {
    public static String metadataPath = Utils.getResourcePath("/org.videoco/databases/metadata.json");

    public abstract void readRecordIntoFactory(String[] record, Factory factory) throws Exception;
    public abstract String[] createRecord(Model model);
    public abstract String getDatabasePath();
    public abstract String[] getDatabaseHeader();
    public abstract HashMap<String, Model> getCache();
    public abstract void clearCache();
    public List<Model> getModels() {
        Collection<Model> tmp = this.getCache().values();
        return new ArrayList<>(tmp);
    }

    public boolean hasDatabaseEntry(String databaseKey) {
        return this.getCache().containsKey(databaseKey);
    }

    public Model addDBRecord(Factory factory) {
        if (factory.findErrorInRequiredFields()!=null) return null;
        if (factory.getDatabaseKeyField()!=null && this.hasDatabaseEntry(factory.getDatabaseKeyField())) return null;
        Model model = factory.createModel();
        if (factory.getDatabaseKeyField()==null) return null;
        this.getCache().put(model.getDatabaseKey(), model);
        this.writeCacheToDatabase();

        this.emit(DBEvent.ADD, model);
        this.emit(DBEvent.CHANGE, model);
        return model;
    }

    public boolean removeDBRecord(String databaseKey) {
        if (!this.hasDatabaseEntry(databaseKey)) return false;
        Model model = this.getModelFromDB(databaseKey);
        this.getCache().remove(databaseKey);
        this.writeCacheToDatabase();

        this.emit(DBEvent.DELETE, model);
        this.emit(DBEvent.CHANGE, model);
        return true;
    }

    public Model updateRecord(String databaseKey, Factory factory) {
        if (factory.findErrorInRequiredFields()!=null) return null;
        if (!this.hasDatabaseEntry(databaseKey)) return null;
        Model model = this.getModelFromDB(databaseKey);
        factory.setID(model.getID());
        removeDBRecord(databaseKey);
        model = addDBRecord(factory);

        this.emit(DBEvent.UPDATE, model);
        this.emit(DBEvent.CHANGE, model);
        return model;
    }

    public Model getModelFromDB(String databaseKey) {
        if (!this.hasDatabaseEntry(databaseKey)) return null;
        return this.getCache().get(databaseKey);
    }


    public static boolean writeMetadata(MetadataFields field, String value) {
        try {
            String jsonStr = Utils.readFileToString(metadataPath);
            JSONObject obj;
            if (jsonStr.isBlank()) obj = new JSONObject();
            else obj = new JSONObject(jsonStr);
            obj.put(field.name(), value);
            FileWriter fw = new FileWriter(metadataPath);
            fw.write(obj.toString());
            fw.flush();
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getMetadata(MetadataFields field) {
        try {
            String jsonStr = Utils.readFileToString(metadataPath);
            JSONObject obj;
            if (jsonStr.isBlank()) obj = new JSONObject();
            else obj = new JSONObject(jsonStr);

            if (obj.has(field.name())) return (String) obj.get(field.name());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public abstract String getGlobalIDFieldName();
    public String getNewID() {
        int newGlobalID = 0;
        try {
            String jsonStr = Utils.readFileToString(metadataPath);
            JSONObject obj;
            if (jsonStr.isBlank()) obj = new JSONObject();
            else obj = new JSONObject(jsonStr);

            Object id;
            if (obj.has(this.getGlobalIDFieldName())) id = obj.get(this.getGlobalIDFieldName());
            else id = "0";

            String currentGlobalID = id.toString();
            newGlobalID = Integer.parseInt(currentGlobalID) + 1;
            obj.put(this.getGlobalIDFieldName(), newGlobalID);

            FileWriter fw = new FileWriter(metadataPath);
            fw.write(obj.toString());
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(newGlobalID);
    }

    public void cacheDatabase(Factory factory) {
        try (CSVReader reader = new CSVReader(new FileReader(this.getDatabasePath()))){
            reader.readNext(); //skip headers

            String[] nextRecord;
            while ((nextRecord = reader.readNext()) != null) {
                this.readRecordIntoFactory(nextRecord, factory);
                Model model = factory.createModel();
                this.getCache().put(model.getDatabaseKey(), model);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
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

    //------Notifier functions-------------------------------
    @Override
    public void emit(VCOEvent event, Model model) {
        var observers = getObservers();
        if (!observers.containsKey(event)) return;
        for (var observer : observers.get(event)) {
            observer.callback(model);
        }
    }

    @Override
    public void attachObserver(Observer<VCOEvent, Model> observer, VCOEvent event) {
        var observers = getObservers();
        if (!observers.containsKey(event)) observers.put(event, new ArrayList<>());
        observers.get(event).add(observer);
    }

    @Override
    public void detachObserver(Observer<VCOEvent, Model> observer, VCOEvent event) {
        var observers = getObservers();
        if (!observers.containsKey(event)) return;
        observers.get(event).remove(observer);
    }
}
