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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class SceneController {
    private MainModel model;
    private Stage stage;
    private final Scene scene;
    private final IController login;
    private final IController mainBook, mainCategory, mainAuthor, mainPublisher;
    private final IController bookViewController, categoryViewController, authorViewController, publisherViewController;

    public SceneController() {
        this.scene = new Scene(new AnchorPane());   //Empty default Scene
        this.login = new LoginController();
        this.mainBook = new MainBookController().setup();
        this.mainCategory = new MainCategoryController().setup();
        this.mainAuthor = new MainAuthorController().setup();
        this.mainPublisher = new MainPublisherController().setup();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));   //Laden
            loader.setController(controller);   //Controller setzen

            Parent parent = loader.load();  //Laden der FXML Daten (Insert in Controller)

            setParameters(controller);  //Parameter erst setzen, nachdem Controller die FXML Daten erhalten hat

            scene.setRoot(parent);  //Anzeigen
        } catch (IOException | RuntimeException e) {
            errorMessage("Resource Error", "Fehler beim laden von Resource: " + resource);
        }
    }

    private void setParameters(IController controller) {
        controller.setStage(stage);
        controller.setModel(model);
        controller.setSceneController(this);
    }

    public void infoMessage(String title, String message) {
        message(title, message, Alert.AlertType.INFORMATION);
    }

    public void errorMessage(String title, String message) {
        message(title, message, Alert.AlertType.ERROR);
    }

    private void message(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.name() + " MESSAGE");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean confirmMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(title);
        alert.setContentText(message);
        Optional<ButtonType> res = alert.showAndWait();
        return res.isPresent() && res.get().equals(ButtonType.OK);
    }

    public int editNumberMessage(String title, String def, String message) {
        try {
            return Integer.parseInt(editStringMessage(title, def, message));
        } catch (NumberFormatException e) {
            errorMessage("Invalid Type", "No valid Number entered.");
            editNumberMessage(title, def, message);
        }
        return Integer.parseInt(def);
    }

    public String editStringMessage(String title, String def, String message) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField textField = new TextField();
        textField.setPromptText(def);
        if (!model.isCreateMode()) textField.setText(def);
        textField.setPrefWidth(350);

        grid.add(new Label(message), 0, 0);
        grid.add(textField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        dialog.setResultConverter(b -> b == ButtonType.OK ? textField.getText() : def);

        return dialog.showAndWait().orElse(def);
    }

    public <T> LinkedList<T> editList(String title, LinkedList<T> selection, LinkedList<T> all) {
        Dialog<List<T>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setResizable(true);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        ListView<T> listViewLeft = new ListView<>();
        listViewLeft.getItems().addAll(selection);
        listViewLeft.setPrefWidth(200);
        grid.add(listViewLeft, 0, 0);

        ListView<T> listViewRight = new ListView<>();
        listViewRight.getItems().addAll(all);
        listViewRight.setPrefWidth(200);
        grid.add(listViewRight, 1, 0);

        setEditListTransfer(listViewRight, listViewLeft);
        setEditListTransfer(listViewLeft, listViewRight);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(b -> b == ButtonType.OK ? listViewLeft.getItems() : new LinkedList<>());

        return new LinkedList<>(dialog.showAndWait().orElse(new LinkedList<>()));
    }

    private <T> void setEditListTransfer(ListView<T> destination, ListView<T> source) {
        source.setOnMouseClicked(event -> {
            if (event.getClickCount() < 2) return;
            T selectedItem = source.getSelectionModel().getSelectedItem();
            if (selectedItem == null) return;
            source.getItems().remove(selectedItem);
            destination.getItems().add(selectedItem);
        });
    }

    public <T> T choiceMessage(String title, T def, LinkedList<T> list) {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);

        ChoiceBox<T> choiceBox = new ChoiceBox<>();
        choiceBox.setPrefHeight(50);
        choiceBox.setValue(def);
        choiceBox.setPadding(new Insets(10));
        ObservableList<T> observableList = FXCollections.observableList(list);
        choiceBox.setItems(observableList);

        dialog.getDialogPane().setContent(choiceBox);

        dialog.setResultConverter(b -> b == ButtonType.OK ? choiceBox.getValue() : def);

        return dialog.showAndWait().orElse(def);
    }
}
