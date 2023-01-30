package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Publisher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PublisherViewController extends ViewController<Publisher> {
    @FXML
    private Label name, country, year;

    @FXML
    private Button editName, editCountry, editYear;

    public void setStage(Stage stage) {
        super.setStage(stage);
        this.stage.setTitle("Publisher Inspector");
    }

    public void setModel(MainModel model) {
        super.setModel(model);
        //Model received
        setOptions();

        //Load Book when Model received
        Publisher publisher = model.getPublisherRequest().getSelection();
        displayValues(publisher);
    }

    @Override
    public void setOptions() {
        if (!model.isMainUser()) return;
        super.setOptions();

        boolean isEdit = model.isEditMode();
        boolean isCreate = model.isCreateMode();

        //Specific edit Buttons
        this.editName.setVisible(isEdit);
        this.editCountry.setVisible(isEdit);
        this.editYear.setVisible(isEdit);
    }

    @Override
    public void displayValues(Publisher publisher) {
        if (publisher == null) return;
        //Fill with Book Data
        if (publisher.getName() != null && !publisher.getName().isBlank())
            this.name.setText(publisher.getName());

        if (publisher.getCountry() != null && !publisher.getCountry().isBlank())
            this.country.setText(publisher.getCountry());

        this.year.setText(String.valueOf(publisher.getFoundation()));
    }

    @Override
    public void returnToMain() {
        if (model.isCreateMode() || model.isEditMode()) {
            if (!sceneController.confirmMessage("Leave Editing", "Unsaved Changes will be lost!")) return;
        }
        super.returnToMain();

        sceneController.loadMainPublisherScene();
    }

    //--------EDIT VALUES

    public void editName() {

    }

    public void editCountry() {

    }

    public void editYear() {

    }

    @Override
    public void submitChanges() {
        boolean res = model.isCreateMode() ? model.getPublisherRequest().createPublisher() : model.getPublisherRequest().editPublisher();
        if (res) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");

    }

    @Override
    public void deleteItem() {
        if (sceneController.confirmMessage("Delete Author", "Do you really want to delete " + model.getPublisherRequest().getSelection().getName() + " ?")) {
            boolean res = model.getPublisherRequest().deletePublisher(model.getPublisherRequest().getSelection().getPublisherId());
            if (res) sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
