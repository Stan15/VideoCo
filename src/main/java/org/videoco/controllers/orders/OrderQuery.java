package org.videoco.controllers.orders;

import org.videoco.controllers.movies.MovieQuery;
import org.videoco.models.MovieModel;
import org.videoco.models.orders.OrderModel;
import org.videoco.utils.database_queries.Query;

public class OrderQuery extends Query<OrderModel> {
    private final String status;
    public OrderQuery(String content, String status) {
        super(content);
        this.status = status;
    }
    @Override
    public int matchScore(OrderModel order) {
        if (this.getContent()==null || this.getContent().isBlank()) return 1;
        int score = 0;
        MovieQuery movieQuery = new MovieQuery(this.content, MovieModel.MovieCategory.ALL);
        for (MovieModel movie : order.getMovies()) {
            score += movieQuery.matchScore(movie);
        }
        return score;
    }

    public String getStatus() {
        return status;
    }
}
