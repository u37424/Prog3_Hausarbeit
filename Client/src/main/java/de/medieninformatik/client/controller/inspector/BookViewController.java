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

public class BookViewController extends ViewController<Book> {
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
        //Model received
        setOptions();

        this.description.textProperty().addListener((obs) -> {
            model.getBookRequest().getSelection().setDescription(this.description.getText());
        });

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
        this.editAuthors.setVisible(isEdit);
        this.editCategories.setVisible(isEdit);


        //Wenn im Create Modus
        this.editIsbn.setVisible(isCreate);
        this.description.setEditable(isEdit);
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

        setBookRating(book.getRating());

        //Load Publisher into Label
        if (book.getPublisher() != null) this.publisher.setText(book.getPublisher().toString());

        //Build HBox representations of Authors and Categories
        if (book.getAuthors() != null) buildAuthorBox();

        if (book.getCategories() != null) buildCategoryBox();
    }

    private void buildAuthorBox() {
        this.authors.getChildren().clear();
        boolean insertSeparator = true;
        for (Author author : model.getBookRequest().getSelection().getAuthors()) {
            if (insertSeparator) insertSeparator = false;
            else this.authors.getChildren().add(new Separator(Orientation.VERTICAL));

            Label label = new Label(author.toString());
            label.setPadding(new Insets(10));
            this.authors.getChildren().add(label);
        }
    }

    private void buildCategoryBox() {
        this.categories.getChildren().clear();
        boolean insertSeparator = true;
        for (Category category : model.getBookRequest().getSelection().getCategories()) {
            if (insertSeparator) insertSeparator = false;
            else this.categories.getChildren().add(new Separator(Orientation.VERTICAL));

            Label label = new Label(category.toString());
            label.setPadding(new Insets(10));
            this.categories.getChildren().add(label);
        }
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
        String eingabe = sceneController.editMessage("Edit Title", this.title.getText(), "Book Title");
        if (eingabe == null || this.title.getText().equals(eingabe)) return;
        this.title.setText(eingabe);
        model.getBookRequest().getSelection().setTitle(eingabe);
    }

    public void editBookISBN() {
        String eingabe = sceneController.editMessage("Edit ISBN", this.isbn.getText(), "Book ISBN");
        if (eingabe == null || this.isbn.getText().equals(eingabe)) return;
        this.isbn.setText(eingabe);
        model.getBookRequest().getSelection().setIsbn(eingabe);
    }

    public void editBookYear() {
        try {
            int eingabe = Integer.parseInt(sceneController.editMessage("Edit Release Year", this.year.getText(), "Release Year"));
            if (Integer.parseInt(this.year.getText()) == eingabe) return;
            this.year.setText(String.valueOf(eingabe));
            model.getBookRequest().getSelection().setReleaseYear(eingabe);
        } catch (NumberFormatException e) {
            sceneController.errorMessage("Invalid Type", "No valid Number entered.");
        }
    }

    public void editBookPages() {
        try {
            int eingabe = Integer.parseInt(sceneController.editMessage("Edit Pages", this.pages.getText(), "Book Pages"));
            if (Integer.parseInt(this.pages.getText()) == eingabe) return;
            this.pages.setText(String.valueOf(eingabe));
            model.getBookRequest().getSelection().setPages(eingabe);
        } catch (NumberFormatException e) {
            sceneController.errorMessage("Invalid Type", "No valid Number entered.");
        }
    }

    private void setBookRating(double rating) {
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
        model.getPublisherRequest().loadAll();

        Publisher publisher = sceneController.choiceMessage("Select Publisher",
                model.getBookRequest().getSelection().getPublisher(), model.getPublisherRequest().getPublishers());

        model.getBookRequest().getSelection().setPublisher(publisher);

        displayValues(model.getBookRequest().getSelection());
    }

    public void editBookAuthors() {
        model.getAuthorRequest().loadAll();

        LinkedList<Author> sel = model.getBookRequest().getSelection().getAuthors();
        LinkedList<Author> all = model.getAuthorRequest().getAuthors();

        for (Author author : sel) {
            all.removeIf(e -> e.getAuthorId() == author.getAuthorId());
        }
        LinkedList<Author> selection = sceneController.editList("Author Selector", sel, all);

        model.getBookRequest().getSelection().setAuthors(selection);
        displayValues(model.getBookRequest().getSelection());
    }

    public void editBookCategories() {
        model.getCategoryRequest().loadAll();

        LinkedList<Category> sel = model.getBookRequest().getSelection().getCategories();
        LinkedList<Category> all = model.getCategoryRequest().getCategories();

        for (Category category : sel) {
            all.removeIf(e -> e.getCategoryId() == category.getCategoryId());
        }
        LinkedList<Category> selection = sceneController.editList("Category Selector", sel, all);

        model.getBookRequest().getSelection().setCategories(selection);
        displayValues(model.getBookRequest().getSelection());
    }

    public void updateText(){
        model.getBookRequest().getSelection().setDescription(this.description.getText());
    }

    @Override
    public void submitChanges() {
        Book book = model.getBookRequest().getSelection();

        if (book.getTitle() == null || book.getIsbn() == null || book.getReleaseYear() <= 0 ||
                book.getRating() < 0 || book.getPages() <= 0 || book.getDescription() == null ||
                book.getAuthors().size() == 0 || book.getCategories().size() == 0) {
            sceneController.errorMessage("Invalid Object", "Please enter valid Values for all Fields!");
            return;
        }

        boolean res = model.isCreateMode() ? model.getBookRequest().createBook() : model.getBookRequest().editBook();

        if (res) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            super.returnToMain();
            sceneController.loadMainBookScene();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");

    }

    @Override
    public void deleteItem() {
        if (sceneController.confirmMessage("Delete Book", "Do you really want to delete " + model.getBookRequest().getSelection().getIsbn() + " ?")) {
            boolean res = model.getBookRequest().deleteBook(model.getBookRequest().getSelection().getIsbn());
            if (res) {
                sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
                super.returnToMain();
                sceneController.loadMainBookScene();
            } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
