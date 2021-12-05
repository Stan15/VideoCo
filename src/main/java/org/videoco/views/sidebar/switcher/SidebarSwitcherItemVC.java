package org.videoco.views.sidebar.switcher;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.videoco.models.users.UserModel;
import org.videoco.views.ViewEnum;

public class SidebarSwitcherItemVC {
    @FXML Button button;

    public void setName(String name) {
        this.button.setText(name);
    }
}
