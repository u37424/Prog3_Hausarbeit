package de.medieninformatik.client.controller;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CategoryViewController extends ViewController<Category> {
    @FXML
    private Label name;

    @FXML
    private Button editName;

    public void setStage(Stage stage) {
        super.setStage(stage);
        this.stage.setTitle("Category Inspector");
    }

    public void setModel(MainModel model) {
        super.setModel(model);
        //Model received
        setOptions();

        //Load Book when Model received
        Category category = model.getCategoryRequest().getSelection();
        displayValues(category);
    }

    @Override
    public void setOptions() {
        if (!model.isMainUser()) return;
        super.setOptions();

        boolean isEdit = model.isEditMode();
        boolean isCreate = model.isCreateMode();

        //Specific edit Buttons
        this.editName.setVisible(isEdit);
    }

    @Override
    public void displayValues(Category category) {
        if (category == null) return;
        //Fill with Book Data
        if (category.getName() != null && !category.getName().isBlank())
            this.name.setText(category.getName());
    }

    @Override
    public void returnToMain() {
        if (model.isCreateMode() || model.isEditMode()) {
            if (!sceneController.confirmMessage("Leave Editing", "Unsaved Changes will be lost!")) return;
        }
        super.returnToMain();

        sceneController.loadMainCategoryScene();
    }

    //--------EDIT VALUES

    public void editCategoryName() {

    }

    @Override
    public void submitChanges() {
        boolean res = model.isCreateMode() ? model.getCategoryRequest().createCategory() : model.getCategoryRequest().editCategory();
        if (res) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");

    }

    @Override
    public void deleteItem() {
        if (sceneController.confirmMessage("Delete Category", "Do you really want to delete " + model.getCategoryRequest().getSelection().getName() + " ?")) {
            boolean res = model.getBookRequest().deleteBook(model.getBookRequest().getSelection().getIsbn());
            if (res) sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
