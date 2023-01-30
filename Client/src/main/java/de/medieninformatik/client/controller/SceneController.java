package de.medieninformatik.client.controller;

import de.medieninformatik.client.controller.inspector.AuthorViewController;
import de.medieninformatik.client.controller.inspector.BookViewController;
import de.medieninformatik.client.controller.inspector.CategoryViewController;
import de.medieninformatik.client.controller.inspector.PublisherViewController;
import de.medieninformatik.client.controller.main.MainAuthorController;
import de.medieninformatik.client.controller.main.MainBookController;
import de.medieninformatik.client.controller.main.MainCategoryController;
import de.medieninformatik.client.controller.main.MainPublisherController;
import de.medieninformatik.client.interfaces.IController;
import de.medieninformatik.client.model.MainModel;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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

    public SceneController() {
        this.scene = new Scene(new AnchorPane());
        this.login = new LoginController();
        this.mainBook = new MainBookController();
        mainBook.setup();
        this.mainCategory = new MainCategoryController();
        mainCategory.setup();
        this.mainAuthor = new MainAuthorController();
        mainAuthor.setup();
        this.mainPublisher = new MainPublisherController();
        mainPublisher.setup();
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

    public void loadLoginScene() {
        loadScene("/login.fxml", login);
    }

    public void loadMainBookScene() {
        loadScene("/main.fxml", mainBook);
    }

    public void loadMainCategoryScene() {
        loadScene("/main.fxml", mainCategory);
    }

    public void loadMainAuthorScene() {
        loadScene("/main.fxml", mainAuthor);
    }

    public void loadMainPublisherScene() {
        loadScene("/main.fxml", mainPublisher);
    }

    public void loadBookViewScene() {
        loadScene("/book.fxml", bookViewController);
    }

    public void loadCategoryViewScene() {
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

    public String editMessage(String type, String def, String message) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(type);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField textField = new TextField();
        textField.setText(def);
        textField.setPrefWidth(350);
        grid.add(new Label(message), 0, 0);
        grid.add(textField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);

        dialog.setResultConverter((ButtonType b) -> {
            if (b == buttonTypeOk) {
                return textField.getText();
            }
            if (b == buttonTypeCancel) {
                return def;
            }
            return def;
        });

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }
}
