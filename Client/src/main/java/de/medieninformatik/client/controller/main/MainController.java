package de.medieninformatik.client.controller.main;

import de.medieninformatik.client.controller.SceneController;
import de.medieninformatik.client.interfaces.IMainController;
import de.medieninformatik.client.model.MainModel;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Locale;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse implementiert einen abstrakten MainController, von dem alle weiteren MainController abgeleitet werden koennen.
 * Die Klasse vereint alle wichtigen Funktionen, die alle MainController besitzen sollten.
 * Diese Funktionen koennen unabhaengig von der konkreten Implementation eingebunden werden.
 * Von der konkreten Implementation abhaengige Funktionen muessen von den daraus abgeleiteten Klassen eingebunden werden.
 * Ebenso werden Funktionen bereitgestellt, die von allen Subklassen verwendet werden koennen.
 * </p>
 */
public abstract class MainController implements IMainController {
    @FXML
    protected Button returnButton, pageBackward, pageForward, filterButton, revertButton, orderButton;

    @FXML
    protected Spinner<Integer> setPageSize;

    @FXML
    protected Button bookButton, categoryButton, authorButton, publisherButton, databaseButton;

    @FXML
    protected TextField stringInput;

    @FXML
    protected ChoiceBox<String> selector;

    @FXML
    protected ListView<GridPane> page;

    protected Stage stage;
    protected MainModel model;
    protected SceneController sceneController;

    //Filter des Benutzers, gespeichert im Controller, um wiederverwendet werden zu koennen
    protected String userString, userSelection;
    protected boolean ascending;
    protected int pageStart;
    protected int pageSize;
    private int minPageSize, maxPageSize, increment;

    /**
     * Wird beim Laden der FXML Datei aufgerufen, um initiale Einstellungen beim Laden des Controllers zu machen.
     */
    @FXML
    public void initialize() {
        //Limits des Spinners setzen
        setPageSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(minPageSize, maxPageSize, pageSize, increment));
        //Onclick eines Listen Items (HBox id == Object ID)
        page.setOnMouseClicked((e) -> {
            if (e.getClickCount() <= 1) return;
            if (page.getSelectionModel().getSelectedItem() == null) return;
            String id = page.getSelectionModel().getSelectedItem().getId();
            inspectItem(id);
        });

        //Choice Box als Default ausschalten
        this.selector.setPrefWidth(0);
        this.selector.setVisible(false);
    }

    /**
     * Wird anstatt des Konstruktors benutzt, um den Controller einmalig beim ersten Erstellen mit initialen Werten zu befuellen.
     *
     * @return MainController mit befuellten Daten
     */
    public MainController setup() {
        //Filter initialisieren
        this.userString = "";
        this.userSelection = "";
        this.ascending = true;
        this.pageStart = 0;
        this.pageSize = 10;
        this.minPageSize = 5;
        this.maxPageSize = 100;
        this.increment = 5;
        return this;
    }

    /**
     * Setzt das entsprechende Model, welches vom Controller verwendet werden soll.
     *
     * @param model Model, welches verwendet werden soll
     */
    public void setModel(MainModel model) {
        this.model = model;
        setOptions();

        //Sync Filter
        this.stringInput.setText(userString);
        this.selector.setValue(userSelection);
        this.setPageSize.getValueFactory().setValue(pageSize);
        //Setze Order Button
        ((ImageView) orderButton.getGraphic()).setImage(new Image(ascending ? "asc.png" : "desc.png"));

        //Change Listener fuer Spinner
        setPageSize.valueProperty().addListener((obs) -> updatePageSize(setPageSize.getValue()));
        //Liste laden
        loadItemList();
    }

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Setzt die Instanz des entsprechenden SceneControllers.
     *
     * @param sceneController Instanz des SceneControllers
     */
    @Override
    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    /**
     * Setzt die Optionen, die der Benutzer entsprechend zu seinem Benutzerstatus im Hauptfenster haben soll.
     */
    public void setOptions() {
        boolean mainUser = model.isMainUser();
        //Setzen der Optionen des Hauptnutzers
        this.databaseButton.setVisible(mainUser);
        this.bookButton.setVisible(mainUser);
        this.categoryButton.setVisible(mainUser);
        this.authorButton.setVisible(mainUser);
        this.publisherButton.setVisible(mainUser);
        this.returnButton.setText(mainUser ? "Log Out" : "Return");
    }

    /**
     * Laedt die aktuellen Elemente der darzustellenden Liste und zeigt sie an.
     * Die Liste kann auch nur einen gewissen Ausschnitt einer groesseren Liste darstellen.
     */
    @Override
    abstract public void loadItemList();

    /**
     * Baut eine GridPane zur Darstellung von Daten in einer Zeile der Liste.
     * Die Zeile kann beliebig viele Daten enthalten.
     * Jede Zeile enthaelt zwei Platzhalter-Spalten fuer zuschaltbare Edit und Delete Buttons.
     *
     * @param columns Spalten, die in der zeile enthalten sein sollen
     * @param id      ID der Zeile, die damit assoziiert wird
     * @return einzeilige GridPane mit allen geforderten Spalten
     */
    protected GridPane listPaneBuilder(int columns, String id) {
        GridPane pane = new GridPane();
        pane.setId(id);

        //Geforderte Spalten erstellen
        for (int i = 0; i < columns; i++) {
            if (i == 0)
                pane.getColumnConstraints().add(new ColumnConstraints(GridPane.USE_COMPUTED_SIZE, 200, GridPane.USE_COMPUTED_SIZE, Priority.ALWAYS, HPos.LEFT, true));
            else
                pane.getColumnConstraints().add(new ColumnConstraints(GridPane.USE_COMPUTED_SIZE, 100, GridPane.USE_COMPUTED_SIZE, Priority.ALWAYS, HPos.CENTER, true));
        }

        //Spalten fuer Edit und Delete
        pane.getColumnConstraints().addAll(
                new ColumnConstraints(GridPane.USE_PREF_SIZE, 20, GridPane.USE_COMPUTED_SIZE, Priority.NEVER, HPos.CENTER, false),
                new ColumnConstraints(GridPane.USE_PREF_SIZE, 20, GridPane.USE_COMPUTED_SIZE, Priority.NEVER, HPos.CENTER, false)
        );

        pane.getRowConstraints().addAll(new RowConstraints(GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        return pane;
    }

    /**
     * Baut ein filterbares Text Item. Das item stellt einen Text dar.
     * Ist in dem Text der eingestellte String-Filter vorhanden, so wird er rot markiert.
     * Es wird nur das erste Aufkommen des Filters markiert.
     *
     * @param item String, der als text-Item dargestellt werden soll
     * @return TextFlow mit entsprechend markierten Text-Zonen
     */
    protected TextFlow buildFrontItem(String item) {
        TextFlow pane = new TextFlow();

        pane.setPadding(new Insets(15));
        //Finde Filter im Titel
        String lItem = item.toLowerCase(Locale.ROOT);
        String match = this.userString.toLowerCase(Locale.ROOT);
        int index = lItem.indexOf(match);

        //Filter im Text
        if (index >= 0) {
            if (index > 0) pane.getChildren().add(new Text(item.substring(0, index)));

            Text fill = new Text(item.substring(index, index + match.length()));
            fill.setFill(Color.RED);
            pane.getChildren().addAll(fill, new Text(item.substring(index + userString.length())));
        } else pane.getChildren().add(new Text(item));

        return pane;
    }

    /**
     * Schaltet die entsprechenden Edit und Delete Buttons einer Zeile in den entsprechenden Spalten hinzu.
     *
     * @param pane einzeilige GridPane, in der die Buttons eingeschaltet werden sollen
     */
    protected void enableEditorButtons(GridPane pane) {
        Button edit = new Button();
        ImageView edView = new ImageView(new Image("edit.png"));
        edView.setFitWidth(15);
        edView.setFitHeight(15);
        edit.setGraphic(edView);
        edit.setStyle("-fx-background-color:  #73dab4;");
        edit.setPrefSize(15, 15);
        edit.setMinSize(15, 15);
        edit.setCursor(Cursor.HAND);
        edit.setOnAction((e) -> editItem(pane.getId()));

        Button delete = new Button();
        ImageView delView = new ImageView(new Image("delete.png"));
        delView.setFitWidth(15);
        delView.setFitHeight(15);
        delete.setGraphic(delView);
        delete.setStyle("-fx-background-color: #cc4b4b;");
        delete.setPrefSize(15, 15);
        delete.setMinSize(15, 15);
        delete.setCursor(Cursor.HAND);
        delete.setOnAction((e) -> deleteItem(pane.getId()));

        pane.addColumn(pane.getColumnCount() - 2, edit);
        pane.addColumn(pane.getColumnCount() - 1, delete);
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer einen neuen Eintrag erstellen will.
     */
    @Override
    public void createItem() {
        model.setCreateMode(true);
    }

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Buch" Schaltflaeche benutzt.
     */
    @Override
    @FXML
    abstract public void bookPressed();

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Category" Schaltflaeche benutzt.
     */
    @Override
    @FXML
    abstract public void categoryPressed();

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Author" Schaltflaeche benutzt.
     */
    @Override
    @FXML
    abstract public void authorPressed();

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Publisher" Schaltflaeche benutzt.
     */
    @Override
    @FXML
    abstract public void publisherPressed();

    /**
     * Veraendert die Sortierreihenfolge der Liste.
     */
    @Override
    public void setOrder() {
        ((ImageView) orderButton.getGraphic()).setImage(new Image(ascending ? "desc.png" : "asc.png"));
        this.ascending = !this.ascending;
        loadItemList();
    }

    /**
     * Setzt eine neue Listengroesse der anzuzeigenden Liste.
     *
     * @param size Neue Groesse der Liste
     */
    @Override
    public void updatePageSize(int size) {
        pageSize = size;
        loadItemList();
    }

    /**
     * Setzt den Anfang der Liste zurueck an den Anfang.
     */
    private void resetPage() {
        this.pageStart = 0;
    }

    /**
     * Setzt einen neuen Ansichtsbereich der Liste, der vor dem aktuellen Ansichtsbereich liegt.
     */
    @Override
    public void pageBackward() {
        if (pageStart - pageSize >= 0) this.pageStart -= pageSize;
        loadItemList();
    }

    /**
     * Setzt einen neuen Ansichtsbereich der Liste, der hinter dem aktuellen Ansichtsbereich liegt.
     */
    @Override
    abstract public void pageForward(); //Braucht spezifische Result Size

    /**
     * Aktualisiert alle Parameter, die fuer das Anzeigen der Liste verwendet werden.
     * Die Filter entsprechen nach Aufruf den aktuellen Werten, die vom Benutzer eingestellt wurden.
     */
    @Override
    public void updateFilter() {
        String string = stringInput.getText();
        String category = selector.getValue();
        if (string == null) string = "";
        if (category == null) category = "";

        //Nur neu laden, wenn es einen Unterschied gibt
        if (userString.equals(string) && userSelection.equals(category)) return;
        this.userString = string;
        this.userSelection = category;
        resetPage();
        loadItemList();
    }

    /**
     * Aktualisiert alle Parameter, die fuer das Anzeigen der Liste verwendet werden.
     * Die Filter entsprechen nach Aufruf den voreingestellten Werten.
     */
    @Override
    public void resetFilter() {
        updateFilter();
        if (userString.isBlank() && userSelection.equals("")) return;
        this.userString = "";
        this.userSelection = "";
        this.stringInput.setText("");
        this.selector.setValue("");
        resetPage();
        loadItemList();
    }

    /**
     * Fuehrt alle notwendigen Aktionen aus, um es dem Benutzer zu ermoeglichen, zum Login Prozess zurueckzukehren.
     * Wenn ein moeglicher Logout nicht stattfinden kann, wird der Benutzer auch nicht abgemeldet.
     */
    @Override
    public void returnToLogin() {
        if (model.isMainUser() && !model.logout())
            sceneController.errorMessage("Logout Denied", "Server refused logout.");
        else sceneController.loadLoginScene();
    }

    /**
     * Leitet alle notwendigen Aktionen ein, um ein spezifisches Element aus der Liste zu betrachten.
     *
     * @param id ID des Elementes, welches betrachtet werden soll.
     */
    @Override
    abstract public void inspectItem(String id);

    /**
     * Leitet alle notwendigen Aktionen ein, um ein spezifisches Element aus der Liste zu editieren.
     *
     * @param id ID des Elementes, welches editiert werden soll.
     */
    @Override
    public void editItem(String id) {
        model.setEditMode(true);
        inspectItem(id);
    }

    /**
     * Leitet alle notwendigen Aktionen ein, um ein spezifisches Element aus der Liste zu loeschen.
     *
     * @param id ID des Elementes, welches geloescht werden soll.
     */
    @Override
    abstract public void deleteItem(String id);

    /**
     * Leitet einen Datenbank-Reset beim Server ein.
     */
    @Override
    public void resetDatabase() {
        if (!sceneController.confirmMessage("Reset Database", "Do you want to reset the Database?")) return;

        if (model.getUserModel().resetDatabase()) {
            resetFilter();  //Fuer eine saubere Anzeige des Resets
            sceneController.infoMessage("Database Reset", "Succeeded!");
        } else sceneController.errorMessage("Database Reset", "Database Reset failed!");
    }
}
