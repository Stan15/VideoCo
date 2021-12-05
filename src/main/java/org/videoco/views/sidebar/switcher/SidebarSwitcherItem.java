package org.videoco.views.sidebar.switcher;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.videoco.controllers.Controller;
import org.videoco.controllers.users.UserController;
import org.videoco.models.Model;
import org.videoco.models.users.UserModel;
import org.videoco.utils.observer.Notifier;
import org.videoco.utils.observer.Observer;
import org.videoco.utils.observer.events.VCOEvent;
import org.videoco.views.ViewController;
import org.videoco.views.ViewEnum;
import org.videoco.views.sidebar.SidebarItem;
import org.videoco.views.sidebar.info.SidebarInfoItemVC;

public class SidebarSwitcherItem extends SidebarItem {
    private final String name;
    private final ViewEnum view;
    private final Controller controller;

    public SidebarSwitcherItem(String name, ViewEnum view, Controller controller) {
        this.name = name;
        this.view = view;
        this.controller = controller;
    }

    @Override
    public Node createNode() {
        Button button = new Button();
        button.setText(this.name);
        button.setOnAction((e) -> {
            this.controller.transition(view, e);
        });
        return button;
    }
}