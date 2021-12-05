package org.videoco.utils.database_queries;

import org.videoco.models.Model;
import org.videoco.models.MovieModel;

public abstract class Query<M extends Model> {
    protected final String content;
    public Query(String content) {
        this.content = content;
    }
    public String getContent() {
        return this.content;
    }

    public abstract int matchScore(M model);
}
