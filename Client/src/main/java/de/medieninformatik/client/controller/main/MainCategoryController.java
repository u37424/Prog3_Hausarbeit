package de.medieninformatik.client.controller.main;

import de.medieninformatik.common.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.util.LinkedList;

public class MainCategoryController extends MainController {
    @FXML
    @Override
    public void initialize() {
        super.initialize();
        categoryButton.setStyle(categoryButton.getStyle() + "-fx-background-color: #5dc367;");
    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.setTitle("Category List");
    }

    @Override
    public void loadItemList() {
        //Aktuelle Daten laden
        model.getCategoryRequest().loadSelection(pageStart, pageSize, ascending, userString);
        //Liste aus den Daten erstellen
        ObservableList<GridPane> list = FXCollections.observableArrayList(buildCategoryItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<GridPane> buildCategoryItems() {
        LinkedList<GridPane> list = new LinkedList<>(); //Baue einen HBox Container pro Buch
        //Liste aus den Daten erstellen
        LinkedList<Category> categories = model.getCategoryRequest().getCategories();
        if (categories == null || categories.size() == 0) return list;
        //List HBox
        for (Category category : categories) {
            GridPane pane = paneBuilder();
            Label label1 = new Label("label");
            Label label2 = new Label("label");
            Label label3 = new Label("label");
            Label label4 = new Label("label");
            pane.addColumn(0, label1);
            pane.addColumn(1, label2);
            pane.addColumn(2, label3);
            pane.addColumn(3, label4);
            list.add(pane);
        }
        return list;
    }

    @Override
    public void createItem() {
        super.createItem();
        model.getCategoryRequest().reset();
        sceneController.loadCategoryViewScene();
    }

    @Override
    public void bookPressed() {
        sceneController.loadMainBookScene();
    }

    @Override
    public void categoryPressed() {
        createItem();
    }

    @Override
    public void authorPressed() {
        sceneController.loadMainAuthorScene();
    }

    @Override
    public void publisherPressed() {
        sceneController.loadMainPublisherScene();
    }

    @Override
    public void pageForward() {
        if (pageStart + pageSize < model.getCategoryRequest().getMax()) this.pageStart += pageSize;
        //Reload List
        loadItemList();
    }

    @Override
    public void inspectItem(String id) {
        try {
            model.getCategoryRequest().getCategory(Integer.parseInt(id));
            sceneController.loadCategoryViewScene();
        } catch (NumberFormatException e) {
            sceneController.errorMessage("Parsing Error", "Item ID cannot be read.");
        }
    }

    @Override
    public void deleteItem(String id) {
        if (sceneController.confirmMessage("Delete Category", "Do you really want to delete " + model.getCategoryRequest().getSelection().getName() + " ?")) {
            if (model.getCategoryRequest().deleteCategory(Integer.parseInt(id)))
                sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
