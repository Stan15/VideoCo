package org.videoco.views.sidebar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.videoco.views.ViewController;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SidebarVC extends ViewController {
    @FXML VBox sidebar;

    private final List<Node> sidebarItems = new ArrayList<>();

    public void addItem(SidebarItem item) {
        this.sidebarItems.add(item.createNode());
        this.sidebar.getChildren().add(item.createNode());
    }
}
