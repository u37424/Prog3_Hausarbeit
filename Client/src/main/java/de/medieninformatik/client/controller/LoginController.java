package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.ILoginController;
import de.medieninformatik.client.model.MainModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse implementiert einen Controller, der das Login verfahren kontrolliert.
 * Der Benutzer kann sich als normaler Benutzer oder Hauptbenutzer anmelden.
 * Eine Anmeldung als Hauptbenutzer kann fehlschlagen.
 * Eine Anmeldung als normaler Benutzer ist immer erfolgreich.
 * </p>
 */
public class LoginController implements ILoginController {

    private MainModel model;
    private SceneController sceneController;

    @FXML
    private TextField hostName;

    @FXML
    private Button hostButton;

    /**
     * Wird beim Laden der FXML Datei aufgerufen, um initiale Einstellungen beim Laden des Controllers zu machen.
     */
    @Override
    public void initialize() {
        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        hostName.setText(bundle.getString("Host.Address"));
    }

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    @Override
    public void setStage(Stage stage) {
        stage.setTitle("Login");
    }

    /**
     * Setzt das entsprechende Model, welches vom Controller verwendet werden soll.
     *
     * @param model Model, welches verwendet werden soll
     */
    @Override
    public void setModel(MainModel model) {
        this.model = model;
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
     * Leitet den Benutzer im normalen Modus weiter.
     * Alle Einstellungen werden auf den Normalen Modus angepasst.
     */
    @Override
    public void loginNormalUser() {
        //Normaler Benutzer kann einfach einloggen
        sceneController.loadMainBookScene();
    }

    /**
     * Versucht eine Anmeldung als Hauptbenutzer durchzufuehren.
     * Wenn der Benutzer sich erfolgreich anmelden konnte, wird er im Hauptbenutzer Modus weitergeleitet.
     * Alle Einstellungen werden auf den Hauptbenutzerstatus angepasst.
     * Kann sich der Benutzer nicht anmelden, werden alle weiteren Einstellungen abgebrochen.
     */
    @Override
    public void loginMainUser() {
        //Wenn erfolgreich eingeloggt, sonst Fehler
        if (!model.login())
            sceneController.errorMessage("Login Denied", "Login as Main User has failed!");
        else sceneController.loadMainBookScene();
    }

    /**
     * Aendert den Host Namen der Server Adresse.
     * Es erfolgt keine Pruefung auf eine Korrektheit der angegebenen Adresse.
     */
    public void changeHostName() {
        String name = hostName.getText();
        if (name == null || name.isBlank()) return;
        model.getUserModel().changeHostName(name);
        sceneController.infoMessage("Host Changed", "Host Name successfully changed!");
        this.hostButton.setDisable(true);
    }

    /**
     * Aktiviert einen Button, der dem Benutzer erlaubt, den Namen des Hosts zu aendern.
     */
    public void enableChangeButton() {
        this.hostButton.setDisable(false);
    }
}
