package org.videoco.controllers.users;

import org.videoco.models.users.UserModel;
import org.videoco.utils.database_queries.Query;

public class UserQuery extends Query<UserModel> {
    UserType type;
    public UserQuery(String content) {
        this(content, UserType.ALL);
    }
    public UserQuery(String content, UserType type) {
        super(content);
        this.type = type;
    }

    @Override
    public int matchScore(UserModel user) {
        if (this.getContent()==null || this.getContent().isBlank()) return 1;
        int score = 0;
        score += user.getID().toLowerCase().contains(this.getContent().toLowerCase()) ? 5 : 0;
        score += user.getEmail().toLowerCase().contains(this.getContent().toLowerCase()) ? 3 : 0;
        score += user.getName().toLowerCase().contains(this.getContent().toLowerCase()) ? 2 : 0;
        return score;
    }

    public UserType getType() {
        return this.type;
    }


}
