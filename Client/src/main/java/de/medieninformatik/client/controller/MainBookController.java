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

public class MainBookController extends MainController {
    @FXML
    public void initialize() {
        super.initialize();
        bookButton.setStyle(bookButton.getStyle() + "-fx-background-color: #5dc367;");
        //Default Category Selection
        selector.getItems().add("");
    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.setTitle("Book List");
    }

    public void setModel(MainModel model) {
        super.setModel(model);
        //Specific selection for books
        loadCategorySelection();
    }

    @Override
    public void setOptions() {
        super.setOptions();
        this.selector.setPrefWidth(150);
        this.selector.setVisible(true);
    }

    private void loadCategorySelection() {
        model.getCategoryRequest().loadAll();
        if (model.getCategoryRequest().getCategories() == null ||
                model.getCategoryRequest().getCategories().size() == 0)
            return;

        for (Category category : model.getCategoryRequest().getCategories())
            this.selector.getItems().add(category.getName());
    }

    @Override
    public void loadItemList() {
        //Aktuelle Daten laden
        model.getBookRequest().loadSelection(pageStart, pageSize, ascending, userString, userSelection);
        //Liste aus den Daten erstellen
        ObservableList<HBox> list = FXCollections.observableArrayList(buildBookItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<HBox> buildBookItems() {
        LinkedList<HBox> list = new LinkedList<>(); //Baue einen HBox Container pro Buch
        //Liste aus den Daten erstellen
        LinkedList<Book> books = model.getBookRequest().getBooks();
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
    public void createItem() {
        super.createItem();
        model.resetSelection();
        sceneController.loadBookViewScene();
    }

    @Override
    public void bookPressed() {
        createItem();
    }

    @Override
    public void categoryPressed() {

    }

    @Override
    public void authorPressed() {

    }

    @Override
    public void publisherPressed() {

    }

    @Override
    public void pageForward() {
        if (pageStart + pageSize < model.getBookRequest().getBookMax()) this.pageStart += pageSize;
        //Reload Book List
        loadItemList();
    }

    @Override
    public void inspectItem(String id) {
        model.getBookRequest().getBook(id);
        sceneController.loadBookViewScene();
    }

    @Override
    public void deleteItem(String id) {
        if (sceneController.confirmMessage("Delete Book", "Do you really want to delete " + model.getBookRequest().getSelection().getIsbn() + " ?")) {
            if (model.getBookRequest().deleteBook(id)) sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
