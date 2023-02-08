package de.medieninformatik.client.controller.inspector;

import de.medieninformatik.client.controller.SceneController;
import de.medieninformatik.client.interfaces.IViewController;
import de.medieninformatik.client.model.MainModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse implementiert einen abstrakten ViewController, von dem alle weiteren ViewController abgeleitet werden koennen.
 * Die Klasse vereint alle wichtigen Funktionen, die alle ViewController besitzen sollten.
 * Diese Funktionen koennen unabhaengig von der konkreten Implementation eingebunden werden.
 * Von der konkreten Implementation abhaengige Funktionen muessen von den daraus abgeleiteten Klassen eingebunden werden.
 * Ebenso werden Funktionen bereitgestellt, die von allen Subklassen verwendet werden koennen.
 * </p>
 */
public abstract class ViewController implements IViewController {
    @FXML
    protected Button submitButton, deleteButton, backButton;

    protected Stage stage;
    protected MainModel model;
    protected SceneController sceneController;

    /**
     * Wird beim Laden der FXML Datei aufgerufen, um initiale Einstellungen beim Laden des Controllers zu machen.
     */
    @Override
    public void initialize() {
    }

    /**
     * Setzt das entsprechende Model, welches vom Controller verwendet werden soll.
     *
     * @param model Model, welches verwendet werden soll
     */
    @Override
    public void setModel(MainModel model) {
        this.model = model;
        setOptions();
        displayValues();
    }

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Setzt die Instanz des entsprechenden SceneControllers.
     *
     * @param sceneController Instanz des SceneControllers
     */
    @Override
    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /**
     * Setzt die Optionen, die der Benutzer entsprechend zu seinem Benutzerstatus im Betrachtungsfenster haben soll.
     */
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

    /**
     * Setzt die eingestellten Default-Werte auf die erhaltenden Werte des zu betrachtenden Elementes.
     */
    @Override
    public abstract void displayValues();

    /**
     * Leitet alle notwendigen Aktionen ein, um den Benutzer in den entsprechenden Main Modus zu bringen.
     */
    @Override
    public void returnToMain() {
        model.setCreateMode(false);
    }

    /**
     * Prueft alle veraenderbaren Werte des dargestellten Objektes auf Korrektheit.
     *
     * @return true, wenn alle Werte den Vorgaben entsprechend vorhanden sind
     */
    @Override
    public abstract boolean validateItem();

    /**
     * Ermoeglicht es dem Hauptbenutzer den aktuellen Eintrag mit den aktuellen Werten zu speichern.
     */
    @Override
    public abstract void submitChanges();

    /**
     * Ermoeglicht es dem Hauptbenutzer den aktuellen Eintrag mit den aktuellen Werten zu loeschen.
     */
    @Override
    public abstract void deleteItem();
}
