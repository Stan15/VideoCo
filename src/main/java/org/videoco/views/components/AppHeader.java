package org.videoco.views.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.videoco.controllers.users.UserController;
import org.videoco.views.ViewController;

public class AppHeader extends ViewController {
    @FXML Hyperlink logoutLink;
    @FXML Label homeLogo;
    @FXML ImageView accountIcon;

    @FXML
    public void goToHomepage(ActionEvent event) {
        UserController.transitionToHomepage(event);
    }

    @FXML
    public void goToAccountPage(ActionEvent event) {

    }

    @FXML
    public void logout(ActionEvent event) {
        UserController.logout(event);
    }
}
