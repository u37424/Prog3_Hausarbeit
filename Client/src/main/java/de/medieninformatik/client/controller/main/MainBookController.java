package de.medieninformatik.client.controller.main;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;
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
 * Diese Klasse implementiert einen spezifischen MainController fuer den Buch-Ansichtsmodus.
 * Der Controller nimmt alle Einstellungen vor, um die Ansicht an die Buch-Ansicht anzupassen.
 * Der Controller implementiert alle spezifischen Aufgaben, um mit Buechern umzugehen.
 * </p>
 */
public class MainBookController extends MainController {

    /**
     * Wird beim Laden der FXML Datei aufgerufen, um initiale Einstellungen beim Laden des Controllers zu machen.
     */
    @FXML
    @Override
    public void initialize() {
        super.initialize();
        bookButton.setStyle(bookButton.getStyle() + "-fx-background-color: #5dc367;");
        //Default Category Auswahl
        selector.getItems().add("");
        this.selector.setPrefWidth(150);
        this.selector.setVisible(true);
    }

    /**
     * Setzt das entsprechende Model, welches vom Controller verwendet werden soll.
     *
     * @param model Model, welches verwendet werden soll
     */
    public void setModel(MainModel model) {
        super.setModel(model);
        //kategorie Filter laden, nachdem Model geladen
        loadCategorySelection();
    }

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.setTitle("Book List");
    }

    /**
     * Laedt alle vorhandenen Kategorien in die Choice Box fuer den Kategorie-Filter.
     */
    private void loadCategorySelection() {
        model.getCategoryModel().loadAll();
        if (model.getCategoryModel().getItemList() == null ||
                model.getCategoryModel().getItemList().size() == 0)
            return;

        for (Category category : model.getCategoryModel().getItemList())
            this.selector.getItems().add(category.getName());
    }

    /**
     * Laedt die aktuellen Elemente der darzustellenden Liste und zeigt sie an.
     * Die Liste kann auch nur einen gewissen Ausschnitt einer groesseren Liste darstellen.
     */
    @Override
    public void loadItemList() {
        //Aktuelle Daten laden
        model.getBookModel().loadSelection(pageStart, pageSize, ascending, userString, userSelection);
        //Liste aus den Daten erstellen
        ObservableList<GridPane> list = FXCollections.observableArrayList(buildBookItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    /**
     * Baut die individuellen Elemente, deren Daten in der Liste dargestellt werden sollen.
     *
     * @return LinkedList von einzeiligen Darstellungen der Listenelemente
     */
    private LinkedList<GridPane> buildBookItems() {
        LinkedList<GridPane> list = new LinkedList<>();

        LinkedList<Book> books = model.getBookModel().getItemList();
        if (books == null || books.size() == 0) return list;

        //HBox pro Buch aus Daten bauen
        for (Book book : books) {
            GridPane pane = listPaneBuilder(5, book.getIsbn());

            TextFlow flow = buildFrontItem(book.getTitle());    //Front Item
            pane.addColumn(0, flow);

            Label year = new Label(String.valueOf(book.getReleaseYear()));  //Jahr
            pane.addColumn(1, year);

            //Autoren (nur erster Autor dargestellt)
            if (book.getAuthors() != null && book.getAuthors().size() != 0) {
                Label author = new Label(book.getAuthors().get(0).getAlias() + ((book.getAuthors().size() > 1) ? ",..." : ""));  //,... bei mehreren Autoren
                pane.addColumn(2, author);
            }

            //Publisher
            if (book.getPublisher() != null) {
                Label publisher = new Label(book.getPublisher().toString());
                pane.addColumn(3, publisher);
            }

            //Rating als Sterne
            HBox rating = buildRatingBox((int) book.getRating());
            rating.setAlignment(Pos.CENTER);
            pane.addColumn(4, rating);

            //Edit Buttons wenn im Edit Modus
            if (model.isMainUser()) enableEditorButtons(pane);
            list.add(pane);
        }
        return list;
    }

    /**
     * Baut eine HBox mit Sternen, die die Bewertung eines Buches darstellt.
     *
     * @param rating darzustellende Bewertung des Buches
     * @return HBox mit entsprechend gefaerbten Sternen
     */
    private HBox buildRatingBox(int rating) {
        HBox container = new HBox();
        container.setPadding(new Insets(15));
        for (int i = 0; i < 5; i++) {
            ImageView imageView = new ImageView(new Image(rating > i ? "star_filled.png" : "star_empty.png"));
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            container.getChildren().add(imageView);
        }
        return container;
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer einen neuen Eintrag erstellen will.
     */
    @Override
    public void createItem() {
        super.createItem();
        model.getBookModel().clear();
        sceneController.loadBookViewScene();
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Buch" Schaltflaeche benutzt.
     */
    @Override
    public void bookPressed() {
        createItem();
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Category" Schaltflaeche benutzt.
     */
    @Override
    public void categoryPressed() {
        sceneController.loadMainCategoryScene();
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Author" Schaltflaeche benutzt.
     */
    @Override
    public void authorPressed() {
        sceneController.loadMainAuthorScene();
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Publisher" Schaltflaeche benutzt.
     */
    @Override
    public void publisherPressed() {
        sceneController.loadMainPublisherScene();
    }

    /**
     * Setzt einen neuen Ansichtsbereich der Liste, der hinter dem aktuellen Ansichtsbereich liegt.
     */
    @Override
    public void pageForward() {
        if (pageStart + pageSize < model.getBookModel().getMax()) this.pageStart += pageSize;
        loadItemList();
    }

    /**
     * Leitet alle notwendigen Aktionen ein, um ein spezifisches Element aus der Liste zu betrachten.
     *
     * @param id ID des Elementes, welches betrachtet werden soll.
     */
    @Override
    public void inspectItem(String id) {
        try {
            model.getBookModel().loadItem(id);
            sceneController.loadBookViewScene();
        } catch (NumberFormatException e) {
            sceneController.errorMessage("Parsing Error", "Item ID cannot be read.");
        }
    }

    /**
     * Leitet alle notwendigen Aktionen ein, um ein spezifisches Element aus der Liste zu loeschen.
     *
     * @param id ID des Elementes, welches geloescht werden soll.
     */
    @Override
    public void deleteItem(String id) {
        if (!sceneController.confirmMessage("Delete Book", "Do you really want to delete this item?")) return;

        if (model.getBookModel().deleteItem(id)) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            loadItemList();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
