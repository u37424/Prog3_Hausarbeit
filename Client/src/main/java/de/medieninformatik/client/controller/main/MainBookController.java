package de.medieninformatik.client.controller.main;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.LinkedList;

public class MainBookController extends MainController {
    @FXML
    @Override
    public void initialize() {
        super.initialize();
        bookButton.setStyle(bookButton.getStyle() + "-fx-background-color: #5dc367;");
        //Default Category Selection
        selector.getItems().add("");
        this.selector.setPrefWidth(150);
        this.selector.setVisible(true);
    }

    @Override
    public void setStage(Stage stage) {
        super.setStage(stage);
        stage.setTitle("Book List");
    }

    public void setModel(MainModel model) {
        super.setModel(model);
        //Specific filter for books
        loadCategorySelection();
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
        ObservableList<GridPane> list = FXCollections.observableArrayList(buildBookItems());
        //Elemente in die ListView einsetzen
        page.setItems(list);
    }

    private LinkedList<GridPane> buildBookItems() {
        LinkedList<GridPane> list = new LinkedList<>(); //Baue einen HBox Container pro Buch
        //Liste aus den Daten erstellen
        LinkedList<Book> books = model.getBookRequest().getBooks();
        if (books == null || books.size() == 0) return list;
        //List HBox
        for (Book book : books) {
            GridPane pane = paneBuilder();
            TextFlow flow = buildFrontItem(book.getTitle());
            Label label2 = new Label("label");
            Label label3 = new Label("label");
            Label label4 = new Label("label");
            pane.addColumn(0, flow);
            pane.addColumn(1, label2);
            pane.addColumn(2, label3);
            pane.addColumn(3, label4);
            list.add(pane);
        }

        return list;
    }

    @Override
    public void createItem() {
        super.createItem();
        model.getBookRequest().reset();
        sceneController.loadBookViewScene();
    }

    @Override
    public void bookPressed() {
        createItem();
    }

    @Override
    public void categoryPressed() {
        sceneController.loadMainCategoryScene();
    }

    @Override
    public void authorPressed() {
        sceneController.loadMainAuthorScene();
    }

    @Override
    public void publisherPressed() {
        sceneController.loadMainPublisherScene();
    }

    @Override
    public void pageForward() {
        if (pageStart + pageSize < model.getBookRequest().getMax()) this.pageStart += pageSize;
        //Reload List
        loadItemList();
    }

    @Override
    public void inspectItem(String id) {
        try {
            model.getBookRequest().getBook(id);
            sceneController.loadBookViewScene();
        } catch (NumberFormatException e) {
            sceneController.errorMessage("Parsing Error", "Item ID cannot be read.");
        }
    }

    @Override
    public void deleteItem(String id) {
        if (sceneController.confirmMessage("Delete Book", "Do you really want to delete " + model.getBookRequest().getSelection().getTitle() + " ?")) {
            if (model.getBookRequest().deleteBook(id))
                sceneController.infoMessage("Deletion Succeeded", "The Entry was deleted!");
            else sceneController.errorMessage("Deletion Failed", "Failed to delete the Entry!");
        }
    }
}
