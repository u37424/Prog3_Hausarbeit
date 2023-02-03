package de.medieninformatik.client.controller.main;

import de.medieninformatik.common.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
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
        LinkedList<GridPane> list = new LinkedList<>();

        LinkedList<Category> categories = model.getCategoryRequest().getItemList();
        if (categories == null || categories.size() == 0) return list;
        //HBox pro Kategorie aus Daten bauen
        for (Category category : categories) {
            GridPane pane = listPaneBuilder(1, String.valueOf(category.getCategoryId()));
            TextFlow flow = buildFrontItem(category.getName());
            pane.addColumn(0, flow);

            //Edit Buttons wenn im Edit Modus
            if (model.isMainUser()) enableEditorButtons(1, pane);
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
        loadItemList();
    }

    @Override
    public void inspectItem(String id) {
        try {
            model.getCategoryRequest().loadItem(id);
            sceneController.loadCategoryViewScene();
        } catch (NumberFormatException e) {
            sceneController.errorMessage("Parsing Error", "Item ID cannot be read.");
        }
    }

    @Override
    public void deleteItem(String id) {
        if (!sceneController.confirmMessage("Delete Category", "Do you really want to delete this Category?")) return;

        if (model.getCategoryRequest().deleteItem(id)) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            loadItemList();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
