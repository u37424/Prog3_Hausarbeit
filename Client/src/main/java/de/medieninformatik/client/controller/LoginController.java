package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.ILoginController;
import de.medieninformatik.client.model.MainModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class LoginController implements ILoginController {

    private MainModel model;
    private SceneController sceneController;

    @FXML
    private TextField hostName;

    @FXML
    private Button hostButton;

    @Override
    public void initialize() {
        ResourceBundle bundle = ResourceBundle.getBundle("ServerResources");
        hostName.setText(bundle.getString("Host.Address"));
    }

    public void setStage(Stage stage) {
        stage.setTitle("Login");
    }

    public void setModel(MainModel model) {
        this.model = model;
    }

    @Override
    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @Override
    public void loginNormalUser() {
        //Normaler Benutzer kann einfach einloggen
        sceneController.loadMainBookScene();
    }

    @Override
    public void loginMainUser() {
        //Wenn erfolgreich eingeloggt, sonst Fehler
        if (!model.login()) sceneController.errorMessage("Login Denied", "Login as Main User has failed!");
        else sceneController.loadMainBookScene();
    }

    public void changeHostName() {
        String name = hostName.getText();
        if (name == null || name.isBlank()) return;
        model.changeHostName(name);
        sceneController.infoMessage("Host Changed", "Host Name successfully changed!");
        this.hostButton.setDisable(true);
    }

    public void enableChangeButton() {
        this.hostButton.setDisable(false);
    }
}
