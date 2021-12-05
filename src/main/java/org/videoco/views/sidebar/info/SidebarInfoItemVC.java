package org.videoco.views.sidebar.info;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.videoco.views.ViewController;

public class SidebarInfoItemVC extends ViewController {
    @FXML Label title;
    @FXML Label value;

    public void setTitle(String title) {
        this.title.setText(title);
    }
    public void bindValue(StringProperty val) {
        this.value.textProperty().bind(val);
    }
}
