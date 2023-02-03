package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Category;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CategoryViewController extends ViewController {
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
        displayValues();
    }

    @Override
    public void setOptions() {
        if (!model.isMainUser()) return;
        super.setOptions();
        boolean isEdit = model.isEditMode();

        //Edit Buttons
        this.editName.setVisible(isEdit);
    }

    @Override
    public void displayValues() {
        Category category = model.getCategoryRequest().getItem();
        if (category == null) return;

        this.name.setText(category.getName());
    }

    @Override
    public void returnToMain() {
        super.returnToMain();
        sceneController.loadMainCategoryScene();
    }

    //--------EDIT VALUES

    public void editCategoryName() {
        String eingabe = sceneController.editStringMessage("Edit Name", this.name.getText(), "Category Name");
        if (eingabe == null) return;
        this.name.setText(eingabe);
        model.getCategoryRequest().getItem().setName(eingabe);
    }

    @Override
    public boolean validateItem() {
        Category item = model.getCategoryRequest().getItem();
        return item.getName() != null && !item.getName().isBlank();
    }

    @Override
    public void submitChanges() {
        if (!validateItem()) {
            sceneController.errorMessage("Invalid Object", "Please enter valid Values for all Fields!");
            return;
        }

        boolean executed = model.isCreateMode() ? model.getCategoryRequest().createItem() : model.getCategoryRequest().editItem();

        if (executed) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");
    }

    @Override
    public void deleteItem() {
        if (!sceneController.confirmMessage("Delete Category", "Do you really want to delete this Category?")) return;

        int id = model.getCategoryRequest().getItem().getCategoryId();
        boolean executed = model.getCategoryRequest().deleteItem(String.valueOf(id));

        if (executed) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            returnToMain();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
