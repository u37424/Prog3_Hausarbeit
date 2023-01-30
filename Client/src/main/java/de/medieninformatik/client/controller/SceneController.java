package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.IController;
import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.client.view.View;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class SceneController {
    private MainModel model;
    private Stage stage;
    private Scene scene;
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
        this.scene = new Scene(new AnchorPane());
        this.login = new LoginController();
        this.mainBook = new MainBookController();
        this.mainCategory = new MainCategoryController();
        this.mainAuthor = new MainAuthorController();
        this.mainPublisher = new MainPublisherController();
        this.bookViewController = new BookViewController();
        this.categoryViewController = new CategoryViewController();
        this.authorViewController = new AuthorViewController();
        this.publisherViewController = new PublisherViewController();
    }

    public void setModel(MainModel model) {
        this.model = model;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setScene(scene);
    }

    public void loadLoginScene(){
        loadScene("/login.fxml", login);
    }

    public void loadMainBookScene(){
        loadScene("/main.fxml", mainBook);
    }

    public void loadMainCategoryScene(){
        loadScene("/main.fxml", mainCategory);
    }

    public void loadMainAuthorScene(){
        loadScene("/main.fxml", mainAuthor);
    }

    public void loadMainPublisherScene(){
        loadScene("/main.fxml", mainPublisher);
    }

    public void loadBookViewScene(){
        loadScene("/book.fxml", bookViewController);
    }

    public void loadCategoryViewScene(){
        loadScene("/category.fxml", categoryViewController);
    }

    public void loadAuthorViewScene() {
        loadScene("/author.fxml", authorViewController);
    }

    public void loadPublisherViewScene() {
        loadScene("/publisher.fxml", publisherViewController);
    }

    private void loadScene(String resource, IController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            loader.setController(controller);

            Parent parent = loader.load();

            setParameters(controller);

            scene.setRoot(parent);
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
