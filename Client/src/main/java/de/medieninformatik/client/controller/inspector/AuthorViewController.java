package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Author;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AuthorViewController extends ViewController {
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
        displayValues();
    }

    @Override
    public void setOptions() {
        if (!model.isMainUser()) return;
        super.setOptions();
        boolean isEdit = model.isEditMode();
        //Edit Buttons
        this.editFirstName.setVisible(isEdit);
        this.editLastName.setVisible(isEdit);
        this.editAlias.setVisible(isEdit);
        this.editAge.setVisible(isEdit);
        this.birthday.setVisible(!isEdit);
        this.editBirthday.setVisible(isEdit);
    }

    @Override
    public void displayValues() {
        Author author = model.getAuthorRequest().getItem();
        if (author == null) return;

        this.firstName.setText(author.getFirstName());
        this.lastName.setText(author.getLastName());
        this.alias.setText(author.getAlias());
        this.age.setText(String.valueOf(author.getAge()));

        if (author.getBirthday() != null && !author.getBirthday().isBlank()) {
            this.birthday.setText(author.getBirthday());
            this.editBirthday.setValue(LocalDate.parse(author.getBirthday()));
        }
    }

    @Override
    public void returnToMain() {
        super.returnToMain();
        sceneController.loadMainAuthorScene();
    }

    //--------EDIT VALUES

    public void editFirstName() {
        String eingabe = sceneController.editStringMessage("Edit First Name", this.firstName.getText(), "Fist Name");
        if (eingabe == null) return;
        this.firstName.setText(eingabe);
        model.getAuthorRequest().getItem().setFirstName(eingabe);
    }

    public void editLastName() {
        String eingabe = sceneController.editStringMessage("Edit Last Name", this.lastName.getText(), "Last Name");
        if (eingabe == null) return;
        this.lastName.setText(eingabe);
        model.getAuthorRequest().getItem().setLastName(eingabe);
    }

    public void editAlias() {
        String eingabe = sceneController.editStringMessage("Edit Alias Name", this.alias.getText(), "Alias Name");
        if (eingabe == null) return;
        this.alias.setText(eingabe);
        model.getAuthorRequest().getItem().setAlias(eingabe);
    }

    public void editAge() {
        int eingabe = sceneController.editNumberMessage("Edit Age", this.age.getText(), "Age");
        this.age.setText(String.valueOf(eingabe));
        model.getAuthorRequest().getItem().setAge(eingabe);
    }

    public void editBirthday() {
        String eingabe = String.valueOf(this.editBirthday.getValue());
        model.getAuthorRequest().getItem().setBirthday(eingabe);
    }

    @Override
    public boolean validateItem() {
        Author item = model.getAuthorRequest().getItem();
        return item.getFirstName() != null && !item.getFirstName().isBlank() &&
                item.getLastName() != null && !item.getLastName().isBlank() &&
                item.getBirthday() != null && !item.getBirthday().isBlank() &&
                item.getAge() > 0;
    }

    @Override
    public void submitChanges() {
        if (!validateItem()) {
            sceneController.errorMessage("Invalid Object", "Please enter valid Values for all Fields!");
            return;
        }

        //Alias ermitteln, wenn notwendig
        Author author = model.getAuthorRequest().getItem();
        if (author.getAlias() == null || author.getAlias().isBlank())
            model.getAuthorRequest().getItem().setAlias(author.getFirstName().charAt(0) + ". " + author.getLastName());

        boolean executed = model.isCreateMode() ? model.getAuthorRequest().createItem() : model.getAuthorRequest().editItem();

        if (executed) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");
    }

    @Override
    public void deleteItem() {
        if (!sceneController.confirmMessage("Delete Author", "Do you really want to delete this Author?")) return;

        int id = model.getAuthorRequest().getItem().getAuthorId();
        boolean executed = model.getAuthorRequest().deleteItem(String.valueOf(id));

        if (executed) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            returnToMain();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
