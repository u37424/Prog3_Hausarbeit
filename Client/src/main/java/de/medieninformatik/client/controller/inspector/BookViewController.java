package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.model.MainModel;
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

public class BookViewController extends ViewController<Book> {
    @FXML
    private Label title, isbn, pages, year, rating, publisher;

    @FXML
    private HBox authors, categories, ratingBox;

    @FXML
    private TextArea description;

    @FXML
    private Button editIsbn, editYear, editPages, editTitle, editPublisher, addAuthor, addCategory;

    public void setStage(Stage stage) {
        super.setStage(stage);
        this.stage.setTitle("Book Inspector");
    }

    public void setModel(MainModel model) {
        super.setModel(model);
        //Model received
        setOptions();

        //Load Book when Model received
        Book book = model.getBookRequest().getSelection();
        displayValues(book);
    }

    @Override
    public void setOptions() {
        if (!model.isMainUser()) return;
        super.setOptions();

        boolean isEdit = model.isEditMode();
        boolean isCreate = model.isCreateMode();

        //Specific edit Buttons
        this.editTitle.setVisible(isEdit);
        this.editYear.setVisible(isEdit);
        this.editPages.setVisible(isEdit);
        this.editPublisher.setVisible(isEdit);
        this.addAuthor.setVisible(isEdit);
        this.addCategory.setVisible(isEdit);


        //Wenn im Create Modus
        this.editIsbn.setVisible(isCreate);
        this.description.setEditable(isCreate);
        this.description.setDisable(!isCreate);
        if (isCreate)
            ratingBox.getChildren().forEach(n -> n.setOnMouseClicked(e -> editBookRating(n.getId())));    //Rating durch Klicks auf Sterne
        if (isCreate) this.description.setText("");
    }

    @Override
    public void displayValues(Book book) {
        if (book == null) return;
        //Fill with Book Data
        if (book.getTitle() != null && !book.getTitle().isBlank())
            this.title.setText(book.getTitle());

        if (book.getIsbn() != null && !book.getIsbn().isBlank())
            this.isbn.setText(book.getIsbn());

        this.year.setText(String.valueOf(book.getReleaseYear()));
        this.pages.setText(String.valueOf(book.getPages()));
        this.rating.setText(String.valueOf(book.getRating()));

        if (book.getDescription() != null && !book.getDescription().isBlank()) {
            this.description.setText(book.getDescription());
            this.description.setDisable(false);
        }

        //Load Publisher into Label
        if (book.getPublisher() != null) this.publisher.setText(book.getPublisher().toString());

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
        super.returnToMain();

        sceneController.loadMainBookScene();
    }

    //--------EDIT VALUES

    public void editBookTitle() {

    }

    public void editBookISBN() {

    }

    public void editBookYear() {

    }

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
        displayValues(model.getBookRequest().getSelection());
    }

    public void editBookPublisher() {
        displayValues(model.getBookRequest().getSelection());
    }

    public void addBookAuthor() {
        displayValues(model.getBookRequest().getSelection());
    }

    public void addBookCategory() {
        displayValues(model.getBookRequest().getSelection());
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
    public void deleteItem() {
        if (sceneController.confirmMessage("Delete Book", "Do you really want to delete " + model.getBookRequest().getSelection().getIsbn() + " ?")) {
            boolean res = model.getBookRequest().deleteBook(model.getBookRequest().getSelection().getIsbn());
            if (res) sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
