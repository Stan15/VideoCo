package org.videoco.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.videoco.models.Model;
import org.videoco.views.ViewType;

import java.util.Objects;

public abstract class Controller {
    protected Model focusModel;
    protected ViewType currentViewType;

    public void setFocusModel(Model model) {
        this.focusModel = model;
    }

    public abstract void transitionToHomeView(ActionEvent event);
    public void transition(ViewType view, ActionEvent event) throws Exception {
        if (!this.isValidTransition(view))
            throw new Exception("Invalid view transition");
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(view.src)));
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        this.currentViewType = view;
    }
    public abstract boolean isValidTransition(ViewType view);
}
