package org.videoco.controllers;

import javafx.event.ActionEvent;
import org.videoco.controllers.database.DatabaseController;
import org.videoco.factories.Factory;
import org.videoco.factories.MovieFactory;
import org.videoco.models.Model;
import org.videoco.models.MovieModel;
import org.videoco.utils.Utils;
import org.videoco.views.ViewType;

import java.util.HashMap;

import static org.videoco.views.ViewType.MovieBrowser;

public class MovieController extends DatabaseController {

    //-----------------Database Functions---------------------
    public static HashMap<String, Model> cache = new HashMap<>();
    public static final String databasePath = Utils.getResourcePath("/org.videoco/databases/employees.csv/movies.csv");
    public static boolean isCached = false;
    public static String[] databaseHeader = new String[]{"id", "title", "description", "category", "actors", "directors", "date-of-release", "amount-in-stock"};

    @Override
    public void readRecordIntoFactory(String[] record, Factory factory) throws Exception {
        MovieFactory movieFactory = (MovieFactory) factory;
        if (record.length!=databaseHeader.length) throw new Exception("Invalid record. Cannot create a movie instance.");
        for (int i=0; i<record.length; i++) {
            switch (databaseHeader[i]) {
                case "id": movieFactory.setID(record[i].strip());
                case "title": movieFactory.setTitle(record[i].strip());
                case "description": movieFactory.setDescription(record[i].strip());
                case "category": movieFactory.setCategory(record[i].strip());
                case "actors": movieFactory.setActors(record[i].strip());
                case "directors": movieFactory.setDirectors(record[i].strip());
                case "date-of-release": movieFactory.setDateOfRelease(record[i].strip());
                case "amount-in-stock": movieFactory.setAmountInStock(Integer.parseInt(record[i].strip()));
            }
        }
    }
    @Override
    public String[] createRecord(Model model) {
        MovieModel movie = (MovieModel) model;
        return new String[]{movie.getID(), movie.getTitle(), movie.getDescription(), movie.getCategory(), movie.getActors(), movie.getDirectors(), movie.getDateOfRelease(), String.valueOf(movie.getAmountInStock())};
    }


    @Override
    public String getDatabasePath() {
        return MovieController.databasePath;
    }

    @Override
    public String[] getDatabaseHeader() {
        return MovieController.databaseHeader;
    }

    @Override
    public HashMap<String, Model> getCache() {
        if (MovieController.isCached) return MovieController.cache;
        MovieController.isCached = true;
        this.cacheDatabase();
        return MovieController.cache;
    }

    @Override
    public void clearCache() {
        MovieController.cache.clear();
        MovieController.isCached = false;
    }

    @Override
    public void transitionToHomeView(ActionEvent event) {
        try {
            transition(ViewType.MovieBrowser, event);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValidTransition(ViewType view) {
        return switch (view) {
            case MoviePage -> currentViewType == MovieBrowser; //you can only switch to MoviePage if you are currently on MovieBrowser
            default -> false;
        };
    }
}
