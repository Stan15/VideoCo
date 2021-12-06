package org.videoco.views;

public enum ViewEnum {
    MOVIE_BROWSER("/org.videoco/views/movies/movie-browser.fxml"),
    MOVIE_DB_BROWSER("/org.videoco/views/movies/movie-db-browser.fxml"),
    MOVIE_PAGE("/org.videoco/views/movies/movie-page.fxml"),
    MOVIE_DB_EDIT_PAGE("/org.videoco/views/movies/movie-db-edit-page.fxml"),
    ORDER_BROWSER("/org.videoco/views/orders/order-browser.fxml"),
    ORDER_PAGE("/org.videoco/views/orders/order-page.fxml"),
    APP_HEADER("/org.videoco/views/components/app-header.fxml"),
    LOGIN("/org.videoco/views/authentication/login.fxml"),
    REGISTER("/org.videoco/views/authentication/register.fxml"),
    SIDEBAR("/org.videoco/views/sidebar/sidebar.fxml"),
    SIDEBAR_INFO_ITEM("/org.videoco/views/sidebar/sidebar-info-item.fxml"),
    EMPLOYEE_ID_GENERATOR("/org.videoco/views/users/employee-id-generator.fxml"),
    EMPLOYEE_BROWSER("/org.videoco/views/users/employee-browser.fxml"),
    CUSTOMER_BROWSER("/org.videoco/views/users/customer-browser.fxml"),
    USER_PROFILE("/org.videoco/views/users/user-profile.fxml"),
    PRIVILEGED_EMPLOYEE_PROFILE_EDITOR("/org.videoco/views/users/privileged-employee-profile-editor.fxml");

    public final String src;
    ViewEnum(String src) {
        this.src = src;
    }
}
