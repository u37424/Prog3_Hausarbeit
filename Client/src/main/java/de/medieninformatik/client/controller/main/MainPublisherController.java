package de.medieninformatik.client.controller.main;

import de.medieninformatik.common.Publisher;
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
 * Diese Klasse implementiert einen spezifischen MainController fuer den Publisher-Ansichtsmodus.
 * Der Controller nimmt alle Einstellungen vor, um die Ansicht an die Publisher-Ansicht anzupassen.
 * Der Controller implementiert alle spezifischen Aufgaben, um mit Publishern umzugehen.
 * </p>
 */
public class MainPublisherController extends MainController {

    /**
     * Wird beim Laden der FXML Datei aufgerufen, um initiale Einstellungen beim Laden des Controllers zu machen.
     */
    @FXML
    @Override
    public void initialize() {
        super.initialize();
        publisherButton.setStyle(publisherButton.getStyle() + "-fx-background-color: #5dc367;");
    }

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.setTitle("Publisher List");
    }

    /**
     * Laedt die aktuellen Elemente der darzustellenden Liste und zeigt sie an.
     * Die Liste kann auch nur einen gewissen Ausschnitt einer groesseren Liste darstellen.
     */
    @Override
    public void loadItemList() {
        //Aktuelle Daten laden
        model.getPublisherModel().loadSelection(pageStart, pageSize, ascending, userString);
        //Liste aus den Daten erstellen
        ObservableList<GridPane> list = FXCollections.observableArrayList(buildPublisherItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    /**
     * Baut die individuellen Elemente, deren Daten in der Liste dargestellt werden sollen.
     *
     * @return LinkedList von einzeiligen Darstellungen der Listenelemente
     */
    private LinkedList<GridPane> buildPublisherItems() {
        LinkedList<GridPane> list = new LinkedList<>();

        LinkedList<Publisher> publishers = model.getPublisherModel().getItemList();
        if (publishers == null || publishers.size() == 0) return list;
        //HBox pro Publisher aus Daten bauen
        for (Publisher publisher : publishers) {
            GridPane pane = listPaneBuilder(2, String.valueOf(publisher.getPublisherId()));
            TextFlow flow = buildFrontItem(publisher.getName());
            pane.addColumn(0, flow);

            Label country = new Label(publisher.getCountry());
            pane.addColumn(1, country);

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
        model.getPublisherModel().clear();
        sceneController.loadPublisherViewScene();
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
        sceneController.loadMainAuthorScene();
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Publisher" Schaltflaeche benutzt.
     */
    @Override
    public void publisherPressed() {
        createItem();
    }

    /**
     * Setzt einen neuen Ansichtsbereich der Liste, der hinter dem aktuellen Ansichtsbereich liegt.
     */
    @Override
    public void pageForward() {
        if (pageStart + pageSize < model.getPublisherModel().getMax()) this.pageStart += pageSize;
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
            model.getPublisherModel().loadItem(id);
            sceneController.loadPublisherViewScene();
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
        if (!sceneController.confirmMessage("Delete Publisher", "Do you really want to delete this item?")) return;

        if (model.getPublisherModel().deleteItem(id)) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            loadItemList();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
