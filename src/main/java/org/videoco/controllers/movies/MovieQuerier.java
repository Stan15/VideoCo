package org.videoco.controllers.movies;


import org.videoco.models.MovieModel;
import org.videoco.utils.database_queries.DBQuerier;

public class MovieQuerier extends DBQuerier<MovieModel> {
    public MovieQuerier(MovieController controller, ListSource<MovieModel> source) {
        super(controller, new MovieQuery(""), source);
    }
    public MovieQuerier(MovieController controller) {
        this(controller, DBQuerier::defaultListSource);
    }

    @Override
    public boolean filterPredicate(MovieModel movie) {
        MovieModel.MovieCategory cat = MovieModel.MovieCategory.valueOf(movie.getCategory());
        boolean filterOurOfStock = (Integer.parseInt(movie.getAmountInStock())>0 || ((MovieQuery) this.query).showOutOfStock);
        boolean filterCategory = (((MovieQuery) this.query).getCategory()== MovieModel.MovieCategory.ALL || cat == ((MovieQuery) this.query).getCategory());
        return super.filterPredicate(movie) && filterOurOfStock && filterCategory;
    }
}