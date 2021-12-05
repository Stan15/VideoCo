package org.videoco.views;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.videoco.models.Model;
import org.videoco.models.users.UserModel;

import java.util.Optional;

public abstract class ViewController {
    protected UserModel user = null;
    protected Model model = null;

    public void setUser(UserModel user) {
        this.user = user;
    }
    public void setModel(Model model) {
        this.model = model;
    }

    public void createAlert(Alert.AlertType type, String msg) {
        createAlert(type, msg, optional -> {});
    }
    public void createAlert(Alert.AlertType type, String msg, AlertAction action) {
        Alert alert = new Alert(type);
        alert.setContentText(msg);
        Optional<ButtonType> optional = alert.showAndWait();
        action.call(optional);
    }

    public interface AlertAction {
        void call(Optional<ButtonType> o);
    }
}
