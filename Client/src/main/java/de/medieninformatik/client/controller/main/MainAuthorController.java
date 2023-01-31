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

public class MainAuthorController extends MainController {
    @FXML
    @Override
    public void initialize() {
        super.initialize();
        authorButton.setStyle(authorButton.getStyle() + "-fx-background-color: #5dc367;");
    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.setTitle("Author List");
    }

    @Override
    public void loadItemList() {
        //Aktuelle Daten laden
        model.getAuthorRequest().loadSelection(pageStart, pageSize, ascending, userString);
        //Liste aus den Daten erstellen
        ObservableList<GridPane> list = FXCollections.observableArrayList(buildAuthorItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<GridPane> buildAuthorItems() {
        LinkedList<GridPane> list = new LinkedList<>(); //Baue einen HBox Container pro Buch
        //Liste aus den Daten erstellen
        LinkedList<Author> authors = model.getAuthorRequest().getAuthors();
        if (authors == null || authors.size() == 0) return list;
        //List HBox
        for (Author author : authors) {
            GridPane pane = paneBuilder(3, String.valueOf(author.getAuthorId()));
            TextFlow flow = buildFrontItem(author.getFirstName() + " " + author.getLastName());
            Label alias = new Label(author.getAlias());
            Label birthday = new Label(author.getBirthday().substring(0, 4));
            pane.addColumn(0, flow);
            pane.addColumn(1, alias);
            pane.addColumn(2, birthday);
            if (model.isMainUser()) addButtons(3, pane);
            list.add(pane);
        }
        return list;
    }

    @Override
    public void createItem() {
        super.createItem();
        model.getAuthorRequest().reset();
        sceneController.loadAuthorViewScene();
    }

    @Override
    public void bookPressed() {
        sceneController.loadMainBookScene();
    }

    @Override
    public void categoryPressed() {
        sceneController.loadMainCategoryScene();
    }

    @Override
    public void authorPressed() {
        createItem();
    }

    @Override
    public void publisherPressed() {
        sceneController.loadMainPublisherScene();
    }

    @Override
    public void pageForward() {
        if (pageStart + pageSize < model.getAuthorRequest().getMax()) this.pageStart += pageSize;
        //Reload List
        loadItemList();
    }

    @Override
    public void inspectItem(String id) {
        try {
            model.getAuthorRequest().getAuthor(Integer.parseInt(id));
            sceneController.loadAuthorViewScene();
        } catch (NumberFormatException e) {
            sceneController.errorMessage("Parsing Error", "Item ID cannot be read.");
        }
    }

    @Override
    public void deleteItem(String id) {
        if (sceneController.confirmMessage("Delete Author", "Do you really want to delete this item ?")) {
            if (model.getAuthorRequest().deleteAuthor(Integer.parseInt(id))) {
                sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
                loadItemList();
            } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}