package de.medieninformatik.client.view;

import de.medieninformatik.client.controller.Controller;
import de.medieninformatik.client.model.Model;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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
    private TextArea info;
    private ListView<HBox> list;

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
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage stage) throws Exception {
        //Darstellung der Daten fuer den Client
        Model model = new Model();
        Controller controller = new Controller(this, model);
        this.controller = controller;
        this.stage = stage;

        Scene loginScene = createLoginScene();
        this.loginScene = loginScene;
        Scene mainScene = createMainScene();
        this.mainScene = mainScene;
        Scene bookScene = createBookScene();
        this.bookScene = bookScene;

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

        normal.setOnAction((e) -> switchToMainScene());

        admin.setOnAction((e) -> controller.login(admin, e));

        box.getChildren().add(normal);
        box.getChildren().add(admin);
        Scene scene = new Scene(box);
        return scene;
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

        Button reset = new Button("Reset");
        this.resetFilterButton = reset;

        Button logout = new Button("Log Out");
        logout.setStyle("-fx-background-color: #aa3333; -fx-text-fill: white");
        logout.setOnAction((e) -> controller.exitProgram());
        logout.setVisible(false);
        this.logoutButton = logout;

        upper.getChildren().add(match);
        upper.getChildren().add(choice);
        upper.getChildren().add(filter);
        upper.getChildren().add(reset);
        upper.getChildren().add(logout);
        box.getChildren().add(0, upper);

        ScrollPane pane = new ScrollPane();
        VBox.setVgrow(pane, Priority.ALWAYS);
        pane.setPadding(new Insets(10,10,10,10));

        ListView<HBox> list = new ListView<>();
        this.list = list;
        list.setFixedCellSize(40);
        list.setPrefWidth(980);
        list.setPrefHeight(680);

        pane.setContent(list);
        box.getChildren().add(1, pane);

        HBox lower = new HBox();
        lower.setAlignment(Pos.CENTER_LEFT);
        lower.setSpacing(10);
        Button create = new Button("Create");
        create.setPrefHeight(35);
        this.createButton = create;

        TextArea info = new TextArea();
        info.setEditable(false);
        info.setPrefSize(500, 15);
        info.setStyle("-fx-background-color: #AAAAFF");
        this.info = info;

        lower.getChildren().add(create);
        lower.getChildren().add(info);
        box.getChildren().add(2, lower);

        Button testBook = new Button("Book View");
        testBook.setPrefHeight(35);
        testBook.setOnAction((e) -> this.stage.setScene(bookScene));
        lower.getChildren().add(testBook);

        box.setPrefSize(1000, 800);

        Scene scene = new Scene(box);
        return scene;
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

        TextArea title = new TextArea();
        title.setText("My test Book.");
        title.setPrefHeight(50);
        title.setStyle("-fx-font-size:2em; -fx-focus-color: -fx-control-inner-background ; -fx-faint-focus-color: -fx-control-inner-background ;");

        box.getChildren().add(title);



        box.setPrefSize(1000, 800);
        Scene scene = new Scene(box);
        return scene;
    }

    public void switchToBookScene() {
        this.stage.setScene(bookScene);
    }

    public void mainUser() {
        this.stage.setTitle(stage.getTitle() + " [Hauptbenutzer]");
        this.logoutButton.setVisible(true);
    }

    public void setList(ObservableList<HBox> items){
        this.list.setItems(items);
    }
}
