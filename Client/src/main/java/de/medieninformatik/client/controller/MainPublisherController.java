package de.medieninformatik.client.controller;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Author;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.LinkedList;

public class MainPublisherController extends MainController {
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

    public void setModel(MainModel model) {
        super.setModel(model);
    }

    @Override
    public void loadItemList() {
        //Aktuelle Daten laden
        model.getAuthorRequest().loadSelection(pageStart, pageSize, ascending, userString);
        //Liste aus den Daten erstellen
        ObservableList<HBox> list = FXCollections.observableArrayList(buildAuthorItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<HBox> buildAuthorItems() {
        LinkedList<HBox> list = new LinkedList<>(); //Baue einen HBox Container pro Buch
        //Liste aus den Daten erstellen
        LinkedList<Author> authors = model.getAuthorRequest().getAuthors();
        if (authors == null || authors.size() == 0) return list;
        //List HBox
        for (Author author : authors) {
            HBox hbox = new HBox();
            hbox.setId(String.valueOf(author.getAuthorId()));
            Label alias = new Label(author.getAlias());
            hbox.getChildren().add(alias);
            list.add(hbox);
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
        model.getAuthorRequest().getAuthor(Integer.parseInt(id));
        sceneController.loadAuthorViewScene();
    }

    @Override
    public void deleteItem(String id) {
        if (sceneController.confirmMessage("Delete Author", "Do you really want to delete " + model.getAuthorRequest().getSelection().getAlias() + " ?")) {
            if (model.getAuthorRequest().deleteAuthor(Integer.parseInt(id))) sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
