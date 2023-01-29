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

public class BookViewController implements IBookController {
    @FXML
    private Label title, isbn, pages, year, rating;

    @FXML
    private HBox publisher, authors, categories, ratingBox;

    @FXML
    private TextArea description;

    @FXML
    private Button submitButton, deleteButton, backButton;

    @FXML
    private Button editIsbn, editYear, editPages, editTitle, editPublisher, editAuthors, editCategories;

    private Stage stage;
    private MainModel model;
    private SceneController sceneController;

    @FXML
    public void initialize() {

    }

    public void setStage(Stage stage) {
        this.stage = stage;
        //Stage received
        this.stage.setTitle("Book Inspector");
    }

    public void setModel(MainModel model) {
        this.model = model;
        //Model received
        setOptions();
        Book book = model.getBookRequest().getSelection();
        if (book == null) return;
        displayBook(book);
    }

    @Override
    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    private void setOptions() {
        if (!model.isMainUser()) return;
        //Wenn im Editing Modus
        boolean isEdit = model.isEditMode();
        boolean isCreate = model.isCreateMode();
        this.editTitle.setVisible(isEdit);
        this.editYear.setVisible(isEdit);
        this.editPages.setVisible(isEdit);
        this.editPublisher.setVisible(isEdit);
        this.editAuthors.setVisible(isEdit);
        this.editCategories.setVisible(isEdit);

        this.editIsbn.setVisible(isEdit && isCreate);
        this.submitButton.setVisible(isEdit);
        this.deleteButton.setVisible(isEdit && !isCreate);
        ((ImageView) this.backButton.getGraphic()).setImage(new Image((isEdit) ? "exit.png" : "return.png"));
        if (isEdit) this.stage.setTitle("Book Editor");

        //Wenn im Create Modus
        this.description.setEditable(isCreate);
        this.description.setDisable(!isCreate);
        if (isCreate)
            ratingBox.getChildren().forEach(n -> n.setOnMouseClicked(e -> editBookRating(n.getId())));    //Rating durch Klicks auf Sterne
        if (isCreate) this.description.setText("");
    }

    @Override
    public void displayBook(Book book) {
        //Fill with Book Data
        if (book.getTitle() != null && !book.getTitle().isBlank())
            this.title.setText(book.getTitle());

        if (book.getIsbn() != null && !book.getIsbn().isBlank())
            this.isbn.setText(book.getIsbn());

        this.year.setText(String.valueOf(book.getReleaseYear()));
        this.pages.setText(String.valueOf(book.getPages()));
        this.rating.setText(String.valueOf(book.getRating()));

        if (book.getDescription() != null && !book.getDescription().isBlank())
            this.description.setText(book.getDescription());

        //Load Publisher into Label
        if (book.getPublisher() != null)
            ((Label) this.publisher.getChildren().get(0)).setText(book.getPublisher().toString());

        //Build HBox representations of Authors and Categories
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
    public void returnToMain() {
        if (model.isCreateMode() || model.isEditMode()) {
            if (!sceneController.confirmMessage("Leave Editing", "Unsaved Changes will be lost!")) return;
        }
        model.setCreateMode(false);
        model.setEditMode(false);
        sceneController.loadMainBookScene();
    }

    @Override
    public void editBookTitle() {

    }

    @Override
    public void editBookISBN() {

    }

    @Override
    public void editBookYear() {

    }

    @Override
    public void editBookPages() {

    }

    public void editBookRating(String id) {
        String image = "star_filled.png";

        int val = 0;
        for (Node n : ratingBox.getChildren()) {
            val++;
            ((ImageView) n).setImage(new Image(image));
            if (id.equals(n.getId())) {
                image = "star_empty.png";
                model.getBookRequest().getSelection().setRating(val);
            }
        }
        displayBook(model.getBookRequest().getSelection());
    }

    @Override
    public void editBookAuthors() {
        displayBook(model.getBookRequest().getSelection());
    }

    @Override
    public void editBookPublisher() {
        displayBook(model.getBookRequest().getSelection());
    }

    @Override
    public void editBookCategories() {
        displayBook(model.getBookRequest().getSelection());
    }

    @Override
    public void submitChanges() {
        boolean res = model.isCreateMode() ? model.getBookRequest().createBook() : model.getBookRequest().editBook();
        if (res) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");

    }

    @Override
    public void deleteBook() {
        if (sceneController.confirmMessage("Delete Book", "Do you really want to delete " + model.getBookRequest().getSelection().getIsbn() + " ?")) {
            boolean res = model.getBookRequest().deleteBook(model.getBookRequest().getSelection().getIsbn());
            if (res) sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
