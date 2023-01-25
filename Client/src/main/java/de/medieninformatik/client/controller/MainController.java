package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.IController;
import de.medieninformatik.client.interfaces.IMainController;
import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.client.view.View;
import de.medieninformatik.common.Book;
import jakarta.ws.rs.core.Link;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;

public class MainController implements IMainController {
    @FXML
    Button returnButton, pageBackward, pageForward, filterButton, revertButton, orderButton;

    @FXML
    Spinner<Integer> setPageSize;

    @FXML
    Button createBook, createCategory, createAuthor, createPublisher;

    @FXML
    TextField stringInput;

    @FXML
    ChoiceBox<String> categorySelector;

    @FXML
    ListView<HBox> page;

    private String userString;
    private String userCategory;
    private boolean ascending;
    private int pageStart;
    private int pageSize;

    private Stage stage;
    private MainModel model;

    @FXML
    public void initialize() {
        this.userString = "";
        this.userCategory = "";
        this.ascending = true;
        this.pageStart = 0;
        this.pageSize = 5;
        //Limits des Spinners setzen
        setPageSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 5, 5));
        setPageSize.valueProperty().addListener((obs) -> updatePageSize(setPageSize.getValue()));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(MainModel model) {
        this.model = model;
    }

    @Override
    public void loadBookList() {
        //Aktuelle Daten laden
        model.loadBookList(userString, userCategory, pageStart, pageSize, ascending);
        //Liste aus den Daten erstellen
        createBookList();
    }

    private void createBookList() {
        //Liste aus den Daten erstellen
        LinkedList<Book> books = model.getBooks();
        page.setItems(FXCollections.observableArrayList(new HBox(), new HBox()));
    }

    @Override
    public void createBook() {

    }

    @Override
    public void createCategory() {

    }

    @Override
    public void createAuthor() {

    }

    @Override
    public void createPublisher() {

    }

    @Override
    @FXML
    public void setOrder() {
        if (ascending) ((ImageView) orderButton.getGraphic()).setImage(new Image("desc.png"));
        else ((ImageView) orderButton.getGraphic()).setImage(new Image("asc.png"));
        ascending = !ascending;
    }

    @Override
    public void updatePageSize(int size) {
        //Listengroesse aktualisieren
        this.pageSize = size;
    }

    @Override
    @FXML
    public void pageBackward() {
        this.pageStart -= this.pageSize;
        if (pageStart < 0) this.pageStart = 0;   //Nicht < 0
    }

    @Override
    @FXML
    public void pageForward() {
        this.pageStart -= this.pageSize;
        int max = model.getResultMax();
        if (pageStart > max) pageStart = max;    //Aufhoeren wenn > Listengroesse
    }

    @Override
    @FXML
    public void updateFilter() {
        String string = stringInput.getText();
        String category = categorySelector.getValue();

        if (string == null) string = "";
        if (category == null) category = "";

        if (userString.equals(string) && userCategory.equals(category)) return;
        this.userString = string;
        this.userCategory = category;
        //Reload Book List
        loadBookList();
    }

    @Override
    @FXML
    public void resetFilter() {
        updateFilter();
        if (userString.isBlank() && userCategory.equals("")) return;
        this.userString = "";
        this.userCategory = "";
        this.stringInput.setText("");
        this.categorySelector.setValue(null);
        loadBookList();
    }

    @Override
    @FXML
    public void returnToLogin() {
        if (model.isMainUser() && !model.logout()) View.errorMessage("Logout Denied", "");
        else {
            setOptions();
            loadScene("/login.fxml");
        }
    }

    @Override
    public void inspectBook(String isbn) {

    }

    @Override
    public void editBook(String isbn) {

    }

    @Override
    public void deleteBook(String isbn) {

    }

    public void setOptions() {
        boolean mainUser = model.isMainUser();
        //Setzen der Optionen des Hauptnutzers
        this.createBook.setVisible(mainUser);
        this.createCategory.setVisible(mainUser);
        this.createAuthor.setVisible(mainUser);
        this.createPublisher.setVisible(mainUser);
        if (mainUser) this.returnButton.setText("Log Out");
        else this.returnButton.setText("Return");
    }

    private void loadScene(String resource) {
        //Laden der Main Scene
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            Parent parent = loader.load();

            IController controller = loader.getController();
            controller.setModel(model);
            controller.setStage(stage);

            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException | RuntimeException e) {
            View.errorMessage("Resource Error", "Fehler beim laden von Resource: " + resource);
        }
    }
}
