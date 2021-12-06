package org.videoco.views;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.videoco.models.Model;
import org.videoco.models.users.UserModel;
import org.videoco.utils.database_queries.DBQuerier;
import org.videoco.utils.database_queries.Query;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class DatabaseBrowser<M extends Model> extends ViewController implements Initializable {
    @FXML protected TextField searchBar;
    @FXML protected Label errorLabel;

    @FXML protected TableView<M> tableView;

    protected DBQuerier<M> querier;

    protected abstract void setColumns();
    protected abstract DBQuerier<M> createQuerier();
    protected abstract Query<M> getQuery();
    @FXML
    public abstract void onModelSelect(MouseEvent event);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setColumns();
        this.querier = this.createQuerier();
        searchBar.textProperty().addListener(
                (observable) -> this.updateQuery()
        );

        ObservableList<M> displayedList = querier.getQueriedList();
        displayedList.addListener((ListChangeListener<Model>) c -> {
            if (displayedList.isEmpty()) setError("No items matched your search.");
            else clearError();
        });
        tableView.setItems(displayedList);
    }

    @Override
    public void setUser(UserModel user) {
        super.setUser(user);
        this.initialize(null, null);
        this.updateQuery();
    }

    @Override
    public void setModel(Model model) {
        super.setModel(model);
        this.updateQuery();
    }

    @FXML
    protected void updateQuery() {
        this.querier.setQuery(this.getQuery());
    }

    public void setError(String msg) {
        this.errorLabel.setText(msg);
        this.errorLabel.getStyleClass().clear();
        this.errorLabel.getStyleClass().add("error-msg-visible");
    }

    public void clearError() {
        this.errorLabel.setText("");
        this.errorLabel.getStyleClass().clear();
        this.errorLabel.getStyleClass().add("error-msg-hidden");
    }
}
