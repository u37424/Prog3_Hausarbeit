package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.common.Publisher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse implementiert einen konkreten ViewController fuer Publisher.
 * Dieser Controller implementiert alle spezifischen Funktionen, die fuer die Arbeit mit einer Publisher-Ansicht gebraucht werden.
 * </p>
 */
public class PublisherViewController extends ViewController {
    @FXML
    private Label name, country, year;

    @FXML
    private Button editName, editCountry, editYear;

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    public void setStage(Stage stage) {
        super.setStage(stage);
        this.stage.setTitle("Publisher Inspector");
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
        this.editName.setVisible(isEdit);
        this.editCountry.setVisible(isEdit);
        this.editYear.setVisible(isEdit);
    }

    /**
     * Setzt die eingestellten Default-Werte auf die erhaltenden Werte des zu betrachtenden Elementes.
     */
    @Override
    public void displayValues() {
        Publisher publisher = model.getPublisherModel().getItem();
        if (publisher == null) return;

        this.name.setText(publisher.getName());
        this.country.setText(publisher.getCountry());
        this.year.setText(String.valueOf(publisher.getFoundation()));
    }

    /**
     * Leitet alle notwendigen Aktionen ein, um den Benutzer in den entsprechenden Main Modus zu bringen.
     */
    @Override
    public void returnToMain() {
        super.returnToMain();
        sceneController.loadMainPublisherScene();
    }

    //--------EDIT VALUES

    /**
     * Setzt den aktuell eingegebenen Namen als Namen des Publishers.
     */
    public void editName() {
        String eingabe = sceneController.editStringMessage("Edit Publisher Name", this.name.getText(), "Publisher Name");
        if (eingabe == null) return;
        this.name.setText(eingabe);
        model.getPublisherModel().getItem().setName(eingabe);
    }

    /**
     * Setzt das aktuell eingegebene Land als Land des Publishers.
     */
    public void editCountry() {
        String eingabe = sceneController.editStringMessage("Edit Main Country", this.country.getText(), "Main Country");
        if (eingabe == null) return;
        this.country.setText(eingabe);
        model.getPublisherModel().getItem().setCountry(eingabe);
    }

    /**
     * Setzt das aktuell eingegebene Gruendungsjahr als Gruendungsjahr des Publishers.
     */
    public void editYear() {
        int eingabe = sceneController.editNumberMessage("Edit Year of Foundation", this.year.getText(), "Year of Foundation");
        this.year.setText(String.valueOf(eingabe));
        model.getPublisherModel().getItem().setFoundation(eingabe);
    }

    //------ENDE EDIT VALUES

    /**
     * Prueft alle veraenderbaren Werte des dargestellten Objektes auf Korrektheit.
     *
     * @return true, wenn alle Werte den Vorgaben entsprechend vorhanden sind
     */
    @Override
    public boolean validateItem() {
        Publisher item = model.getPublisherModel().getItem();
        return item.getCountry() != null && !item.getCountry().isBlank() &&
                item.getCountry() != null && !item.getCountry().isBlank();
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

        //Ausfuehrung je nach aktuellem Modus (Erstellen / Bearbeiten)
        boolean executed = model.isCreateMode() ? model.getPublisherModel().createItem() : model.getPublisherModel().editItem();

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
        if (!sceneController.confirmMessage("Delete Author", "Do you really want to delete this Publisher?")) return;

        int id = model.getPublisherModel().getItem().getPublisherId();
        boolean executed = model.getPublisherModel().deleteItem(String.valueOf(id));

        if (executed) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            returnToMain();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
