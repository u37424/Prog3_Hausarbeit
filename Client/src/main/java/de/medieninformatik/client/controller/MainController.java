package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.IMainController;
import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.client.view.View;
import de.medieninformatik.common.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.LinkedList;

public class MainController implements IMainController {
    @FXML
    Button returnButton, pageBackward, pageForward, filterButton, revertButton, orderButton;

    @FXML
    Spinner<Integer> setPageSize;

    @FXML
    Button createBook, createCategory, createAuthor, createPublisher, databaseButton;

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

        //Onclick eines Listen Items (HBox.getID() == someISBN)
        page.setOnMouseClicked((e) -> {
            if (e.getClickCount() > 1) {
                if (page.getSelectionModel().getSelectedItem() == null) return;
                String isbnId = page.getSelectionModel().getSelectedItem().getId();
                inspectBook(isbnId);
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("List Viewer");
    }

    public void setModel(MainModel model) {
        this.model = model;
        setOptions();
        //Initiales laden der Buchliste beim Betreten
        loadBookList();
    }

    public void setOptions() {
        boolean mainUser = model.isMainUser();
        //Setzen der Optionen des Hauptnutzers
        this.databaseButton.setVisible(mainUser);
        this.createBook.setVisible(mainUser);
        this.createCategory.setVisible(mainUser);
        this.createAuthor.setVisible(mainUser);
        this.createPublisher.setVisible(mainUser);
        if (mainUser) this.returnButton.setText("Log Out");
        else this.returnButton.setText("Return");
    }

    @Override
    public void loadBookList() {
        //Aktuelle Daten laden
        model.loadBookList(userString, userCategory, pageStart, pageSize, ascending);
        //Liste aus den Daten erstellen
        ObservableList<HBox> list = FXCollections.observableArrayList(buildBookItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<HBox> buildBookItems() {
        LinkedList<HBox> list = new LinkedList<>(); //Baue einen HBox Container pro Buch
        //Liste aus den Daten erstellen
        LinkedList<Book> books = model.getBooks();
        //List HBox
        //foreach book -> list.add(new HBox());
        list.add(new HBox());
        list.add(new HBox());
        list.add(new HBox());
        list.add(new HBox());
        return list;
    }

    @Override
    @FXML
    public void createBook() {
        model.setCrateMode(true);
        model.setSelection(new Book(""));
        View.loadScene("/book.fxml", stage, model);
    }

    @Override
    @FXML
    public void createCategory() {

    }

    @Override
    @FXML
    public void createAuthor() {

    }

    @Override
    @FXML
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
        //Reload Book List
        loadBookList();
    }

    @Override
    @FXML
    public void pageForward() {
        this.pageStart -= this.pageSize;
        int max = model.getResultMax();
        if (pageStart > max) pageStart = max;    //Aufhoeren wenn > Listengroesse
        //Reload Book List
        loadBookList();
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
        //Reload Book List
        loadBookList();
    }

    @Override
    @FXML
    public void returnToLogin() {
        if (model.isMainUser() && !model.logout()) View.errorMessage("Logout Denied", "Server refused logout.");
        else {
            setOptions();
            View.loadScene("/login.fxml", stage, model);
        }
    }

    @Override
    public void inspectBook(String isbn) {
        model.loadBook(isbn);
        View.loadScene("/book.fxml", stage, model);
    }

    @Override
    public void editBook(String isbn) {
        model.setEditMode(true);
        inspectBook(isbn);
    }

    @Override
    public void deleteBook(String isbn) {

    }

    @Override
    @FXML
    public void resetDatabase() {
        if(View.confirmMessage("Reset Database", "Do you want to reset the Database?")) model.resetDatabase();
    }
}
