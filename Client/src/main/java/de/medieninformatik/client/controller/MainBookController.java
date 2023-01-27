package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.IController;
import de.medieninformatik.client.interfaces.IMainController;
import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.client.view.View;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.LinkedList;

public class MainBookController implements IMainController {
    @FXML
    Button returnButton, pageBackward, pageForward, filterButton, revertButton, orderButton;

    @FXML
    Spinner<Integer> setPageSize;

    @FXML
    Button bookButton, categoryButton, authorButton, publisherButton, databaseButton;

    @FXML
    TextField stringInput;

    @FXML
    ChoiceBox<String> selector;

    @FXML
    ListView<HBox> page;

    private Stage stage;
    private MainModel model;

    @FXML
    public void initialize() {
        bookButton.setStyle(bookButton.getStyle()+"-fx-background-color: #5dc367;");
        //Default Category Selection
        selector.getItems().add("");
        //Limits des Spinners setzen
        setPageSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 5, 5));

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
        //On Stage received
        this.stage.setTitle("List Viewer");
    }

    public void setModel(MainModel model) {
        this.model = model;
        //On Model received
        //Optionen des Users setzen
        setOptions();

        //Sync Filter
        this.stringInput.setText(model.getUserString());
        this.selector.setValue(model.getUserCategory());
        this.setPageSize.getValueFactory().setValue(model.getPageSize());
        if (model.isAscending()) ((ImageView) orderButton.getGraphic()).setImage(new Image("asc.png"));
        else ((ImageView) orderButton.getGraphic()).setImage(new Image("desc.png"));

        //Set Change Listener for Spinner after loading
        setPageSize.valueProperty().addListener((obs) -> updatePageSize(setPageSize.getValue()));

        loadCategorySelection();
        //Load BookList
        loadBookList();
    }

    public void setOptions() {
        boolean mainUser = model.isMainUser();
        //Setzen der Optionen des Hauptnutzers
        this.databaseButton.setVisible(mainUser);
        this.bookButton.setVisible(mainUser);
        this.categoryButton.setVisible(mainUser);
        this.authorButton.setVisible(mainUser);
        this.publisherButton.setVisible(mainUser);
        if (mainUser) this.returnButton.setText("Log Out");
        else this.returnButton.setText("Return");
    }

    private void loadCategorySelection() {
        model.loadCategoryList();
        if (model.getCategories() == null || model.getCategories().size() == 0) return;
        for (Category category : model.getCategories()) this.selector.getItems().add(category.getName());
    }

    @Override
    public void loadBookList() {
        //Aktuelle Daten laden
        model.loadBookList();
        //Liste aus den Daten erstellen
        ObservableList<HBox> list = FXCollections.observableArrayList(buildBookItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<HBox> buildBookItems() {
        LinkedList<HBox> list = new LinkedList<>(); //Baue einen HBox Container pro Buch
        //Liste aus den Daten erstellen
        LinkedList<Book> books = model.getBooks();
        if (books == null || books.size() == 0) return list;
        //List HBox
        for (Book book : books) {
            HBox hbox = new HBox();
            hbox.setId(book.getIsbn());
            Label title = new Label(book.getTitle());
            hbox.getChildren().add(title);
            list.add(hbox);
        }
        return list;
    }

    @Override
    public void createBook() {
        model.setCrateMode(true);
        model.resetSelection();
        View.loadScene("/book.fxml", stage, model, new BookViewController());
    }

    @Override
    public void bookPressed() {
        createBook();
    }

    @Override
    public void categoryPressed() {
        switchController(new MainCategoryController());
    }

    @Override
    public void authorPressed() {

    }

    @Override
    public void publisherPressed() {

    }

    public void switchController(IController controller) {
        View.loadScene("/main.fxml", stage, model, controller);
    }

    @Override
    public void setOrder() {
        if (model.isAscending()) ((ImageView) orderButton.getGraphic()).setImage(new Image("desc.png"));
        else ((ImageView) orderButton.getGraphic()).setImage(new Image("asc.png"));
        model.setAscending(!model.isAscending());
        loadBookList();
    }

    @Override
    public void updatePageSize(int size) {
        //Listengroesse aktualisieren
        model.setPageSize(size);
        loadBookList();
    }

    @Override
    public void pageBackward() {
        model.pageBackward();
        //Reload Book List
        loadBookList();
    }

    @Override
    public void pageForward() {
        model.pageForward();
        //Reload Book List
        loadBookList();
    }

    @Override
    public void updateFilter() {
        //Only reload on filter change
        if (model.updateFilters(stringInput.getText(), selector.getValue()))
            loadBookList();
    }

    @Override
    public void resetFilter() {
        updateFilter();
        if (model.resetFilters()) {
            this.stringInput.setText("");
            this.selector.setValue("");
            loadBookList();
        }
    }

    @Override
    public void returnToLogin() {
        if (model.isMainUser() && !model.logout()) View.errorMessage("Logout Denied", "Server refused logout.");
        else {
            setOptions();
            View.loadScene("/login.fxml", stage, model, new LoginController());
        }
    }

    @Override
    public void inspectBook(String isbn) {
        model.loadBook(isbn);
        View.loadScene("/book.fxml", stage, model, new BookViewController());
    }

    @Override
    public void editBook(String isbn) {
        model.setEditMode(true);
        inspectBook(isbn);
    }

    @Override
    public void deleteBook(String isbn) {
        if (View.confirmMessage("Delete Book", "Do you really want to delete " + model.getSelection().getIsbn() + " ?")) {
            if (model.deleteBook(isbn)) View.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else View.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }

    @Override
    public void resetDatabase() {
        if (View.confirmMessage("Reset Database", "Do you want to reset the Database?")) {
            if (model.resetDatabase()) {
                View.infoMessage("Database Reset", "Succeeded!");
                resetFilter();
            } else View.errorMessage("Database Reset", "Database Reset failed!");
        }
    }
}
