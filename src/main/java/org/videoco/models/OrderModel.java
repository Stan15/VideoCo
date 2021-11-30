//package org.videoco.models;
//
//import java.util.ArrayList;
//
//public class OrderModel implements Model {
//    public String id;
//    public String customerID;
//    public String placedDT;
//    public String returnedDT;
//    public ArrayList<Movie> movies = new ArrayList<Movie>();
//
//    @Override
//    public String getDatabaseKey() {
//        return this.id;
//    }
//
//    public String getID() {
//        return id;
//    }
//
//    public void setID(String id) {
//        this.id = id;
//    }
//
//    public String getCustomerID() {
//        return customerID;
//    }
//
//    public void setCustomerID(String customerID) {
//        this.customerID = customerID;
//    }
//
//    public String getPlacedDT() {
//        return placedDT;
//    }
//
//    public void setPlacedDT(String placedDT) {
//        this.placedDT = placedDT;
//    }
//
//    public String getReturnedDT() {
//        return returnedDT;
//    }
//
//    public void setReturnedDT(String returnedDT) {
//        this.returnedDT = returnedDT;
//    }
//
//    public void addMovie(Movie movie) {
//        this.movies.add(movie);
//    }
//
//    public void setMovies(String movieIDs) {
//        String[] IDlist = movieIDs.split("-");
//        movies = new ArrayList<Movie>();
//        for (String movieID : IDlist) {
//            this.movies.add(MovieController.getMovie(movieID));
//        }
//    }
//}
