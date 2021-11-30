package org.videoco.views;

import org.videoco.models.users.UserModel;

public enum ViewType {
    MovieBrowser("/org.videoco.views/movies/movie-browser.fxml"),
    MoviePage("/org.videoco/views/movies/movie-page.fxml"),
    LOGIN("/org.videoco/views/authentication/login.fxml"),
    REGISTER("/org.videoco/views/authentication/register.fxml"),
    MovieCard("hi");

    public final String src;
    ViewType(String src) {
        this.src = src;
    }
}
