package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.common.Category;
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
 * Diese Klasse implementiert einen konkreten ViewController fuer Kategorien.
 * Dieser Controller implementiert alle spezifischen Funktionen, die fuer die Arbeit mit einer Kategorie-Ansicht gebraucht werden.
 * </p>
 */
public class CategoryViewController extends ViewController {
    @FXML
    private Label name;

    @FXML
    private Button editName;

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    public void setStage(Stage stage) {
        super.setStage(stage);
        this.stage.setTitle("Category Inspector");
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
    }

    /**
     * Setzt die eingestellten Default-Werte auf die erhaltenden Werte des zu betrachtenden Elementes.
     */
    @Override
    public void displayValues() {
        Category category = model.getCategoryModel().getItem();
        if (category == null) return;

        this.name.setText(category.getName());
    }

    /**
     * Leitet alle notwendigen Aktionen ein, um den Benutzer in den entsprechenden Main Modus zu bringen.
     */
    @Override
    public void returnToMain() {
        super.returnToMain();
        sceneController.loadMainCategoryScene();
    }

    //--------EDIT VALUES

    /**
     * Setzt den aktuell eingegebenen Namen als Namen der Kategorie.
     */
    public void editCategoryName() {
        String eingabe = sceneController.editStringMessage("Edit Name", this.name.getText(), "Category Name");
        if (eingabe == null) return;
        this.name.setText(eingabe);
        model.getCategoryModel().getItem().setName(eingabe);
    }

    //-----ENDE EDIT VALUES

    /**
     * Prueft alle veraenderbaren Werte des dargestellten Objektes auf Korrektheit.
     *
     * @return true, wenn alle Werte den Vorgaben entsprechend vorhanden sind
     */
    @Override
    public boolean validateItem() {
        Category item = model.getCategoryModel().getItem();
        return item.getName() != null && !item.getName().isBlank();
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
        boolean executed = model.isCreateMode() ? model.getCategoryModel().createItem() : model.getCategoryModel().editItem();

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
        if (!sceneController.confirmMessage("Delete Category", "Do you really want to delete this Category?")) return;

        int id = model.getCategoryModel().getItem().getCategoryId();
        boolean executed = model.getCategoryModel().deleteItem(String.valueOf(id));

        if (executed) {
            sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            returnToMain();
        } else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
    }
}
