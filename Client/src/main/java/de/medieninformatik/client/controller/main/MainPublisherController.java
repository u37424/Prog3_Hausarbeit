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
        ObservableList<GridPane> list = FXCollections.observableArrayList(buildPublisherItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<GridPane> buildPublisherItems() {
        LinkedList<GridPane> list = new LinkedList<>();

        LinkedList<Publisher> publishers = model.getPublisherRequest().getItemList();
        if (publishers == null || publishers.size() == 0) return list;
        //HBox pro Publisher aus Daten bauen
        for (Publisher publisher : publishers) {
            GridPane pane = listPaneBuilder(2, String.valueOf(publisher.getPublisherId()));
            TextFlow flow = buildFrontItem(publisher.getName());
            pane.addColumn(0, flow);

            Label country = new Label(publisher.getCountry());
            pane.addColumn(1, country);

            //Edit Buttons wenn im Edit Modus
            if (model.isMainUser()) enableEditorButtons(2, pane);
            list.add(pane);
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
        loadItemList();
    }

    @Override
    public void inspectItem(String id) {
        try {
            model.getPublisherRequest().loadItem(id);
            sceneController.loadPublisherViewScene();
        } catch (NumberFormatException e) {
            sceneController.errorMessage("Parsing Error", "Item ID cannot be read.");
        }
    }

    @Override
    public void deleteItem(String id) {
        if (!sceneController.confirmMessage("Delete Publisher", "Do you really want to delete this item?")) return;

        if (model.getPublisherRequest().deleteItem(id)) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            loadItemList();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
