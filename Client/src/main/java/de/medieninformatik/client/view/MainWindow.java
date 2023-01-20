package de.medieninformatik.client.view;

import de.medieninformatik.client.controller.Controller;
import de.medieninformatik.client.model.Model;
import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.Optional;

public class MainWindow extends Application {

    private Stage stage;
    private Scene loginScene;
    private Scene mainScene;
    private Scene bookScene;
    private Controller controller;
    private TextField match;
    private ComboBox<String> choice;
    private Button filterButton;
    private Button resetFilterButton;
    private Button logoutButton;
    private Button createButton;
    private ListView<HBox> list;
    private Button ascending;
    private Button descending;
    private Button normalButton;
    private Button adminButton;
    private HBox bookTitleArea;
    private Button bookLeaveButton;
    private TextArea bookDescriptionField;
    private HBox bookIsbnArea;
    private HBox bookYear;
    private HBox bookPages;
    private HBox bookPublisher;
    private HBox bookRatingBoxes;
    private Label bookRating;
    private HBox bookCategories;
    private HBox bookAuthors;
    private LinkedList<HBox> bookValues = new LinkedList<>();

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
     */
    @Override
    public void start(Stage stage) {
        //Darstellung der Daten fuer den Client
        Model model = new Model();
        Controller controller = new Controller(this, model);
        this.controller = controller;
        this.stage = stage;

        Scene loginScene = createLoginScene();
        this.loginScene = loginScene;
        this.mainScene = createMainScene();
        this.bookScene = createBookScene();

        stage.setScene(loginScene);
        stage.setResizable(true);
        stage.setOnCloseRequest((e) -> controller.exitProgram());
        stage.setTitle("Informatik Viewer v.0.1");
        stage.show();
    }

    private Scene createLoginScene() {
        //Erste Scene: Anmeldung als normaler Nutzer oder Hauptbenutzer
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        box.setPrefSize(1000, 800);

        Button normal = new Button("Normal");
        Button admin = new Button("Admin");

        normal.setStyle("-fx-background-color:#3cb177; -fx-text-fill: black;-fx-font-size: 14px;-fx-padding: 3 30 3 30;");
        admin.setStyle("-fx-background-color:#3c7fb1; -fx-text-fill: black;-fx-font-size: 14px;-fx-padding: 3 30 3 30;");

        normal.setPrefSize(120, 35);
        admin.setPrefSize(120, 35);

        normal.setOnAction((e) -> switchToMainScene());

        admin.setOnAction((e) -> controller.login());
        normal.setOnAction((event -> controller.startup()));

        this.normalButton = normal;
        this.adminButton = admin;

        box.getChildren().add(normal);
        box.getChildren().add(admin);
        Scene scene = new Scene(box);
        return scene;
    }

    public void switchToLoginScene() {
        this.stage.setScene(loginScene);
    }

    private Scene createMainScene() {
        //Ausgewaehlte Buecherliste (Titel Jahr Autor Verlag [Kategorien] Bewertung), als Default von A-Z in Schritten (z.B. 10)
        //Filtereingabe fuer FilterString -> Threshold -> Liste wird angepasst
        //Kategorie Selection -> Submit Button -> Liste wird angepasst
        //Reset Button fuer Filter
        //Hinzufuegen Button fuer Hauptbenutzer
        //Loeschen Button fuer Hauptbenutzer
        //Eintrag bearbeiten Button fuer Hauptbenutzer
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(15);

        HBox upper = new HBox();
        upper.setSpacing(10);
        TextField match = new TextField();
        match.setPrefWidth(200);
        this.match = match;

        final ComboBox<String> choice = new ComboBox<>();
        choice.setPrefWidth(120);
        this.choice = choice;

        Button filter = new Button("Filter");
        this.filterButton = filter;
        filter.setOnAction((event -> controller.updateFilters(match.getText(), choice.getValue())));

        Button reset = new Button("Reset");
        this.resetFilterButton = reset;
        reset.setOnAction((event -> {
            match.setText("");
            choice.setValue("All");
            controller.updateFilters(match.getText(), choice.getValue());
        }));

        Button logout = new Button("Return");
        logout.setStyle("-fx-background-color: #3cb177; -fx-text-fill: white");
        logout.setOnAction((e) -> controller.toLoginView());
        this.logoutButton = logout;

        upper.getChildren().addAll(match, choice, filter, reset, logout);
        box.getChildren().add(0, upper);

        ScrollPane pane = new ScrollPane();
        VBox.setVgrow(pane, Priority.ALWAYS);
        pane.setPadding(new Insets(10, 10, 10, 10));

        ListView<HBox> list = new ListView<>();
        this.list = list;
        list.setFixedCellSize(40);
        list.setPrefWidth(980);
        list.setPrefHeight(680);

        pane.setContent(list);
        box.getChildren().add(1, pane);


        this.ascending = setButtonImage("up_arrow.png", 35);
        ascending.setOnAction((event -> controller.updateOrder("asc")));
        this.descending = setButtonImage("down_arrow.png", 35);
        descending.setOnAction((event -> controller.updateOrder("desc")));

        HBox lower = new HBox();
        lower.setAlignment(Pos.CENTER_LEFT);
        lower.setSpacing(10);
        Button create = new Button("Create");
        create.setPrefHeight(35);
        create.setOnAction((event -> controller.createEntry()));
        create.setVisible(false);
        this.createButton = create;

        lower.getChildren().addAll(ascending, descending, create);

        box.getChildren().add(2, lower);

        box.setPrefSize(1000, 800);

        Scene scene = new Scene(box);
        return scene;
    }

    private Button setButtonImage(String image, int size) {
        ImageView view = new ImageView(image);
        view.setFitHeight(size);
        view.setPreserveRatio(true);
        Button btn = new Button();
        btn.setPadding(new Insets(0));
        btn.setStyle("-fx-background-insets: 0 0 -1 0, 0, 1, 2");
        btn.setPrefHeight(size);
        btn.setGraphic(view);
        return btn;
    }

    public void switchToMainScene() {
        this.stage.setScene(mainScene);
    }

    private Scene createBookScene() {
        //Titel, Autoren, Beschreibung, Bewertung, Jahr, Kategorien, Verlag
        //Kategorie Klick -> Kategorie Selection & Submit
        //Autor Klick -> Unsichtbarer Autor Filter & Submit
        //Loeschen Button fuer Hauptbenutzer
        //Eintrag bearbeiten Button fuer Hauptbenutzer
        VBox box = new VBox();

        box.setSpacing(5);

        HBox titelZeile = new HBox();
        titelZeile.setAlignment(Pos.CENTER_LEFT);
        titelZeile.setSpacing(15);
        titelZeile.setPadding(new Insets(-5, 10, -5, 10));

        Button leave = setButtonImage("back.png", 25);
        leave.setOnAction((event -> controller.toMainView()));
        leave.setFocusTraversable(false);
        this.bookLeaveButton = leave;

        HBox title = getDataField("", "title",1000 - 25);
        title.setPrefHeight(50);
        title.setPadding(new Insets(0));
        title.setStyle(title.getStyle() + "-fx-font-size: 2em");
        this.bookTitleArea = title;
        this.bookValues.add(bookTitleArea);

        titelZeile.getChildren().add(leave);
        titelZeile.getChildren().add(title);

        Separator s = new Separator();
        s.setStyle("-fx-background-color: black");

        HBox data = new HBox();
        data.setSpacing(10);
        data.setAlignment(Pos.CENTER_LEFT);

        data.setStyle("-fx-font-size:1.4em; -fx-text-fill: lightgray;");

        HBox isbn = getDataField("ISBN", "isbn", 200);
        this.bookIsbnArea = isbn;
        this.bookValues.add(bookIsbnArea);

        HBox year = getDataField("Erscheinungsjahr",  "year",200);
        this.bookYear = year;
        this.bookValues.add(year);

        HBox pages = getDataField("Seiten",  "pages",100);
        this.bookPages = pages;
        this.bookValues.add(pages);

        HBox publisher = getDataField("Verlag",  "publisher",300);
        this.bookPublisher = publisher;
        this.bookValues.add(publisher);

        HBox ratingBoxes = buildRatingBoxes(0);
        ratingBoxes.setAlignment(Pos.CENTER_RIGHT);
        ratingBoxes.setSpacing(5);
        this.bookRatingBoxes = ratingBoxes;

        Label rating = new Label();
        rating.setPrefSize(50, 25);
        this.bookRating = rating;

        data.getChildren().addAll(isbn, year, pages, publisher, ratingBoxes, rating);

        Separator s2 = new Separator();
        s2.setStyle("-fx-background-color: black");

        HBox categories = new HBox();
        categories.setSpacing(10);
        categories.setAlignment(Pos.CENTER);
        categories.setStyle("-fx-font-size: 1.2em");
        this.bookCategories = categories;

        Separator s3 = new Separator();
        s3.setStyle("-fx-background-color: black");

        HBox authors = new HBox();
        authors.setSpacing(10);
        authors.setAlignment(Pos.CENTER);
        authors.setStyle("-fx-font-size: 1.2em");
        this.bookAuthors = authors;

        Separator s4 = new Separator();
        s4.setStyle("-fx-background-color: black");

        TextArea description = new TextArea();
        description.setPrefWidth(1000);
        HBox.setHgrow(description, Priority.ALWAYS);
        description.setPrefHeight(500);
        description.setPadding(new Insets(0));
        description.setStyle("-fx-font-size:1.3em; -fx-background-color: inherit; -fx-background-insets: 0;");
        description.setEditable(false);
        description.setFocusTraversable(false);
        this.bookDescriptionField = description;

        box.getChildren().addAll(titelZeile, s, data, s2, categories, s3, authors, s4, description);

        box.setPrefSize(1000, 800);
        Scene scene = new Scene(box);
        return scene;
    }

    private HBox getDataField(String name, String id, int width) {
        String displayName = name + ": ";

        HBox container = new HBox();
        container.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label();
        label.setText(displayName);
        label.setFocusTraversable(false);

        Label value = new Label();
        value.setFocusTraversable(false);
        value.setId(id);

        container.setPrefWidth(width);
        container.getChildren().addAll(label, value);
        return container;
    }

    public void switchToBookScene(Book book) {
        this.stage.setScene(bookScene);
        displayBook(book);
    }

    private void displayBook(Book book) {
        ((Label) this.bookTitleArea.getChildren().get(1)).setText(book.getTitle());
        ((Label) this.bookIsbnArea.getChildren().get(1)).setText(book.getIsbn());
        ((Label) this.bookYear.getChildren().get(1)).setText(String.valueOf(book.getReleaseYear()));
        ((Label) this.bookPages.getChildren().get(1)).setText(String.valueOf(book.getPages()));
        ((Label) this.bookPublisher.getChildren().get(1)).setText(book.getPublisher().getName() + " (" + book.getPublisher().getCountry() + ")");

        this.bookRatingBoxes.getChildren().forEach(c -> setRatingColor((Shape) c, c.getId(), (int) book.getRating())
        );
        this.bookRating.setText(Double.toString(book.getRating()));

        this.bookDescriptionField.setText(book.getDescription());

        setBookCategories(book.getCategories());
        setBookAuthors(book.getAuthors());
    }

    public void setEditMode() {
        setBookCategoryButtons();
        setBookAuthorButtons();
        this.bookDescriptionField.setEditable(true);
        this.bookValues.forEach(value -> {
            Label label = (Label) value.getChildren().get(1);
            if (label.getId() != null && label.getId().equals("publisher"))
                label.setOnMouseClicked((event -> controller.editBookPublisher()));
            else label.setOnMouseClicked((event -> controller.editBookValue(label.getId())));
            label.setStyle(label.getStyle() + "-fx-border-color: green;");
        });
    }

    private void setBookCategories(Category[] categories) {
        this.bookCategories.getChildren().clear();
        for (int i = 0; i < categories.length; i++) {
            HBox container = new HBox();
            container.setSpacing(5);
            Label category = new Label(categories[i].getName());
            container.getChildren().add(category);
            if (i > 0) this.bookCategories.getChildren().add(new Separator(Orientation.VERTICAL));
            this.bookCategories.getChildren().add(container);
        }
    }

    private void setBookAuthors(Author[] authors) {
        this.bookAuthors.getChildren().clear();
        for (int i = 0; i < authors.length; i++) {
            HBox container = new HBox();
            container.setSpacing(5);
            Label author = new Label(authors[i].getFirstName() + " " + authors[i].getLastName());
            container.getChildren().add(author);
            if (i > 0) this.bookAuthors.getChildren().add(new Separator(Orientation.VERTICAL));
            this.bookAuthors.getChildren().add(container);
        }
    }

    private void setBookCategoryButtons() {
        this.bookCategories.getChildren().forEach(child -> {
            if (child.getClass().equals(Separator.class)) return;
            Button remove = setButtonImage("delete.png", 15);
            remove.setFocusTraversable(false);
            HBox c = (HBox) child;
            c.getChildren().add(remove);
        });
    }

    private void setBookAuthorButtons() {
        this.bookAuthors.getChildren().forEach(child -> {
            if (child.getClass().equals(Separator.class)) return;
            Button remove = setButtonImage("delete.png", 15);
            remove.setFocusTraversable(false);
            HBox c = (HBox) child;
            c.getChildren().add(remove);
        });
    }

    public void setNonEditMode() {
        this.bookDescriptionField.setEditable(false);
        this.bookValues.forEach(value -> {
            Label label = (Label) value.getChildren().get(1);
            label.setOnMouseClicked((event -> {
            }));
            label.setStyle(label.getStyle() + "-fx-border-color: none;");
        });
    }

    public void mainUser() {
        this.stage.setTitle(stage.getTitle() + " [Hauptbenutzer]");
        this.logoutButton.setText("Log Out");
        this.createButton.setVisible(true);
        this.logoutButton.setStyle("-fx-background-color: #aa3333; -fx-text-fill: white");
        adminButton.setStyle("-fx-background-color:#95a1a6; -fx-text-fill: black;-fx-font-size: 14px;-fx-padding: 3 30 3 30;");
        infoMessage("Sie sind jetzt der Hauptbenutzer!");
    }

    public void mainUserFail() {
        adminButton.setStyle("-fx-background-color:#aa3333; -fx-text-fill: black;-fx-font-size: 14px;-fx-padding: 3 30 3 30;");
        errorMessage("Client kann nicht als Hauptbenutzer angemeldet werden!");
    }

    public void mainUserLogout() {
        this.stage.setTitle(stage.getTitle().replace(" [Hauptbenutzer]", ""));
        this.logoutButton.setText("Return");
        this.createButton.setVisible(false);
        this.logoutButton.setStyle("-fx-background-color: #3cb177; -fx-text-fill: white");
        normalButton.setStyle("-fx-background-color:#3cb177; -fx-text-fill: black;-fx-font-size: 14px;-fx-padding: 3 30 3 30;");
        adminButton.setStyle("-fx-background-color:#3c7fb1; -fx-text-fill: black;-fx-font-size: 14px;-fx-padding: 3 30 3 30;");
    }

    public LinkedList<HBox> createList(LinkedList<Book> books) {
        LinkedList<HBox> items = new LinkedList<>();
        for (Book book : books) {
            HBox box = new HBox();
            box.setSpacing(10);
            box.setPadding(new Insets(5, 10, 5, 10));
            box.setAlignment(Pos.CENTER);

            Label title = new Label();
            title.setText(book.getTitle() + " (" + book.getReleaseYear() + ")");
            title.setPrefWidth(560);

            Label author = new Label();
            String authorString = (book.getAuthors() == null || book.getAuthors().length == 0) ? "Unknown" :
                    (book.getAuthors()[0].getAlias() + (book.getAuthors().length > 1 ? ",..." : ""));
            author.setText(authorString);
            author.setPrefWidth(100);

            Label publisher = new Label();
            publisher.setText(book.getPublisher().getName());
            publisher.setStyle("-fx-text-fill: darkgray");
            publisher.setPrefWidth(100);

            HBox rating = buildRatingBoxes((int) book.getRating());
            rating.setAlignment(Pos.CENTER_RIGHT);
            rating.setSpacing(5);

            Label concreteRating = new Label();
            concreteRating.setText(Double.toString(book.getRating()));
            concreteRating.setPrefWidth(20);

            Button edit = setButtonImage("edit.png", 15);
            edit.setOnAction((e) -> controller.editBook(book.getIsbn()));
            edit.setPrefWidth(15);
            edit.setVisible(controller.isMainUser());

            Button delete = setButtonImage("delete.png", 15);
            delete.setOnAction((e) -> {
                if (confirmAction("Delete Entry", "Do you want to delete \"" + book.getTitle() + "\" from the Database?"))
                    controller.deleteBook(book.getIsbn());
            });
            delete.prefWidth(15);
            delete.setVisible(controller.isMainUser());

            box.getChildren().addAll(title, author, publisher, rating, concreteRating, edit, delete);

            box.setOnMouseClicked((event -> {
                if (event.getClickCount() > 1) controller.displayBook(book.getIsbn());
            }));

            items.add(box);
        }
        return items;
    }

    private HBox buildRatingBoxes(int r) {
        HBox rating = new HBox();
        Shape shape = new Rectangle(10, 10, r >= 1 ? Color.YELLOW : Color.DARKGRAY);
        Shape shape2 = new Rectangle(10, 10, r >= 2 ? Color.YELLOW : Color.DARKGRAY);
        Shape shape3 = new Rectangle(10, 10, r >= 3 ? Color.YELLOW : Color.DARKGRAY);
        Shape shape4 = new Rectangle(10, 10, r >= 4 ? Color.YELLOW : Color.DARKGRAY);
        Shape shape5 = new Rectangle(10, 10, r == 5 ? Color.YELLOW : Color.DARKGRAY);
        shape.setId("1");
        shape2.setId("2");
        shape3.setId("3");
        shape4.setId("4");
        shape5.setId("5");
        rating.getChildren().add(shape);
        rating.getChildren().add(shape2);
        rating.getChildren().add(shape3);
        rating.getChildren().add(shape4);
        rating.getChildren().add(shape5);
        return rating;
    }

    private void setRatingColor(Shape box, String id, int rating) {
        int p = Integer.parseInt(id);
        box.setFill(rating >= p ? Color.YELLOW : Color.DARKGRAY);
    }

    public void setList(LinkedList<HBox> itemList) {
        ObservableList<HBox> items = FXCollections.observableList(itemList);
        this.list.setItems(items);
    }

    public void setCategoryNames(LinkedList<String> categoryNames) {
        ObservableList<String> options = FXCollections.observableArrayList(categoryNames);
        this.choice.setItems(options);
    }

    public void errorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void infoMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public boolean confirmAction(String action, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Action");
        alert.setHeaderText(action);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    public String editTextMessage(String message, String def) {
        TextInputDialog dialog = new TextInputDialog(def);
        dialog.setResizable(true);
        dialog.setTitle("Edit Value");
        dialog.setHeaderText(message);
        dialog.setContentText(message);
        Optional<String> result = dialog.showAndWait();

        return (result.isEmpty() || result.get().isBlank()) ? def : result.get();
    }

    public<T> T editChoiceMessage(String message, T def, LinkedList<T> options) {
        ChoiceDialog<T> choiceDialog = new ChoiceDialog<T>(def, options);
        choiceDialog.setTitle("Choose Value");
        choiceDialog.setHeaderText("Choose Value");
        choiceDialog.setContentText(message);
        Optional<T> result = choiceDialog.showAndWait();
        return result.isEmpty() ? def : result.get();
    }
}
