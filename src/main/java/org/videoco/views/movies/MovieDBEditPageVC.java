package org.videoco.views.movies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.videoco.controllers.database.MetadataFields;
import org.videoco.controllers.movies.MovieController;
import org.videoco.factories.MovieFactory;
import org.videoco.models.Model;
import org.videoco.models.MovieModel;
import org.videoco.views.ViewController;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.ResourceBundle;

public class MovieDBEditPageVC extends ViewController implements Initializable {
    @FXML Button addRecordBttn;
    @FXML Button deleteRecordBttn;
    @FXML Button updateRecordBttn;

    @FXML TextField title;
    @FXML DatePicker dateOfRelease;
    @FXML ChoiceBox<MovieModel.MovieCategory> category;
    @FXML TextArea description;
    @FXML TextField actors;
    @FXML TextField directors;
    @FXML TextField amountInStock;

    private boolean rmvAddBtn = false;
    private boolean rmvDeleteBtn = false;
    private boolean rmvUpdateBtn = false;

    @FXML
    public void addRecord(ActionEvent event) {
        MovieController controller = new MovieController(user);
        MovieFactory factory = new MovieFactory();
        setupMovieFactory(factory);
        String error = factory.findErrorInRequiredFields();
        if (error==null || error.isBlank()) {
            if (controller.addDBRecord(factory)!=null) {
                this.createAlert(Alert.AlertType.INFORMATION, "Movie successfully added to database.");
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.close();
                return;
            }
            error = "Record already exists in our database";
        }
        this.createAlert(Alert.AlertType.ERROR, error);
    }

    @FXML
    public void deleteRecord(ActionEvent event) {
        MovieController controller = new MovieController(user);
        if (controller.removeDBRecord(this.model.getDatabaseKey()))
            this.createAlert(Alert.AlertType.INFORMATION, "Movie successfully removed from database");
        else
            this.createAlert(Alert.AlertType.ERROR, "Movie could not be found in our database.");
    }

    @FXML
    public void updateRecord(ActionEvent event) {
        MovieController controller = new MovieController(user);
        MovieFactory factory = new MovieFactory();
        setupMovieFactory(factory);
        String error = factory.findErrorInRequiredFields();
        if (error==null || error.isBlank()) {
            if (controller.hasDatabaseEntry(this.model.getDatabaseKey())) {
                if (controller.updateRecord(this.model.getDatabaseKey(), factory)!=null) {
                    this.createAlert(Alert.AlertType.INFORMATION, "Movie successfully updated in our database");
                    return;
                } else {
                     error = "Something went wrong... couldn't update movie record.";
                }
            } else {
                error = "Movie could not be found in our database.";
            }
        }
        this.createAlert(Alert.AlertType.ERROR, error);
    }

    public void setupMovieFactory(MovieFactory factory) {
        factory.setTitle(title.getText());
        try {
            factory.setDateOfRelease(dateOfRelease.getValue().format(DateTimeFormatter.ofPattern(Objects.requireNonNull(MovieController.getMetadata(MetadataFields.DATE_FORMAT)))));
        }catch(Exception e) {
            e.printStackTrace();
        }
        factory.setCategory(category.getValue());
        factory.setDescription(description.getText());
        factory.setActors(actors.getText());
        factory.setDirectors(directors.getText());
        factory.setAmountInStock(amountInStock.getText());
    }

    public void setupFields(MovieModel movie) {
        title.setText(movie.getTitle());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Objects.requireNonNull(MovieController.getMetadata(MetadataFields.DATE_FORMAT)));
        try {
            LocalDate localDate = LocalDate.parse(movie.getDateOfRelease(), formatter);
            dateOfRelease.setValue(localDate);
        }catch (DateTimeParseException e) {
            e.printStackTrace();
        }
        category.setValue(movie.getCategory());
        description.setText(movie.getDescription());
        actors.setText(movie.getActors());
        directors.setText(movie.getDirectors());
        amountInStock.setText(String.valueOf(movie.getAmountInStock()));
    }


    public void removeAddBttn() {
        this.rmvAddBtn = true;
    }

    public void removeDeleteBttn() {
        this.rmvDeleteBtn = true;
    }

    public void removeUpdateBttn() {
        this.rmvUpdateBtn = true;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (rmvAddBtn) ((Pane) addRecordBttn.getParent()).getChildren().remove(addRecordBttn);
        if (rmvDeleteBtn) ((Pane) deleteRecordBttn.getParent()).getChildren().remove(deleteRecordBttn);
        if (rmvUpdateBtn) ((Pane) updateRecordBttn.getParent()).getChildren().remove(updateRecordBttn);

        for (MovieModel.MovieCategory cat : MovieModel.MovieCategory.values()) {
            if (cat!= MovieModel.MovieCategory.ALL)
                category.getItems().add(cat);
        }
    }
    @Override
    public void setModel(Model model) {
        if (model==null) return;
        super.setModel(model);
        setupFields((MovieModel) this.model);
    }
}
