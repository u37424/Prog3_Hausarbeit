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

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Die Klasse stellt alle Szenenuebergreifenden Funktionen bereit.
 * Hier koennen Szenen gewechselt werden, ohne Model und View zu duplizieren.
 * Alle verwendbaren Contoller werden hier gespeichert, damit sie wiederverwendet werden koennen.
 * Ebenso koennen hier Dialoge von allen Controllern verwendet werden, um Nachrichten anzuzeigen und Eingaben abzufragen.
 * </p>
 */
public class SceneController {
    private MainModel model;
    private Stage stage;
    private final Scene scene;

    //Alle Controller speichern, damit sie nicht dauernd neu erstellt werden muessen und ihre Einstellungen speichern koennen
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

    /**
     * Laedt die Login FXML Datei und uebertraegt automatisch den passenden Controller.
     */
    public void loadLoginScene() {
        loadScene("/login.fxml", login);
    }

    /**
     * Laedt die Main FXML Datei und uebertraegt automatisch den passenden MainBookController.
     */
    public void loadMainBookScene() {
        loadScene("/main.fxml", mainBook);
    }

    /**
     * Laedt die Main FXML Datei und uebertraegt automatisch den passenden MainCategoryController.
     */
    public void loadMainCategoryScene() {
        loadScene("/main.fxml", mainCategory);
    }

    /**
     * Laedt die Main FXML Datei und uebertraegt automatisch den passenden MainAuthorController.
     */
    public void loadMainAuthorScene() {
        loadScene("/main.fxml", mainAuthor);
    }

    /**
     * Laedt die Main FXML Datei und uebertraegt automatisch den passenden MainPublisherController.
     */
    public void loadMainPublisherScene() {
        loadScene("/main.fxml", mainPublisher);
    }

    /**
     * Laedt die BookView FXML Datei und uebertraegt automatisch den passenden BookViewController.
     */
    public void loadBookViewScene() {
        loadScene("/book.fxml", bookViewController);
    }

    /**
     * Laedt die CategoryView FXML Datei und uebertraegt automatisch den passenden CategoryViewController.
     */
    public void loadCategoryViewScene() {
        loadScene("/category.fxml", categoryViewController);
    }

    /**
     * Laedt die AuthorView FXML Datei und uebertraegt automatisch den passenden AuthorViewController.
     */
    public void loadAuthorViewScene() {
        loadScene("/author.fxml", authorViewController);
    }

    /**
     * Laedt die PublisherView FXML Datei und uebertraegt automatisch den passenden PublisherViewController.
     */
    public void loadPublisherViewScene() {
        loadScene("/publisher.fxml", publisherViewController);
    }

    /**
     * Laedt eine spezifizierte FXML Datei und initialisiert den angegebenen Controller.
     * Die Datei muss unter den Ressourcen dieser Klasse vorhanden sein.
     *
     * @param resource   Resource Name der FXML Datei
     * @param controller Controller, der der FXML Datei uebergeben werden soll
     */
    private void loadScene(String resource, IController controller) {
        try {
            final FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));   //Laden
            loader.setController(controller);   //Controller setzen

            Parent parent = loader.load();  //Laden der FXML Daten (Insert in Controller)

            setParameters(controller);  //Parameter erst setzen, nachdem Controller die FXML Daten erhalten hat

            scene.setRoot(parent);  //Anzeigen
        } catch (IOException | RuntimeException e) {
            errorMessage("Resource Error", "Fehler beim laden von Resource: " + resource);
        }
    }

    /**
     * Verbindet den Controller mit Model und View.
     *
     * @param controller anzugebender Controller
     */
    private void setParameters(IController controller) {
        controller.setStage(stage);
        controller.setModel(model);
        controller.setSceneController(this);
    }

    /**
     * Gibt dem Benutzer eine Nachricht als Infonachricht aus.
     *
     * @param title   Titel der Nachricht
     * @param message Text der Nachricht
     */
    public void infoMessage(String title, String message) {
        message(title, message, Alert.AlertType.INFORMATION);
    }

    /**
     * Gibt dem Benutzer eine Nachricht als Fehlernachricht aus.
     *
     * @param title   Titel der Nachricht
     * @param message Text der Nachricht
     */
    public void errorMessage(String title, String message) {
        message(title, message, Alert.AlertType.ERROR);
    }

    /**
     * Gibt dem Benutzer eine Nachricht aus.
     *
     * @param title   Titel der Nachricht
     * @param message Text der Nachricht
     * @param type    Nachrichtentyp
     */
    private void message(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(type.name() + " MESSAGE");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Gibt dem Benutzer eine Nachricht aus, die er bestaetigen oder ablehnen kann.
     *
     * @param title   Titel der Nachricht
     * @param message Text der Nachricht
     * @return true, wenn angenommen, sonst false
     */
    public boolean confirmMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(title);
        alert.setContentText(message);
        Optional<ButtonType> res = alert.showAndWait();
        return res.isPresent() && res.get().equals(ButtonType.OK);
    }

    /**
     * Gibt dem Benutzer ein Textfeld aus, in dem er eine Nummer eingeben kann.
     * Der Bneutzer darf keinen Text eingeben.
     *
     * @param title   Titel der Nachricht
     * @param def     Standardwert des Textfeldes
     * @param message Text der Nachricht
     * @return Eingabe des Users als Integer
     */
    public int editNumberMessage(String title, String def, String message) {
        try {
            return Integer.parseInt(editStringMessage(title, def, message));
        } catch (NumberFormatException e) {
            errorMessage("Invalid Type", "No valid Number entered.");
            editNumberMessage(title, def, message);
        }
        return Integer.parseInt(def);
    }

    /**
     * Gibt dem Benutzer ein Textfeld aus, in dem er einen Text eingeben kann.
     *
     * @param title   Titel der Nachricht
     * @param def     Standardwert des Textfeldes
     * @param message Text der Nachricht
     * @return Eingabe des Users
     */
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

    /**
     * Gibt dem Benutzer zwei Listen aus, eine Auswahl (links) und eine komplette Liste (rechts).
     * Der Benutzer kann die in den Listen enthaltende Werte per Doppelklick zwischen den Listen verschieben.
     *
     * @param title     Titel der Nachricht
     * @param selection Elemente, die in der Auswahl sein sollen
     * @param all       Elemente, die in der kompletten Liste sein sollen
     * @param <T>       Art der Elemente
     * @return Elemente in der Auswahl Liste.
     */
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

    /**
     * Setzt die Transferaktion beim Klick auf ein Element in einer Liste (im ListDialog).
     *
     * @param destination Zielliste des Elementes
     * @param source      Herkunftsliste des Elementes
     * @param <T>         Art des Elementes
     */
    private <T> void setEditListTransfer(ListView<T> destination, ListView<T> source) {
        source.setOnMouseClicked(event -> {
            if (event.getClickCount() < 2) return;
            T selectedItem = source.getSelectionModel().getSelectedItem();
            if (selectedItem == null) return;
            source.getItems().remove(selectedItem);
            destination.getItems().add(selectedItem);
        });
    }

    /**
     * Gibt dem Benutzer eine Auswahl an Elementen aus der er waehlen kann.
     *
     * @param title Titel der Nachricht
     * @param def   Standardwert der Nachricht
     * @param list  Liste an Auswahlelementen
     * @param <T>   Art der Elemente
     * @return Auswahl des Benutzers
     */
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
