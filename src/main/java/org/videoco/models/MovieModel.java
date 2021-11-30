package org.videoco.models;

import org.videoco.controllers.Controller;
import org.videoco.controllers.MovieController;
import org.videoco.utils.Utils;

public class MovieModel implements Model {
    private String id;
    private String title;
    private String description;
    private String category;
    private String actors;
    private String directors;
    private String dateOfRelease;
    private int amountInStock;
    private String cardImage;

    public MovieModel(String id, String title, String description, String category, int amountInStock) {
        this.setID(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setCategory(category);
        this.setAmountInStock(amountInStock);
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getDateOfRelease() {
        return dateOfRelease;
    }

    public void setDateOfRelease(String dateOfRelease) {
        this.dateOfRelease = dateOfRelease;
    }

    public int getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(int amountInStock) {
        this.amountInStock = amountInStock;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String uri) {
        this.cardImage = cardImage;
    }

    @Override
    public String getDatabaseKey() {
        return this.getID();
    }

    @Override
    public Controller createController() {
        return new MovieController();
    }
}
