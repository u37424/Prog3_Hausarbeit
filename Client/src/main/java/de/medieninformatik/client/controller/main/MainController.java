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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.Locale;

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

    protected String userString;
    protected String userSelection;
    protected boolean ascending;
    protected int pageStart;
    protected int pageSize;
    private int minPageSize;
    private int maxPageSize;
    private int increment;

    @FXML
    public void initialize() {
        //Limits des Spinners setzen
        setPageSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(minPageSize, maxPageSize, pageSize, increment));
        //Onclick eines Listen Items (HBox.getID() == someISBN)
        page.setOnMouseClicked((e) -> {
            if (e.getClickCount() > 1) {
                if (page.getSelectionModel().getSelectedItem() == null) return;
                String id = page.getSelectionModel().getSelectedItem().getId();
                inspectItem(id);
            }
        });

        this.selector.setPrefWidth(0);
        this.selector.setVisible(false);
    }

    public void setup() {
        this.userString = "";
        this.userSelection = "";
        this.ascending = true;
        this.pageStart = 0;
        this.pageSize = 10;
        this.minPageSize = 5;
        this.maxPageSize = 100;
        this.increment = 5;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setModel(MainModel model) {
        this.model = model;
        //On Model received
        //Optionen des Users setzen
        setOptions();

        //Sync Filter
        this.stringInput.setText(userString);
        this.selector.setValue(userSelection);
        this.setPageSize.getValueFactory().setValue(pageSize);
        if (ascending) ((ImageView) orderButton.getGraphic()).setImage(new Image("asc.png"));
        else ((ImageView) orderButton.getGraphic()).setImage(new Image("desc.png"));

        //Set Change Listener for Spinner after loading
        setPageSize.valueProperty().addListener((obs) -> updatePageSize(setPageSize.getValue()));
        //Load BookList
        loadItemList();
    }

    @Override
    public void setSceneController(SceneController controller) {
        this.sceneController = controller;
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

    @Override
    abstract public void loadItemList();

    protected GridPane paneBuilder(int columns, String id) {
        GridPane pane = new GridPane();
        pane.setId(id);
        for (int i = 0; i < columns; i++) {
            if(i == 0)pane.getColumnConstraints().add(new ColumnConstraints(GridPane.USE_COMPUTED_SIZE, 200, GridPane.USE_COMPUTED_SIZE, Priority.ALWAYS, HPos.LEFT , true));
            else pane.getColumnConstraints().add(new ColumnConstraints(GridPane.USE_COMPUTED_SIZE, 100, GridPane.USE_COMPUTED_SIZE, Priority.ALWAYS, HPos.CENTER, true));
        }
        pane.getColumnConstraints().addAll(new ColumnConstraints(GridPane.USE_PREF_SIZE, 20, GridPane.USE_COMPUTED_SIZE, Priority.NEVER, HPos.CENTER, false),
                new ColumnConstraints(GridPane.USE_PREF_SIZE, 20, GridPane.USE_COMPUTED_SIZE, Priority.NEVER, HPos.CENTER, false));
        pane.getRowConstraints().addAll(new RowConstraints(GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE, GridPane.USE_COMPUTED_SIZE, Priority.ALWAYS, VPos.CENTER, true));
        return pane;
    }

    protected TextFlow buildFrontItem(String string) {
        TextFlow pane = new TextFlow();

        pane.setPadding(new Insets(15));
        String checkTitle = string.toLowerCase(Locale.ROOT);
        String checkString = this.userString.toLowerCase(Locale.ROOT);
        int index = checkTitle.indexOf(checkString);
        if (index >= 0) {
            if (index > 0) {
                pane.getChildren().add(new Text(string.substring(0, index)));
            }
            Text fill = new Text(string.substring(index, index + checkString.length()));
            fill.setFill(Color.RED);
            pane.getChildren().add(fill);
            pane.getChildren().add(new Text(string.substring(index + userString.length())));
        } else {
            pane.getChildren().add(new Text(string));
        }
        return pane;
    }

    protected void addButtons(int columnIndex, GridPane pane) {
        Button edit = new Button();
        ImageView edView = new ImageView(new Image("edit.png"));
        edView.setFitWidth(15);
        edView.setFitHeight(15);
        edit.setGraphic(edView);
        edit.setStyle("-fx-background-color:  #73dab4;");
        edit.setPrefSize(15,15);
        edit.setMinSize(15,15);
        edit.setCursor(Cursor.HAND);
        edit.setOnAction((e) -> editItem(pane.getId()));

        Button delete = new Button();
        ImageView delView = new ImageView(new Image("delete.png"));
        delView.setFitWidth(15);
        delView.setFitHeight(15);
        delete.setGraphic(delView);
        delete.setStyle("-fx-background-color: #cc4b4b;");
        delete.setPrefSize(15,15);
        delete.setMinSize(15,15);
        delete.setCursor(Cursor.HAND);
        delete.setOnAction((e) -> deleteItem(pane.getId()));
        pane.addColumn(columnIndex, edit);
        pane.addColumn(columnIndex+1, delete);
    }

    @Override
    public void createItem() {
        model.setCreateMode(true);
    }

    @Override
    @FXML
    abstract public void bookPressed();

    @Override
    @FXML
    abstract public void categoryPressed();

    @Override
    @FXML
    abstract public void authorPressed();

    @Override
    @FXML
    abstract public void publisherPressed();

    @Override
    public void setOrder() {
        if (ascending) ((ImageView) orderButton.getGraphic()).setImage(new Image("desc.png"));
        else ((ImageView) orderButton.getGraphic()).setImage(new Image("asc.png"));
        this.ascending = !this.ascending;
        loadItemList();
    }

    @Override
    public void updatePageSize(int size) {
        //Listengroesse aktualisieren
        pageSize = size;
        loadItemList();
    }

    @Override
    public void pageBackward() {
        if (pageStart - pageSize >= 0) this.pageStart -= pageSize;
        //Reload Book List
        loadItemList();
    }

    @Override
    abstract public void pageForward();

    @Override
    public void updateFilter() {
        String string = stringInput.getText();
        String category = selector.getValue();
        //Only reload on filter change
        if (string == null) string = "";
        if (category == null) category = "";

        if (userString.equals(string) && userSelection.equals(category)) return;
        this.userString = string;
        this.userSelection = category;
        loadItemList();
    }

    @Override
    public void resetFilter() {
        updateFilter();
        if (userString.isBlank() && userSelection.equals("")) return;
        this.userString = "";
        this.userSelection = "";
        this.stringInput.setText("");
        this.selector.setValue("");
        loadItemList();
    }

    @Override
    public void returnToLogin() {
        if (model.isMainUser() && !model.logout())
            sceneController.errorMessage("Logout Denied", "Server refused logout.");
        else {
            setOptions();
            sceneController.loadLoginScene();
        }
    }

    @Override
    abstract public void inspectItem(String id);

    @Override
    public void editItem(String id) {
        model.setEditMode(true);
        inspectItem(id);
    }

    @Override
    abstract public void deleteItem(String id);

    @Override
    public void resetDatabase() {
        if (sceneController.confirmMessage("Reset Database", "Do you want to reset the Database?")) {
            if (model.resetDatabase()) {
                sceneController.infoMessage("Database Reset", "Succeeded!");
                resetFilter();
                loadItemList();
            } else sceneController.errorMessage("Database Reset", "Database Reset failed!");
        }
    }
}
