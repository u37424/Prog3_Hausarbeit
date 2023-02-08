package de.medieninformatik.client.controller.main;

import de.medieninformatik.common.Author;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
 * Diese Klasse implementiert einen spezifischen MainController fuer den Autor-Ansichtsmodus.
 * Der Controller nimmt alle Einstellungen vor, um die Ansicht an die Autor-Ansicht anzupassen.
 * Der Controller implementiert alle spezifischen Aufgaben, um mit Autoren umzugehen.
 * </p>
 */
public class MainAuthorController extends MainController {

    /**
     * Wird beim Laden der FXML Datei aufgerufen, um initiale Einstellungen beim Laden des Controllers zu machen.
     */
    @FXML
    @Override
    public void initialize() {
        super.initialize();
        authorButton.setStyle(authorButton.getStyle() + "-fx-background-color: #5dc367;");
    }

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.setTitle("Author List");
    }

    /**
     * Laedt die aktuellen Elemente der darzustellenden Liste und zeigt sie an.
     * Die Liste kann auch nur einen gewissen Ausschnitt einer groesseren Liste darstellen.
     */
    @Override
    public void loadItemList() {
        //Aktuelle Daten laden
        model.getAuthorModel().loadSelection(pageStart, pageSize, ascending, userString);
        //Liste aus den Daten erstellen
        ObservableList<GridPane> list = FXCollections.observableArrayList(buildAuthorItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    /**
     * Baut die individuellen Elemente, deren Daten in der Liste dargestellt werden sollen.
     *
     * @return LinkedList von einzeiligen Darstellungen der Listenelemente
     */
    private LinkedList<GridPane> buildAuthorItems() {
        LinkedList<GridPane> list = new LinkedList<>();

        LinkedList<Author> authors = model.getAuthorModel().getItemList();
        if (authors == null || authors.size() == 0) return list;
        //HBox pro Autor aus Daten bauen
        for (Author author : authors) {
            GridPane pane = listPaneBuilder(3, String.valueOf(author.getAuthorId()));
            TextFlow flow = buildFrontItem(author.getFirstName() + " " + author.getLastName());
            pane.addColumn(0, flow);

            Label alias = new Label(author.getAlias());
            pane.addColumn(1, alias);

            Label birthday = new Label(author.getBirthday().substring(0, 4));
            pane.addColumn(2, birthday);

            //Edit Buttons wenn im Edit Modus
            if (model.isMainUser()) enableEditorButtons(pane);
            list.add(pane);
        }
        return list;
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer einen neuen Eintrag erstellen will.
     */
    @Override
    public void createItem() {
        super.createItem();
        model.getAuthorModel().clear();
        sceneController.loadAuthorViewScene();
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Buch" Schaltflaeche benutzt.
     */
    @Override
    public void bookPressed() {
        sceneController.loadMainBookScene();
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
        createItem();
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
        if (pageStart + pageSize < model.getAuthorModel().getMax()) this.pageStart += pageSize;
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
            model.getAuthorModel().loadItem(id);
            sceneController.loadAuthorViewScene();
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
        if (!sceneController.confirmMessage("Delete Author", "Do you really want to delete this item ?")) return;

        if (model.getAuthorModel().deleteItem(id)) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            loadItemList();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
