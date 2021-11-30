package org.videoco.factories;

import org.videoco.models.MovieModel;

public class MovieFactory implements Factory {
    public String id;
    public String title;
    public String description;
    public String category;
    public String actors;
    public String directors;
    public String dateOfRelease;
    public int amountInStock;


    @Override
    public MovieModel createModel() {
        MovieModel model = new MovieModel(id, title, description, category, 0);
        model.setActors(this.actors);
        model.setDirectors(this.directors);
        model.setDateOfRelease(this.dateOfRelease);
        return model;
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
}
