package org.videoco.models;

import org.videoco.controllers.Controller;
import org.videoco.controllers.movies.MovieController;

public class MovieModel extends Model {
    private String title;
    private String description;
    private MovieCategory category;
    private String actors;
    private String directors;
    private String dateOfRelease;
    private String amountInStock;
    private String cardImage;

    public MovieModel(String id, String title, String description, MovieCategory category, String amountInStock) {
        this.setID(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setCategory(category);
        this.setAmountInStock(amountInStock);
    }

    public String getTitle() {
        return title;
    }

    public String getBlurb() {
        if (getDescription().length()>50) {
            return getDescription().substring(0, 50) + "...";
        }
        return getDescription();
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

    public MovieCategory getCategory() {
        return category;
    }

    public void setCategory(MovieCategory category) {
        if (category==null) return;
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

    public String getAmountInStock() {
        return amountInStock;
    }

    public void setAmountInStock(String amountInStock) {
        this.amountInStock = amountInStock;
    }

    public String getCardImage() {
        return cardImage;
    }

    public void setCardImage(String uri) {
        this.cardImage = cardImage;
    }

    public String getActorsFormated() {
        if (this.actors==null) return "";
        return String.join("; ", this.actors.split("-"));
    }
    public String getDirectorsFormated() {
        if (this.directors==null) return "";
        return String.join("; ", this.directors.split("-"));
    }

    @Override
    public String getDatabaseKey() {
        return this.getID();
    }

    public enum MovieCategory {
        ACTION, COMEDY, DRAMA, ROMANCE, HORROR, ALL
    }

    @Override
    public String toString() {
        return String.format("%s (ID#%s)", this.getTitle(), this.getID());
    }
}
