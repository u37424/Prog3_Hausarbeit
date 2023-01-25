package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.IBookController;
import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.client.view.View;
import de.medieninformatik.common.Book;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class BookController implements IBookController {
    @FXML
    private Button backButton;

    @FXML
    private Label title, isbn, pages, year, rating;

    @FXML
    private HBox publisher, authors, categories, ratingBox;

    @FXML
    private TextArea description;

    @FXML
    private Button submitButton, deleteButton;

    private Stage stage;
    private MainModel model;

    @FXML
    public void initialize() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Book Inspector");
    }

    public void setModel(MainModel model) {
        this.model = model;
        setOptions();
        Book book = model.getSelection();
        if (book == null) return;
        displayBook(book);
    }

    private void setOptions() {
        if (!model.isMainUser()) return;
        //Wenn im Editing Modus
        boolean isEdit = model.isEditMode();
        boolean isCreate = model.isCrateMode();
        this.submitButton.setVisible(isEdit);
        this.deleteButton.setVisible(isEdit && !isCreate);

        //Wenn im Create Modus
        this.description.setEditable(isCreate);
        this.description.setDisable(!isCreate);
        if (isCreate) ratingBox.getChildren().forEach(n -> n.setOnMouseClicked(e -> editBookRating(n.getId())));
    }

    @Override
    public void displayBook(Book book) {
        if (book.getTitle() != null && !book.getTitle().isBlank())
            this.title.setText(book.getTitle());

        if (book.getIsbn() != null && !book.getIsbn().isBlank())
            this.isbn.setText(book.getIsbn());

        this.year.setText(String.valueOf(book.getReleaseYear()));
        this.pages.setText(String.valueOf(book.getPages()));
        this.rating.setText(String.valueOf(book.getRating()));

        if (book.getDescription() != null && !book.getDescription().isBlank())
            this.description.setText(book.getDescription());

        if (book.getPublisher() != null)
            ((Label) this.publisher.getChildren().get(0)).setText(book.getPublisher().toString());

        if (book.getAuthors() != null) buildAuthorBox();

        if (book.getCategories() != null) buildCategoryBox();
    }

    private void buildCategoryBox() {
        this.categories.getChildren().clear();
    }

    private void buildAuthorBox() {
        this.authors.getChildren().clear();
    }

    @Override
    @FXML
    public void returnToMain() {
        if (model.isCrateMode() || model.isEditMode()) {
            if (!View.confirmMessage("Leave Editing", "Unsaved Changes will be lost!")) return;
        }
        model.setCrateMode(false);
        model.setEditMode(false);
        View.loadScene("/main.fxml", stage, model);
    }

    public void editBookRating(String id) {
        String finalId = id;
        String image = "star_filled.png";
        for (Node n : ratingBox.getChildren()) {
            ((ImageView) n).setImage(new Image(image));
            if (finalId.equals(n.getId())) image = "star_empty.png";
        }
        id = id.replace("star", "");
        int val = Integer.parseInt(id);
        model.getSelection().setRating(val);
        displayBook(model.getSelection());
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
    public void deleteBook() {

    }
}
