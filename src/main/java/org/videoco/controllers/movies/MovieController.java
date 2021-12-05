package org.videoco.controllers.movies;

import org.videoco.controllers.database.MetadataFields;
import org.videoco.controllers.database.DatabaseController;
import org.videoco.factories.Factory;
import org.videoco.factories.MovieFactory;
import org.videoco.models.Model;
import org.videoco.models.MovieModel;
import org.videoco.models.users.UserModel;
import org.videoco.utils.Utils;
import org.videoco.utils.observer.Observer;
import org.videoco.utils.observer.events.VCOEvent;
import org.videoco.views.ViewEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieController extends DatabaseController {
    public MovieController(){}
    public MovieController(UserModel user) {
        this.setUser(user);
    }

    //-----------------Database Functions---------------------
    public static HashMap<String, Model> cache = new HashMap<>();
    public static final String databasePath = Utils.getResourcePath("/org.videoco/databases/movies.csv");
    public static boolean isCached = false;
    public static String[] databaseHeader = new String[]{"id", "title", "description", "category", "actors", "directors", "date-of-release", "amount-in-stock"};
    @Override
    public String getGlobalIDFieldName() {
        return MetadataFields.GLOBAL_MOVIE_ID.name();
    }

    @Override
    public void readRecordIntoFactory(String[] record, Factory factory) throws Exception {
        MovieFactory movieFactory = (MovieFactory) factory;
        if (record.length!=databaseHeader.length) throw new Exception("Invalid record. Cannot create a movie instance.");
        for (int i=0; i<record.length; i++) {
            switch (databaseHeader[i]) {
                case "id" -> movieFactory.setID(record[i].strip());
                case "title" -> movieFactory.setTitle(record[i].strip());
                case "description" -> movieFactory.setDescription(record[i].strip());
                case "category" -> movieFactory.setCategory(record[i].strip());
                case "actors" -> movieFactory.setActors(record[i].strip());
                case "directors" -> movieFactory.setDirectors(record[i].strip());
                case "date-of-release" -> movieFactory.setDateOfRelease(record[i].strip());
                case "amount-in-stock" -> movieFactory.setAmountInStock(record[i].strip());
            }
        }
    }
    @Override
    public String[] createRecord(Model model) {
        MovieModel movie = (MovieModel) model;
        return new String[]{movie.getID(), movie.getTitle(), movie.getDescription(), movie.getCategory(), movie.getActors(), movie.getDirectors(), movie.getDateOfRelease(), movie.getAmountInStock()};
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
        this.cacheDatabase(new MovieFactory());
        return MovieController.cache;
    }

    @Override
    public void clearCache() {
        MovieController.cache.clear();
        MovieController.isCached = false;
    }

    @Override
    public boolean isValidTransition(ViewEnum view) {
        return true;
//        return switch (view) {
//            case MOVIE_PAGE -> currentView == MOVIE_BROWSER || currentView ==null; //you can only switch to MoviePage if you are currently on MovieBrowser
//            case MOVIE_BROWSER -> true;
//            case MOVIE_DB_BROWSER ->
//                    user instanceof EmployeeModel &&
//                    ((EmployeeModel) user).getAdminStatus().level >= AdminStatus.ADMIN.level;
//            case MOVIE_DB_EDIT_PAGE -> currentView ==MOVIE_DB_BROWSER;
//            default -> false;
//        };
    }


    private static final Map<VCOEvent, List<Observer<VCOEvent, Model>>> OBSERVERS = new HashMap<>();
    @Override
    public Map<VCOEvent, List<Observer<VCOEvent, Model>>> getObservers() {
        return MovieController.OBSERVERS;
    }


    public void decrementMovieStock(MovieModel movie) {
        int amount = -1;
        MovieFactory factory = new MovieFactory(movie);
        factory.setAmountInStock(String.valueOf(Integer.parseInt(movie.getAmountInStock())+amount));
        this.updateRecord(movie.getDatabaseKey(), factory);
    }

    public void incrementMovieStock(MovieModel movie) {
        int amount = 1;
        MovieFactory factory = new MovieFactory(movie);
        factory.setAmountInStock(String.valueOf(Integer.parseInt(movie.getAmountInStock())+amount));
        this.updateRecord(movie.getDatabaseKey(), factory);
    }
}
