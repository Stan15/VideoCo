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
import org.videoco.views.ViewType;

import java.io.IOException;
import java.util.Objects;

public class RegisterView {
    @FXML TextField firstName;
    @FXML TextField lastName;
    @FXML TextField email;
    @FXML PasswordField password;
    @FXML PasswordField confirmPassword;
    @FXML TextField employeeRegistrationCode;

    @FXML Label errorMsgLabel;
    @FXML Group errorMsgGroup;

    @FXML
    public void switchToLoginScreen(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(ViewType.LOGIN.src)));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    public void createAccount(ActionEvent event) {
        String errorMsg = "";

        String firstName = this.firstName.getText();
        String lastName = this.lastName.getText();
        if (firstName.isBlank()) {
            errorMsg = "First name is missing.";
        }
        String name = firstName.strip() + " " + lastName.strip();

        String password = this.password.getText().strip();
        if (password.isBlank()) errorMsg = "Password missing.";

        if (password.length()<8) errorMsg = "Use 8 characters or more for your password.";

        if (!password.equals(this.confirmPassword.getText().strip())) {
            errorMsg = "Those passwords didn't match. Try again.";
        }

        String email = this.email.getText().strip();
        if (email.isBlank()) errorMsg = "Chose an email address.";
        else if (!email.matches("[a-z][a-z0-9]*@[a-z]+\\.[a-z]+")) errorMsg = "Incorrect email format.";

        if (!errorMsg.isBlank()) {
            this.setError(errorMsg);
            return;
        }
        this.clearError();

        UserController.AuthPackage authPackage = UserController.registerUser(name, email, password, employeeRegistrationCode.getText());
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
