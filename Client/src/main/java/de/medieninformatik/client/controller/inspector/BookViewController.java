package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.LinkedList;

public class BookViewController extends ViewController {
    @FXML
    private Label title, isbn, pages, year, rating, publisher;

    @FXML
    private HBox authors, categories, ratingBox;

    @FXML
    private TextArea description;

    @FXML
    private Button editIsbn, editYear, editPages, editTitle, editPublisher, editAuthors, editCategories;

    public void setStage(Stage stage) {
        super.setStage(stage);
        this.stage.setTitle("Book Inspector");
    }

    public void setModel(MainModel model) {
        super.setModel(model);
        displayValues();
    }

    @Override
    public void setOptions() {
        if (!model.isMainUser()) return;
        super.setOptions();
        boolean isEdit = model.isEditMode();
        boolean isCreate = model.isCreateMode();

        //Edit Optionen
        this.editTitle.setVisible(isEdit);
        this.editYear.setVisible(isEdit);
        this.editPages.setVisible(isEdit);
        this.editPublisher.setVisible(isEdit);
        this.editAuthors.setVisible(isEdit);
        this.editCategories.setVisible(isEdit);
        this.description.setEditable(isEdit);
        if (isEdit)
            this.description.textProperty().addListener((obs) -> model.getBookRequest().getItem().setDescription(this.description.getText()));

        //Wenn im Create Modus
        this.editIsbn.setVisible(isCreate);
        if (isCreate)
            ratingBox.getChildren().forEach(star -> star.setOnMouseClicked(e -> editBookRating(star.getId())));  //Rating durch Klicks auf Sterne
        if (isCreate) this.description.setText("");
    }

    @Override
    public void displayValues() {
        Book book = model.getBookRequest().getItem();
        if (book == null) return;

        //Daten aus Buch laden
        this.title.setText(book.getTitle());
        this.isbn.setText(book.getIsbn());
        this.year.setText(String.valueOf(book.getReleaseYear()));
        this.pages.setText(String.valueOf(book.getPages()));
        this.rating.setText(String.valueOf(book.getRating()));
        displayRating(book.getRating());    //Darstellung als Sterne

        if (book.getDescription() != null && !book.getDescription().isBlank()) {
            this.description.setText(book.getDescription());
            this.description.setDisable(false);
        }

        //Publisher als Label
        if (book.getPublisher() != null) this.publisher.setText(book.getPublisher().toString());
        //Autoren und Kategorien in HBox Containern
        if (book.getAuthors() != null) buildAuthorBox();
        if (book.getCategories() != null) buildCategoryBox();
    }

    private void displayRating(double rating) {
        int r = (int) rating;
        if (r == 0) return;
        String id = "star" + r;
        String image = "star_filled.png";

        for (Node n : ratingBox.getChildren()) {
            ((ImageView) n).setImage(new Image(image));
            if (id.equals(n.getId())) {
                image = "star_empty.png";
            }
        }
    }

    private void buildAuthorBox() {
        this.authors.getChildren().clear();
        boolean insertSeparator = true; //Um ersten Trennstrich abzuwarten

        for (Author author : model.getBookRequest().getItem().getAuthors()) {
            if (insertSeparator) insertSeparator = false;
            else this.authors.getChildren().add(new Separator(Orientation.VERTICAL));

            Label label = new Label(author.toString());
            label.setPadding(new Insets(10));
            this.authors.getChildren().add(label);
        }
    }

    private void buildCategoryBox() {
        this.categories.getChildren().clear();
        boolean insertSeparator = true; //Um ersten Trennstrich abzuwarten

        for (Category category : model.getBookRequest().getItem().getCategories()) {
            if (insertSeparator) insertSeparator = false;
            else this.categories.getChildren().add(new Separator(Orientation.VERTICAL));

            Label label = new Label(category.toString());
            label.setPadding(new Insets(10));
            this.categories.getChildren().add(label);
        }
    }

    @Override
    public void returnToMain() {
        super.returnToMain();
        sceneController.loadMainBookScene();
    }

    //--------EDIT VALUES

    public void editBookTitle() {
        String eingabe = sceneController.editStringMessage("Edit Title", this.title.getText(), "Book Title");
        if (eingabe == null) return;
        this.title.setText(eingabe);
        model.getBookRequest().getItem().setTitle(eingabe);
    }

    public void editBookISBN() {
        String eingabe = sceneController.editStringMessage("Edit ISBN", this.isbn.getText(), "Book ISBN");
        if (eingabe == null) return;
        this.isbn.setText(eingabe);
        model.getBookRequest().getItem().setIsbn(eingabe);
    }

    public void editBookYear() {
        int eingabe = sceneController.editNumberMessage("Edit Release Year", this.year.getText(), "Release Year");
        this.year.setText(String.valueOf(eingabe));
        model.getBookRequest().getItem().setReleaseYear(eingabe);
    }

    public void editBookPages() {
        int eingabe = sceneController.editNumberMessage("Edit Pages", this.pages.getText(), "Pages");
        this.pages.setText(String.valueOf(eingabe));
        model.getBookRequest().getItem().setPages(eingabe);
    }

    public void editBookRating(String id) {
        String image = "star_filled.png";

        int val = 0;
        for (Node n : ratingBox.getChildren()) {
            val++;
            ((ImageView) n).setImage(new Image(image));
            if (id.equals(n.getId())) {
                image = "star_empty.png";
                model.getBookRequest().getItem().setRating(val);
            }
        }
    }

    public void editBookPublisher() {
        model.getPublisherRequest().loadAll();

        Publisher publisher = sceneController.choiceMessage("Select Publisher",
                model.getBookRequest().getItem().getPublisher(), model.getPublisherRequest().getItemList());

        model.getBookRequest().getItem().setPublisher(publisher);
    }

    public void editBookAuthors() {
        model.getAuthorRequest().loadAll();

        LinkedList<Author> selection = model.getBookRequest().getItem().getAuthors();
        LinkedList<Author> all = model.getAuthorRequest().getItemList();

        for (Author author : selection) {
            all.removeIf(e -> e.getAuthorId() == author.getAuthorId()); //Remove Selection from all
        }

        LinkedList<Author> eingabe = sceneController.editList("Author Selector", selection, all);

        model.getBookRequest().getItem().setAuthors(eingabe);
        displayValues();    //neues Item darstellen
    }

    public void editBookCategories() {
        model.getCategoryRequest().loadAll();

        LinkedList<Category> selection = model.getBookRequest().getItem().getCategories();
        LinkedList<Category> all = model.getCategoryRequest().getItemList();

        for (Category category : selection) {
            all.removeIf(e -> e.getCategoryId() == category.getCategoryId());
        }

        LinkedList<Category> eingabe = sceneController.editList("Category Selector", selection, all);

        model.getBookRequest().getItem().setCategories(eingabe);
        displayValues();    //Neues Item darstellen
    }

    public boolean validateItem() {
        Book item = model.getBookRequest().getItem();
        return item.getTitle() != null && !item.getTitle().isBlank() &&
                item.getIsbn() != null && !item.getIsbn().isBlank() &&
                item.getDescription() != null && !item.getDescription().isBlank() &&
                item.getDescription() != null && !item.getDescription().isBlank() &&
                item.getPages() > 0 && item.getRating() >= 0 &&
                item.getPublisher() != null &&
                item.getAuthors() != null && item.getAuthors().size() != 0 &&
                item.getCategories() != null && item.getCategories().size() != 0;
    }

    @Override
    public void submitChanges() {
        if (!validateItem()) {
            sceneController.errorMessage("Invalid Object", "Please enter valid Values for all Fields!");
            return;
        }

        boolean executed = model.isCreateMode() ? model.getBookRequest().createItem() : model.getBookRequest().editItem();

        if (executed) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");
    }

    @Override
    public void deleteItem() {
        if (!sceneController.confirmMessage("Delete Book", "Do you really want to delete this Book?")) return;

        boolean executed = model.getBookRequest().deleteItem(model.getBookRequest().getItem().getIsbn());

        if (executed) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            returnToMain();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
