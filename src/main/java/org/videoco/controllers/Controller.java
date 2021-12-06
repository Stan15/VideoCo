package org.videoco.controllers;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.videoco.controllers.users.UserController;
import org.videoco.models.Model;
import org.videoco.models.users.UserModel;
import org.videoco.views.ViewController;
import org.videoco.views.ViewEnum;
import org.videoco.views.ViewModifier;
import org.videoco.views.sidebar.info.SidebarInfoItem;
import org.videoco.views.sidebar.switcher.SidebarSwitcherItem;
import org.videoco.views.sidebar.SidebarVC;

import java.util.List;
import java.util.Objects;

public abstract class Controller {
    protected UserModel user;
    protected Model focusModel;
    protected static ViewEnum currentView;

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserModel getUser() {
        return user;
    }

    public Model getFocusModel() {
        return focusModel;
    }

    public void setFocusModel(Model model) {
        this.focusModel = model;
    }

    public void transition(ViewEnum view, Event event) {
        transition(view, event, (v, l) -> v);
    }
    public void transition(ViewEnum view, Event event, ViewModifier viewModifier) {
        try {
            if (!this.isValidTransition(view))
                throw new Exception("Invalid view transition");

            //get and load view
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(view.src)));
            Parent root = loader.load();
            setupViewController(loader);

            //modify view
            root = (Parent) viewModifier.modify(root, loader);

            //attach app header
            if (view!= ViewEnum.REGISTER && view!= ViewEnum.LOGIN)
                root = wrapWithUserControls(root);

            //display view
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            currentView = view;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createPopup(ViewEnum view, Event event) {
        createPopup(view, event, (v, l) -> v);
    }
    public void createPopup(ViewEnum view, Event event, ViewModifier viewModifier) {
        try {
            if (!this.isValidTransition(view))
                throw new Exception("Invalid view transition");
            Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(primaryStage);

            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(view.src)));
            Parent dialogView = loader.load();
            setupViewController(loader);
            dialogView = (Parent) viewModifier.modify(dialogView, loader);

            Scene dialogScene = new Scene(dialogView);
            dialog.setScene(dialogScene);
            dialog.show();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Parent wrapWithUserControls(Node node) {
        if (this.user==null) return (Parent) node;
        try {
            BorderPane parent = new BorderPane();
            parent.setCenter(node);

            //add the header
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource(ViewEnum.APP_HEADER.src)));
            Node header = loader.load();
            setupViewController(loader);
            parent.setTop(header);

            //add switcher sidebar
            List<SidebarSwitcherItem> switchers = this.user.createController().getSidebarSwitcherItems();
            if (!switchers.isEmpty()) {
                loader = new FXMLLoader(getClass().getResource(ViewEnum.SIDEBAR.src));
                Node sidebar = loader.load();
                SidebarVC viewController = (SidebarVC) setupViewController(loader);
                for (SidebarSwitcherItem switcher : switchers) {
                    viewController.addItem(switcher);
                }
                parent.setLeft(sidebar);
            }

            //add info sidebar
            List<SidebarInfoItem> infoItems = this.user.createController().getSidebarInfoItems();
            if (!infoItems.isEmpty()) {
                loader = new FXMLLoader(getClass().getResource(ViewEnum.SIDEBAR.src));
                Node infoBar = loader.load();
                SidebarVC viewController = (SidebarVC) setupViewController(loader);
                for (SidebarInfoItem infoItem : infoItems) {
                    viewController.addItem(infoItem);
                }
                parent.setRight(infoBar);
            }
            return parent;
        }catch (Exception e) {
            e.printStackTrace();
            return (Parent) node;
        }
    }
    private ViewController setupViewController(FXMLLoader loader) {
        ViewController controller = loader.getController();
        controller.setUser(this.user);
        controller.setModel(this.focusModel);
        return controller;
    }
    public abstract boolean isValidTransition(ViewEnum view);
}
