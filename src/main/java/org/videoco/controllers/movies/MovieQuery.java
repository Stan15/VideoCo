package org.videoco.controllers.movies;


import org.videoco.models.MovieModel;
import org.videoco.utils.database_queries.Query;

public class MovieQuery extends Query<MovieModel> {
    MovieModel.MovieCategory category;
    boolean showOutOfStock;
    public MovieQuery(String content) {
        this(content, MovieModel.MovieCategory.ALL);
    }

    @Override
    public int matchScore(MovieModel movie) {
        if (this.getCategory()!=movie.getCategory())
        if (this.getContent()==null || this.getContent().isBlank()) return 1;
        int score = 0;
        score += movie.getTitle().toLowerCase().contains(this.getContent().toLowerCase()) ? 5 : 0;
        score += movie.getActors().toLowerCase().contains(this.getContent().toLowerCase()) ? 3 : 0;
        score += movie.getDescription().toLowerCase().contains(this.getContent().toLowerCase()) ? 2 : 0;
        return score;
    }

    public MovieQuery(String content, MovieModel.MovieCategory category) {
        this(content, category, false);
    }

    public MovieQuery(String content, MovieModel.MovieCategory category, boolean showOutOfStock) {
        super(content);
        this.category = category;
        this.showOutOfStock = showOutOfStock;
    }

    public MovieModel.MovieCategory getCategory() {
        return category;
    }
}