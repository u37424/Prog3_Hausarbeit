package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.ILoginController;
import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.client.view.View;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController implements ILoginController {

    private Stage stage;
    private MainModel model;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(MainModel model) {
        this.model = model;
    }

    @Override
    @FXML
    public void loginNormalUser() {
        //Normaler Benutzer kann einfach einloggen
        loadMainScene("/main.fxml");
    }

    @Override
    @FXML
    public void loginMainUser() {
        //Wenn erfolgreich eingeloggt, sonst Fehler
        if (!model.login()) View.errorMessage("Login Denied", "Anmeldung als Hauptnutzer fehlgeschlagen!");
        else loadMainScene("/main.fxml");
    }

    private void loadMainScene(String resource) {
        //Laden der Main Scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Parent parent = loader.load();

            MainController controller = loader.getController();
            controller.setModel(model);
            controller.setStage(stage);
            //View an Benutzerart anpassen
            controller.setOptions();
            //Initiales laden der Buchliste beim Betreten
            controller.loadBookList();
            //Scene wechseln
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException | RuntimeException e) {
            View.errorMessage("Resource Error", "Fehler beim laden von Resource: " + resource);
        }
    }
}
