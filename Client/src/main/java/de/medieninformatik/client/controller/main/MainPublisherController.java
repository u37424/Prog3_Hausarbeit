package de.medieninformatik.client.controller.main;

import de.medieninformatik.common.Publisher;
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
        publisherButton.setStyle(publisherButton.getStyle() + "-fx-background-color: #5dc367;");
    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.setTitle("Publisher List");
    }

    @Override
    public void loadItemList() {
        //Aktuelle Daten laden
        model.getPublisherRequest().loadSelection(pageStart, pageSize, ascending, userString);
        //Liste aus den Daten erstellen
        ObservableList<HBox> list = FXCollections.observableArrayList(buildPublisherItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<HBox> buildPublisherItems() {
        LinkedList<HBox> list = new LinkedList<>(); //Baue einen HBox Container pro Buch
        //Liste aus den Daten erstellen
        LinkedList<Publisher> publishers = model.getPublisherRequest().getPublishers();
        if (publishers == null || publishers.size() == 0) return list;
        //List HBox
        for (Publisher publisher : publishers) {
            HBox hbox = new HBox();
            hbox.setId(String.valueOf(publisher.getPublisherId()));
            Label name = new Label(publisher.getName());
            hbox.getChildren().add(name);
            list.add(hbox);
        }
        return list;
    }

    @Override
    public void createItem() {
        super.createItem();
        model.getPublisherRequest().reset();
        sceneController.loadPublisherViewScene();
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
      sceneController.loadMainAuthorScene();
    }

    @Override
    public void publisherPressed() {
        createItem();
    }

    @Override
    public void pageForward() {
        if (pageStart + pageSize < model.getPublisherRequest().getMax()) this.pageStart += pageSize;
        //Reload List
        loadItemList();
    }

    @Override
    public void inspectItem(String id) {
        model.getPublisherRequest().getPublisher(Integer.parseInt(id));
        sceneController.loadPublisherViewScene();
    }

    @Override
    public void deleteItem(String id) {
        if (sceneController.confirmMessage("Delete Publisher", "Do you really want to delete " + model.getPublisherRequest().getSelection().getName() + " ?")) {
            if (model.getPublisherRequest().deletePublisher(Integer.parseInt(id))) sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
