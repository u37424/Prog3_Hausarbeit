package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.common.Author;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse implementiert einen konkreten ViewController fuer Autoren.
 * Dieser Controller implementiert alle spezifischen Funktionen, die fuer die Arbeit mit einer Autor-Ansicht gebraucht werden.
 * </p>
 */
public class AuthorViewController extends ViewController {
    @FXML
    private Label firstName, lastName, alias, age, birthday;

    @FXML
    private DatePicker editBirthday;

    @FXML
    private Button editFirstName, editLastName, editAlias, editAge;

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    public void setStage(Stage stage) {
        super.setStage(stage);
        this.stage.setTitle("Author Inspector");
    }

    /**
     * Setzt die Optionen, die der Benutzer entsprechend zu seinem Benutzerstatus im Betrachtungsfenster haben soll.
     */
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

    /**
     * Setzt die eingestellten Default-Werte auf die erhaltenden Werte des zu betrachtenden Elementes.
     */
    @Override
    public void displayValues() {
        Author author = model.getAuthorModel().getItem();
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

    /**
     * Leitet alle notwendigen Aktionen ein, um den Benutzer in den entsprechenden Main Modus zu bringen.
     */
    @Override
    public void returnToMain() {
        super.returnToMain();
        sceneController.loadMainAuthorScene();
    }

    //--------EDIT VALUES

    /**
     * Setzt den aktuell eingegebenen Vornamen als Vornamen des Autors.
     */
    public void editFirstName() {
        String eingabe = sceneController.editStringMessage("Edit First Name", this.firstName.getText(), "Fist Name");
        if (eingabe == null) return;
        this.firstName.setText(eingabe);
        model.getAuthorModel().getItem().setFirstName(eingabe);
    }

    /**
     * Setzt den aktuell eingegebenen Nachnamen als Nachnamen des Autors.
     */
    public void editLastName() {
        String eingabe = sceneController.editStringMessage("Edit Last Name", this.lastName.getText(), "Last Name");
        if (eingabe == null) return;
        this.lastName.setText(eingabe);
        model.getAuthorModel().getItem().setLastName(eingabe);
    }

    /**
     * Setzt den aktuell eingegebenen Alias als Alias des Autors.
     */
    public void editAlias() {
        String eingabe = sceneController.editStringMessage("Edit Alias Name", this.alias.getText(), "Alias Name");
        if (eingabe == null) return;
        this.alias.setText(eingabe);
        model.getAuthorModel().getItem().setAlias(eingabe);
    }

    /**
     * Setzt das aktuell eingegebene Alter als Alter des Autors.
     */
    public void editAge() {
        int eingabe = sceneController.editNumberMessage("Edit Age", this.age.getText(), "Age");
        this.age.setText(String.valueOf(eingabe));
        model.getAuthorModel().getItem().setAge(eingabe);
    }

    /**
     * Setzt das aktuell eingegebene Geburtsdatum als Geburtsdatum des Autors.
     */
    public void editBirthday() {
        String eingabe = String.valueOf(this.editBirthday.getValue());
        model.getAuthorModel().getItem().setBirthday(eingabe);
    }

    //-----ENDE EDIT VALUES

    /**
     * Prueft alle veraenderbaren Werte des dargestellten Objektes auf Korrektheit.
     *
     * @return true, wenn alle Werte den Vorgaben entsprechend vorhanden sind
     */
    @Override
    public boolean validateItem() {
        Author item = model.getAuthorModel().getItem();
        return item.getFirstName() != null && !item.getFirstName().isBlank() &&
                item.getLastName() != null && !item.getLastName().isBlank() &&
                item.getBirthday() != null && !item.getBirthday().isBlank() &&
                item.getAge() > 0;
    }

    /**
     * Ermoeglicht es dem Hauptbenutzer den aktuellen Eintrag mit den aktuellen Werten zu speichern.
     */
    @Override
    public void submitChanges() {
        if (!validateItem()) {
            sceneController.errorMessage("Invalid Object", "Please enter valid Values for all Fields!");
            return;
        }

        //Alias ermitteln, wenn notwendig
        Author author = model.getAuthorModel().getItem();
        if (author.getAlias() == null || author.getAlias().isBlank())
            model.getAuthorModel().getItem().setAlias(author.getFirstName().charAt(0) + ". " + author.getLastName());

        //Ausfuehrung je nach aktuellem Modus (Erstellen / Bearbeiten)
        boolean executed = model.isCreateMode() ? model.getAuthorModel().createItem() : model.getAuthorModel().editItem();

        if (executed) {
            sceneController.infoMessage("Submit Succeeded", "Changes have been saved on the Server!");
            returnToMain();
        } else sceneController.errorMessage("Submit Error", "Failed to save Changes!");
    }

    /**
     * Ermoeglicht es dem Hauptbenutzer den aktuellen Eintrag mit den aktuellen Werten zu loeschen.
     */
    @Override
    public void deleteItem() {
        if (!sceneController.confirmMessage("Delete Author", "Do you really want to delete this Author?")) return;

        int id = model.getAuthorModel().getItem().getAuthorId();
        boolean executed = model.getAuthorModel().deleteItem(String.valueOf(id));

        if (executed) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            returnToMain();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
