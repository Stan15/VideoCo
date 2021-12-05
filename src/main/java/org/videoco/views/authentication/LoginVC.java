package org.videoco.views.authentication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.videoco.controllers.users.UserController;
import org.videoco.models.users.UserModel;
import org.videoco.views.ViewController;
import org.videoco.views.ViewEnum;

import java.io.IOException;
import java.util.Objects;

public class LoginVC extends ViewController {
    @FXML TextField email;
    @FXML PasswordField password;
    @FXML Label errorMsgLabel;
    @FXML Group errorMsgGroup;

    @FXML
    public void switchToRegisterScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(ViewEnum.REGISTER.src)));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public void loginUser(ActionEvent event) {
        String email = this.email.getText().strip();
        String password = this.password.getText().strip();

        String errorMsg = "";
        if (email.isBlank()) errorMsg = "Enter an email.";
        else if (!email.matches("[a-z][a-z0-9]*@[a-z]+\\.[a-z]+")) errorMsg = "Incorrect email format.";
        else if (password.isBlank()) errorMsg = "Enter a password.";

        if (!errorMsg.isBlank()) {
            this.setError(errorMsg);
            return;
        }
        this.clearError();

        UserController.AuthPackage authPackage = UserController.login(email, password);
        UserModel model = authPackage.getUserModel();
        if (model==null) {
            errorMsg = authPackage.getErrorMsg();
            this.setError(errorMsg);
        }else {
            model.createController().transitionToHomeView(event);
        }
    }

    public void setError(String msg) {
        this.errorMsgLabel.setText(msg);
        this.errorMsgGroup.getStyleClass().clear();
        this.errorMsgGroup.getStyleClass().add("error-msg-visible");
    }

    public void clearError() {
        this.errorMsgLabel.setText("");
        this.errorMsgGroup.getStyleClass().clear();
        this.errorMsgGroup.getStyleClass().add("error-msg-hidden");
    }
}
