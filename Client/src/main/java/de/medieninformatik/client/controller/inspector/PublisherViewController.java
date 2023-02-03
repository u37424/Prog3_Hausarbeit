package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Publisher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PublisherViewController extends ViewController {
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
        displayValues();
    }

    @Override
    public void setOptions() {
        if (!model.isMainUser()) return;
        super.setOptions();
        boolean isEdit = model.isEditMode();

        //Edit Buttons
        this.editName.setVisible(isEdit);
        this.editCountry.setVisible(isEdit);
        this.editYear.setVisible(isEdit);
    }

    @Override
    public void displayValues() {
        Publisher publisher = model.getPublisherRequest().getItem();
        if (publisher == null) return;

        this.name.setText(publisher.getName());
        this.country.setText(publisher.getCountry());
        this.year.setText(String.valueOf(publisher.getFoundation()));
    }

    @Override
    public void returnToMain() {
        super.returnToMain();
        sceneController.loadMainPublisherScene();
    }

    //--------EDIT VALUES

    public void editName() {
        String eingabe = sceneController.editStringMessage("Edit Publisher Name", this.name.getText(), "Publisher Name");
        if (eingabe == null) return;
        this.name.setText(eingabe);
        model.getPublisherRequest().getItem().setName(eingabe);
    }

    public void editCountry() {
        String eingabe = sceneController.editStringMessage("Edit Main Country", this.country.getText(), "Main Country");
        if (eingabe == null) return;
        this.country.setText(eingabe);
        model.getPublisherRequest().getItem().setCountry(eingabe);
    }

    public void editYear() {
        int eingabe = sceneController.editNumberMessage("Edit Year of Foundation", this.year.getText(), "Year of Foundation");
        this.year.setText(String.valueOf(eingabe));
        model.getPublisherRequest().getItem().setFoundation(eingabe);
    }

    @Override
    public boolean validateItem() {
        Publisher item = model.getPublisherRequest().getItem();
        return item.getCountry() != null && !item.getCountry().isBlank() &&
                item.getCountry() != null && !item.getCountry().isBlank();
    }

    @Override
    public void submitChanges() {
        if (!validateItem()) {
            sceneController.errorMessage("Invalid Object", "Please enter valid Values for all Fields!");
            return;
        }

        boolean executed = model.isCreateMode() ? model.getPublisherRequest().createItem() : model.getPublisherRequest().editItem();

        if (executed) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");
    }

    @Override
    public void deleteItem() {
        if (!sceneController.confirmMessage("Delete Author", "Do you really want to delete this Publisher?")) return;

        int id = model.getPublisherRequest().getItem().getPublisherId();
        boolean executed = model.getPublisherRequest().deleteItem(String.valueOf(id));

        if (executed) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            returnToMain();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
