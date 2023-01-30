package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Author;
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
        String eingabe = sceneController.editMessage("Edit Publisher Name", this.name.getText(), "Publisher Name");
        if (eingabe == null || this.name.getText().equals(eingabe)) return;
        this.name.setText(eingabe);
        model.getPublisherRequest().getSelection().setName(eingabe);
    }

    public void editCountry() {
        String eingabe = sceneController.editMessage("Edit Main Country", this.country.getText(), "Main Country");
        if (eingabe == null || this.country.getText().equals(eingabe)) return;
        this.country.setText(eingabe);
        model.getPublisherRequest().getSelection().setCountry(eingabe);
    }

    public void editYear() {
        try {
            int eingabe = Integer.parseInt(sceneController.editMessage("Edit Year of Foundation", this.year.getText(), "Year of Foundation"));
            if (Integer.parseInt(this.year.getText()) == eingabe) return;
            this.year.setText(String.valueOf(eingabe));
            model.getPublisherRequest().getSelection().setFoundation(eingabe);
        } catch (NumberFormatException e) {
            sceneController.errorMessage("Invalid Type", "No valid Number entered.");
        }
    }

    @Override
    public void submitChanges() {
        Publisher publisher = model.getPublisherRequest().getSelection();
        if (publisher.getName() == null || publisher.getCountry() == null || publisher.getFoundation() <= 0) {
            sceneController.errorMessage("Invalid Object", "Please enter valid Values for all Fields!");
            return;
        }

        boolean res = model.isCreateMode() ? model.getPublisherRequest().createPublisher() : model.getPublisherRequest().editPublisher();
        if (res) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            super.returnToMain();
            sceneController.loadMainPublisherScene();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");

    }

    @Override
    public void deleteItem() {
        if (sceneController.confirmMessage("Delete Author", "Do you really want to delete " + model.getPublisherRequest().getSelection().getName() + " ?")) {
            boolean res = model.getPublisherRequest().deletePublisher(model.getPublisherRequest().getSelection().getPublisherId());
            if (res) {
                sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
                super.returnToMain();
                sceneController.loadMainPublisherScene();
            } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
