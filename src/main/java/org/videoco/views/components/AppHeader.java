package org.videoco.views.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.videoco.controllers.users.UserController;
import org.videoco.views.ViewController;
import org.videoco.views.ViewEnum;

public class AppHeader extends ViewController {
    @FXML Hyperlink logoutLink;
    @FXML Label homeLogo;
    @FXML ImageView accountIcon;

    @FXML
    public void goToHomepage(MouseEvent event) {
        UserController.transitionToHomepage(event);
    }

    @FXML
    public void goToAccountPage(MouseEvent event) {
        this.user.createController().createPopup(ViewEnum.USER_PROFILE, event);
    }

    @FXML
    public void logout(ActionEvent event) {
        UserController.logout(event);
    }
}
