package org.videoco.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public interface ViewModifier {
    Node modify(Node node, FXMLLoader loader);
}