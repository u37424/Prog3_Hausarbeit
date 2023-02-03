package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.controller.SceneController;
import de.medieninformatik.client.interfaces.IViewController;
import de.medieninformatik.client.model.MainModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public abstract class ViewController implements IViewController {
    @FXML
    protected Button submitButton, deleteButton, backButton;

    protected Stage stage;
    protected MainModel model;
    protected SceneController sceneController;

    @Override
    public void initialize() {
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setModel(MainModel model) {
        this.model = model;
        setOptions();
    }

    @Override
    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @Override
    public void setOptions() {
        if (!model.isMainUser()) return;
        boolean isEdit = model.isEditMode();
        boolean isCreate = model.isCreateMode();

        //Submit, Back, Delete
        this.submitButton.setVisible(isEdit);
        this.deleteButton.setVisible(isEdit && !isCreate);
        ((ImageView) this.backButton.getGraphic()).setImage(new Image((isEdit) ? "exit.png" : "return.png"));
    }

    @Override
    public abstract void displayValues();

    @Override
    public void returnToMain() {
        model.setCreateMode(false);
    }

    @Override
    public abstract boolean validateItem();

    @Override
    public abstract void submitChanges();

    @Override
    public abstract void deleteItem();
}
