package de.medieninformatik.client.controller;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
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
        ObservableList<HBox> list = FXCollections.observableArrayList(buildBookItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<HBox> buildBookItems() {
        LinkedList<HBox> list = new LinkedList<>(); //Baue einen HBox Container pro Buch
        //Liste aus den Daten erstellen
        LinkedList<Category> categories = model.getCategoryRequest().getCategories();
        if (categories == null || categories.size() == 0) return list;
        //List HBox
        for (Category category : categories) {
            HBox hbox = new HBox();
            hbox.setId(String.valueOf(category.getCategoryId()));
            Label name = new Label(category.getName());
            hbox.getChildren().add(name);
            list.add(hbox);
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

    }

    @Override
    public void publisherPressed() {

    }

    @Override
    public void pageForward() {
        if (pageStart + pageSize < model.getCategoryRequest().getMax()) this.pageStart += pageSize;
        //Reload Book List
        loadItemList();
    }

    @Override
    public void inspectItem(String id) {
        model.getCategoryRequest().getCategory(Integer.parseInt(id));
        sceneController.loadCategoryViewScene();
    }

    @Override
    public void deleteItem(String id) {
        if (sceneController.confirmMessage("Delete Category", "Do you really want to delete " + model.getCategoryRequest().getSelection().getName() + " ?")) {
            if (model.getCategoryRequest().deleteCategory(Integer.parseInt(id))) sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
