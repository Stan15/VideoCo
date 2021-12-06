package org.videoco.factories;

import org.videoco.controllers.movies.MovieController;
import org.videoco.models.Model;
import org.videoco.models.MovieModel;

public class MovieFactory extends Factory {
    public String id;
    public String title;
    public String description;
    public MovieModel.MovieCategory category;
    public String actors;
    public String directors;
    public String dateOfRelease;
    public String amountInStock;

    public MovieFactory(){}
    public MovieFactory(MovieModel movie) {
        super(movie);
    }

    @Override
    public void copy(Model model) {
        if (!(model instanceof MovieModel m)) return;
        this.setID(m.getID());
        this.setTitle(m.getTitle());
        this.setDescription(m.getDescription());
        this.setCategory(m.getCategory().name());
        this.setActors(m.getActors());
        this.setDirectors(m.getDirectors());
        this.setDateOfRelease(m.getDateOfRelease());
        this.setAmountInStock(m.getAmountInStock());
    }

    @Override
    public MovieModel createModel() {
        if (this.findErrorInRequiredFields()!=null) return null;
        if (this.id==null || !this.id.matches("-?\\d+(\\.\\d+)?")) this.setID(new MovieController().getNewID());
        MovieModel model = new MovieModel(id, title, description, category, amountInStock);
        model.setActors(this.actors);
        model.setDirectors(this.directors);
        model.setDateOfRelease(this.dateOfRelease);
        return model;
    }

    @Override
    public String findErrorInRequiredFields() {
        if (this.title==null || this.title.isBlank()) return "Missing movie title.";
        if (this.description==null || this.description.isBlank()) return "Missing movie description.";
        if (this.category==null) return "Missing movie category.";
        try {
            Integer.parseInt(this.amountInStock);
        }catch (Exception e) {
            return "Amount in stock should be a valid number.";
        }
        return null;
    }

    @Override
    public String getDatabaseKeyField() {
        return getID();
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

    public MovieModel.MovieCategory getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = MovieModel.MovieCategory.valueOf(category.strip().toUpperCase());
    }
    public void setCategory(MovieModel.MovieCategory category) {
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
}
