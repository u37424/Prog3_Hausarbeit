package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.ILoginController;
import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.client.view.View;
import javafx.stage.Stage;

public class LoginController implements ILoginController {

    private Stage stage;
    private MainModel model;
    private SceneController sceneController;

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Login");
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
}
