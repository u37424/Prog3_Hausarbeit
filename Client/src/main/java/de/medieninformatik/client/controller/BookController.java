package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.IBookController;
import de.medieninformatik.client.model.MainModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class BookController implements IBookController {
    @FXML private Button backButton;

    @FXML private Label title, isbn, pages, year;

    private Stage stage;
    private MainModel model;

    @FXML
    public void initialize() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(MainModel model) {
        this.model = model;
    }

    @Override
    public void returnToMain() {

    }

    @Override
    public void editBookInfo() {

    }

    @Override
    public void editBookAuthors() {

    }

    @Override
    public void editBookPublisher() {

    }

    @Override
    public void editBookCategories() {

    }

    @Override
    public void editBookDescription() {

    }

    @Override
    public void deleteBook() {

    }
}
