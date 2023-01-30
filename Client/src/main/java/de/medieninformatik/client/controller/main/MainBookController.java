package de.medieninformatik.client.controller.main;

import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.Locale;

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
                HBox hBox = new HBox();
                hBox.setId(book.getIsbn());
                hBox.setSpacing(10);
                hBox.setAlignment(Pos.CENTER_LEFT);

                TextFlow title = buildTitle(book);
                Label publisher = new Label(book.getPublisher().getName());
                Label author = new Label(book.getAuthors().get(0).getAlias());

                hBox.getChildren().addAll(title, publisher, author);

                list.add(hBox);
        }

        return list;
    }

    private TextFlow buildTitle(Book book) {
        TextFlow textFlow = new TextFlow();
        String title = book.getTitle();
        String checkTitle = title.toLowerCase(Locale.ROOT);
        String checkString = this.userString.toLowerCase(Locale.ROOT);
        int index = checkTitle.indexOf(checkString);
        if (index >= 0) {
            if (index > 0) {
                textFlow.getChildren().add(new Text(title.substring(0, index)));
            }
            Text fill = new Text(title.substring(index, index+checkString.length()));
            fill.setFill(Color.RED);
            textFlow.getChildren().add(fill);
            textFlow.getChildren().add(new Text(title.substring(index + userString.length())));
        } else {
            textFlow.getChildren().add(new Text(title));
        }
        return textFlow;
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
        model.getBookRequest().getBook(id);
        sceneController.loadBookViewScene();
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
