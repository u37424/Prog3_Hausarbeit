package de.medieninformatik.client.controller.inspector;

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

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse implementiert einen konkreten ViewController fuer Buecher.
 * Dieser Controller implementiert alle spezifischen Funktionen, die fuer die Arbeit mit einer Buch-Ansicht gebraucht werden.
 * </p>
 */
public class BookViewController extends ViewController {
    @FXML
    private Label title, isbn, pages, year, rating, publisher;

    @FXML
    private HBox authors, categories, ratingBox;

    @FXML
    private TextArea description;

    @FXML
    private Button editIsbn, editYear, editPages, editTitle, editPublisher, editAuthors, editCategories;

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    public void setStage(Stage stage) {
        super.setStage(stage);
        this.stage.setTitle("Book Inspector");
    }


    /**
     * Setzt die Optionen, die der Benutzer entsprechend zu seinem Benutzerstatus im Betrachtungsfenster haben soll.
     */
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
        this.description.setDisable(!isEdit);
        if (isEdit)
            this.description.textProperty().addListener((obs) -> model.getBookModel().getItem().setDescription(this.description.getText()));

        //Wenn im Create Modus
        this.editIsbn.setVisible(isCreate);
        if (isCreate)
            ratingBox.getChildren().forEach(star -> star.setOnMouseClicked(e -> editBookRating(star.getId())));  //Rating durch Klicks auf Sterne
        if (isCreate) this.description.setText("");
    }

    /**
     * Setzt die eingestellten Default-Werte auf die erhaltenden Werte des zu betrachtenden Elementes.
     */
    @Override
    public void displayValues() {
        Book book = model.getBookModel().getItem();
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

    /**
     * Stellt eine gegebene Bewertung als Sterne-Bewertung dar.
     *
     * @param rating darzustellende Bewertung
     */
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

    /**
     * Stellt alle zum Buch gehoerigen Autoren dar.
     * Die Autoren werden durch Striche separiert.
     */
    private void buildAuthorBox() {
        this.authors.getChildren().clear();
        boolean insertSeparator = true; //Um ersten Trennstrich abzuwarten

        for (Author author : model.getBookModel().getItem().getAuthors()) {
            if (insertSeparator) insertSeparator = false;
            else this.authors.getChildren().add(new Separator(Orientation.VERTICAL));

            Label label = new Label(author.toString());
            label.setPadding(new Insets(10));
            this.authors.getChildren().add(label);
        }
    }

    /**
     * Stellt alle zum Buch gehoerigen Kategorien dar.
     * Die Kategorien werden durch Striche separiert.
     */
    private void buildCategoryBox() {
        this.categories.getChildren().clear();
        boolean insertSeparator = true; //Um ersten Trennstrich abzuwarten

        for (Category category : model.getBookModel().getItem().getCategories()) {
            if (insertSeparator) insertSeparator = false;
            else this.categories.getChildren().add(new Separator(Orientation.VERTICAL));

            Label label = new Label(category.toString());
            label.setPadding(new Insets(10));
            this.categories.getChildren().add(label);
        }
    }

    /**
     * Leitet alle notwendigen Aktionen ein, um den Benutzer in den entsprechenden Main Modus zu bringen.
     */
    @Override
    public void returnToMain() {
        super.returnToMain();
        sceneController.loadMainBookScene();
    }

    //--------EDIT VALUES

    /**
     * Setzt den aktuell eingegebenen Titel als Titel des Buches.
     */
    public void editBookTitle() {
        String eingabe = sceneController.editStringMessage("Edit Title", this.title.getText(), "Book Title");
        if (eingabe == null) return;
        this.title.setText(eingabe);
        model.getBookModel().getItem().setTitle(eingabe);
    }

    /**
     * Setzt die aktuell eingegebene ISBN als ISBN des Buches.
     */
    public void editBookISBN() {
        String eingabe = sceneController.editStringMessage("Edit ISBN", this.isbn.getText(), "Book ISBN");
        if (eingabe == null) return;
        this.isbn.setText(eingabe);
        model.getBookModel().getItem().setIsbn(eingabe);
    }

    /**
     * Setzt das aktuell eingegebene Erscheinungsjahr als Erscheinungsjahr des Buches.
     */
    public void editBookYear() {
        int eingabe = sceneController.editNumberMessage("Edit Release Year", this.year.getText(), "Release Year");
        this.year.setText(String.valueOf(eingabe));
        model.getBookModel().getItem().setReleaseYear(eingabe);
    }

    /**
     * Setzt die aktuell eingegebene Seitenzahl als Seitenzahl des Buches.
     */
    public void editBookPages() {
        int eingabe = sceneController.editNumberMessage("Edit Pages", this.pages.getText(), "Pages");
        this.pages.setText(String.valueOf(eingabe));
        model.getBookModel().getItem().setPages(eingabe);
    }

    /**
     * Setzt die aktuell eingegebene Bewertung als Bewertung des Buches.
     */
    public void editBookRating(String id) {
        String image = "star_filled.png";

        int val = 0;
        for (Node n : ratingBox.getChildren()) {
            val++;
            ((ImageView) n).setImage(new Image(image));
            if (id.equals(n.getId())) {
                image = "star_empty.png";
                model.getBookModel().getItem().setRating(val);
            }
        }
    }

    /**
     * Setzt den aktuell eingegebenen Verlag als Verlag des Buches.
     */
    public void editBookPublisher() {
        model.getPublisherModel().loadAll();

        Publisher publisher = sceneController.choiceMessage("Select Publisher",
                model.getBookModel().getItem().getPublisher(), model.getPublisherModel().getItemList());

        model.getBookModel().getItem().setPublisher(publisher);
        displayValues();
    }

    /**
     * Setzt die aktuell eingegebenen Autoren als Autoren des Buches.
     */
    public void editBookAuthors() {
        model.getAuthorModel().loadAll();

        LinkedList<Author> selection = model.getBookModel().getItem().getAuthors();
        LinkedList<Author> all = model.getAuthorModel().getItemList();

        for (Author author : selection) all.removeIf(e -> e.getAuthorId() == author.getAuthorId());

        LinkedList<Author> eingabe = sceneController.editList("Author Selector", selection, all);

        model.getBookModel().getItem().setAuthors(eingabe);
        displayValues();    //neues Item darstellen
    }

    /**
     * Setzt die aktuell eingegebenen Kategorien als Kategorien des Buches.
     */
    public void editBookCategories() {
        model.getCategoryModel().loadAll();

        LinkedList<Category> selection = model.getBookModel().getItem().getCategories();
        LinkedList<Category> all = model.getCategoryModel().getItemList();

        for (Category category : selection) all.removeIf(e -> e.getCategoryId() == category.getCategoryId());

        LinkedList<Category> eingabe = sceneController.editList("Category Selector", selection, all);

        model.getBookModel().getItem().setCategories(eingabe);
        displayValues();    //Neues Item darstellen
    }

    //----ENDE EDIT VALUES

    /**
     * Prueft alle veraenderbaren Werte des dargestellten Objektes auf Korrektheit.
     *
     * @return true, wenn alle Werte den Vorgaben entsprechend vorhanden sind
     */
    public boolean validateItem() {
        Book item = model.getBookModel().getItem();
        return item.getTitle() != null && !item.getTitle().isBlank() &&
                item.getIsbn() != null && !item.getIsbn().isBlank() &&
                item.getDescription() != null && !item.getDescription().isBlank() &&
                item.getDescription() != null && !item.getDescription().isBlank() &&
                item.getPages() > 0 && item.getRating() >= 0 &&
                item.getPublisher() != null &&
                item.getAuthors() != null && item.getAuthors().size() != 0 &&
                item.getCategories() != null && item.getCategories().size() != 0;
    }

    /**
     * Ermoeglicht es dem Hauptbenutzer den aktuellen Eintrag mit den aktuellen Werten zu speichern.
     */
    @Override
    public void submitChanges() {
        if (!validateItem()) {
            sceneController.errorMessage("Invalid Object", "Please enter valid Values for all Fields!");
            return;
        }

        //Ausfuehrung je nach aktuellem Modus (Erstellen / Bearbeiten)
        boolean executed = model.isCreateMode() ? model.getBookModel().createItem() : model.getBookModel().editItem();

        if (executed) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");
    }

    /**
     * Ermoeglicht es dem Hauptbenutzer den aktuellen Eintrag mit den aktuellen Werten zu loeschen.
     */
    @Override
    public void deleteItem() {
        if (!sceneController.confirmMessage("Delete Book", "Do you really want to delete this Book?")) return;

        boolean executed = model.getBookModel().deleteItem(model.getBookModel().getItem().getIsbn());

        if (executed) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            returnToMain();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
