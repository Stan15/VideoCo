package org.videoco.views.sidebar.info;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.videoco.controllers.users.UserController;
import org.videoco.models.Model;
import org.videoco.utils.observer.Notifier;
import org.videoco.utils.observer.Observer;
import org.videoco.utils.observer.events.DBEvent;
import org.videoco.utils.observer.events.VCOEvent;
import org.videoco.views.ViewEnum;
import org.videoco.views.sidebar.SidebarItem;

public class SidebarInfoItem extends SidebarItem implements Observer<VCOEvent, Model> {
    Model user;
    String title;
    StringProperty infoValue;
    ValueRetriever valueRetriever;

    public SidebarInfoItem(UserController controller, String title, ValueRetriever valueRetriever) {
        this.user = controller.getUser();
        this.title = title;
        this.valueRetriever = valueRetriever;
        this.infoValue = new SimpleStringProperty(valueRetriever.getValue(this.user));
        this.subscribe(controller, DBEvent.CHANGE);
    }

    public void subscribe(Notifier<VCOEvent, Model> notifier, VCOEvent event) {
        notifier.attachObserver(this, event);
    }

    @Override
    public void unsubscribe(Notifier<VCOEvent, Model> notifier, VCOEvent event) {
        notifier.detachObserver(this, event);
    }

    @Override
    public void callback(Model model) {
        if (!model.equals(this.user)) return;
        this.infoValue.setValue(this.valueRetriever.getValue(model));
    }

    @Override
    public Node createNode() {
        //the item in the node must be an observable value
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ViewEnum.SIDEBAR_INFO_ITEM.src));
            Node sidebarItem = loader.load();
            SidebarInfoItemVC controller = loader.getController();
            controller.setTitle(this.title);
            controller.bindValue(this.infoValue);
            return sidebarItem;
        }catch (Exception e) {
            e.printStackTrace();
            return new VBox();
        }
    }

    public interface ValueRetriever {
        String getValue(Model model);
    }
}
