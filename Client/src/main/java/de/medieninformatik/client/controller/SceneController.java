package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.IController;
import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.client.view.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class SceneController {
    private MainModel model;
    private Stage stage;
    private IController login;
    private IController mainBook;
    private IController mainCategory;
    private IController mainAuthor;
    private IController mainPublisher;
    private IController bookViewController;
    private IController categoryViewController;
    private IController authorViewController;
    private IController publisherViewController;

    public SceneController(){
        this.login = new LoginController();
        this.mainBook = new MainBookController();
        this.bookViewController = new BookViewController();
    }

    public void setModel(MainModel model) {
        this.model = model;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadLoginScene(){
        loadScene("/login.fxml", login);
    }

    public void loadMainBookScene(){
        loadScene("/main.fxml", mainBook);
    }

    public void loadBookViewScene(){
        loadScene("/book.fxml", bookViewController);
    }

    private void loadScene(String resource, IController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            loader.setController(controller);

            Parent parent = loader.load();

            setParameters(controller);

            double width = stage.getWidth();
            double height = stage.getHeight();

            Scene scene = new Scene(parent, width, height);

            stage.setScene(scene);
            stage.setWidth(width);
            stage.setHeight(height);
            stage.sizeToScene();
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            errorMessage("Resource Error", "Fehler beim laden von Resource: " + resource);
        }
    }

    private void setParameters(IController controller) {
        controller.setStage(stage);
        controller.setModel(model);
        controller.setSceneController(this);
    }

    public void infoMessage(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info Message");
        alert.setHeaderText(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void errorMessage(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean confirmMessage(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(type);
        alert.setContentText(message);
        Optional<ButtonType> res = alert.showAndWait();
        return res.isPresent() && res.get().equals(ButtonType.OK);
    }
}
