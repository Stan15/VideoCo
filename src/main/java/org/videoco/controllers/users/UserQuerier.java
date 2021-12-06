package org.videoco.controllers.users;

import org.videoco.controllers.orders.OrderQuery;
import org.videoco.models.orders.OrderStatus;
import org.videoco.models.users.UserModel;
import org.videoco.utils.database_queries.DBQuerier;

import java.util.Objects;

public class UserQuerier extends DBQuerier<UserModel> {
    public UserQuerier(UserController controller, UserQuery query, ListSource<UserModel> source) {
        super(controller, query, source);
    }
    public UserQuerier(UserController controller, ListSource<UserModel> source) {
        this(controller, new UserQuery("", UserType.ALL), source);
    }
    public UserQuerier(UserController controller) {
        this(controller, DBQuerier::defaultListSource);
    }

    @Override
    public boolean filterPredicate(UserModel user) {
        boolean filterUserType = ((UserQuery) this.query).getType()==UserType.ALL || ((UserQuery) this.query).getType()==user.getType();
        return super.filterPredicate(user) && filterUserType;
    }
}
