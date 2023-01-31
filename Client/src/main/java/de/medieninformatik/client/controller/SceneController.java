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

    public void infoMessage(String title, String message) {
        message(title, message, Alert.AlertType.INFORMATION);
    }

    public void errorMessage(String title, String message) {
        message(title, message, Alert.AlertType.ERROR);
    }

    private void message(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.name() + " Message");
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

    public String editMessage(String title, String def, String message) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));

        TextField textField = new TextField();
        textField.setPromptText(def);
        textField.setPrefWidth(350);
        grid.add(new Label(message), 0, 0);
        grid.add(textField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        dialog.setResultConverter((ButtonType b) -> {
            if (b == submitButtonType) {
                return textField.getText();
            } else return def;
        });

        textField.requestFocus();

        Optional<String> result = dialog.showAndWait();

        return result.orElse(null);
    }

    public <T> LinkedList<T> editList(String title, LinkedList<T> selection, LinkedList<T> all) {
        Dialog<List<T>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setResizable(true);

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        ListView<T> listViewLeft = new ListView<>();
        listViewLeft.getItems().addAll(selection);
        listViewLeft.setPrefWidth(200);
        grid.add(listViewLeft, 0, 0);

        ListView<T> listViewRight = new ListView<>();
        if (all != null) listViewRight.getItems().addAll(all);
        listViewRight.setPrefWidth(200);
        grid.add(listViewRight, 1, 0);

        setItemClickedEvent(listViewRight, listViewLeft);
        setItemClickedEvent(listViewLeft, listViewRight);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return listViewLeft.getItems();
            }
            return null;
        });

        Optional<List<T>> result = dialog.showAndWait();

        return new LinkedList<>(result.orElseGet(LinkedList::new));
    }


    private <T> void setItemClickedEvent(ListView<T> destination, ListView<T> source) {
        source.setOnMouseClicked(event -> {
            if (event.getClickCount() < 2) return;
            T selectedItem = source.getSelectionModel().getSelectedItem();
            if (selectedItem == null) return;
            source.getItems().remove(selectedItem);
            destination.getItems().add(selectedItem);
        });
    }

    public<T> T choiceMessage(String title, T def, LinkedList<T> list) {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setResizable(true);

        ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        ChoiceBox<T> choiceBox = new ChoiceBox<>();
        choiceBox.setPrefHeight(50);
        choiceBox.setValue(def);
        choiceBox.setPadding(new Insets(10));
        ObservableList<T> observableList = FXCollections.observableList(list);
        choiceBox.setItems(observableList);

        dialog.getDialogPane().setContent(choiceBox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButtonType) {
                return choiceBox.getValue();
            }
            return null;
        });

        Optional<T> result = dialog.showAndWait();

        return result.orElse(def);
    }
}
