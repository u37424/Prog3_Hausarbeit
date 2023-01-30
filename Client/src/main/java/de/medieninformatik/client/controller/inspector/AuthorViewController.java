package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Author;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AuthorViewController extends ViewController<Author> {
    @FXML
    private Label firstName, lastName, alias, age, birthday;

    @FXML
    private DatePicker editBirthday;

    @FXML
    private Button editFirstName, editLastName, editAlias, editAge;

    public void setStage(Stage stage) {
        super.setStage(stage);
        this.stage.setTitle("Author Inspector");
    }

    public void setModel(MainModel model) {
        super.setModel(model);
        //Model received
        setOptions();

        //Load Book when Model received
        Author author = model.getAuthorRequest().getSelection();
        displayValues(author);
    }

    @Override
    public void setOptions() {
        if (!model.isMainUser()) return;
        super.setOptions();

        boolean isEdit = model.isEditMode();
        boolean isCreate = model.isCreateMode();

        //Specific edit Buttons
        this.editFirstName.setVisible(isEdit);
        this.editLastName.setVisible(isEdit);
        this.editAlias.setVisible(isEdit);
        this.editAge.setVisible(isEdit);
        this.birthday.setVisible(!isEdit);
        this.editBirthday.setVisible(isEdit);
    }

    @Override
    public void displayValues(Author author) {
        if (author == null) return;
        //Fill with Book Data
        if (author.getFirstName() != null && !author.getFirstName().isBlank())
            this.firstName.setText(author.getFirstName());

        if (author.getLastName() != null && !author.getLastName().isBlank())
            this.lastName.setText(author.getLastName());

        if (author.getAlias() != null && !author.getAlias().isBlank())
            this.alias.setText(author.getAlias());

        this.age.setText(String.valueOf(author.getAge()));

        if (author.getBirthday() != null && !author.getBirthday().isBlank()) {
            this.birthday.setText(author.getBirthday());
            this.editBirthday.setValue(LocalDate.parse(author.getBirthday()));
        }
    }

    @Override
    public void returnToMain() {
        if (model.isCreateMode() || model.isEditMode()) {
            if (!sceneController.confirmMessage("Leave Editing", "Unsaved Changes will be lost!")) return;
        }
        super.returnToMain();

        sceneController.loadMainAuthorScene();
    }

    //--------EDIT VALUES

    public void editFirstName() {

    }

    public void editLastName() {

    }

    public void editAlias() {

    }

    public void editAge() {

    }

    public void editBirthday() {

    }

    @Override
    public void submitChanges() {
        boolean res = model.isCreateMode() ? model.getAuthorRequest().createAuthor() : model.getAuthorRequest().editAuthor();
        if (res) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");

    }

    @Override
    public void deleteItem() {
        if (sceneController.confirmMessage("Delete Author", "Do you really want to delete " + model.getAuthorRequest().getSelection().getAlias() + " ?")) {
            boolean res = model.getAuthorRequest().deleteAuthor(model.getAuthorRequest().getSelection().getAuthorId());
            if (res) sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
