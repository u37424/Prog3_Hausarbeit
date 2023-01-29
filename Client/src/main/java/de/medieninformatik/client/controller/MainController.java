package de.medieninformatik.client.controller;

import de.medieninformatik.client.interfaces.IController;
import de.medieninformatik.client.interfaces.IMainController;
import de.medieninformatik.client.model.MainModel;
import de.medieninformatik.client.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
    protected ListView<HBox> page;

    protected Stage stage;
    protected MainModel model;
    protected SceneController sceneController;

    protected String userString;
    protected String userSelection;
    protected boolean ascending;
    protected int pageStart;
    protected int pageSize;

    @FXML
    public void initialize() {
        this.userString = "";
        this.userSelection = "";
        this.ascending = true;
        this.pageStart = 0;
        this.pageSize = 5;
        //Limits des Spinners setzen
        setPageSize.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 100, 5, 5));

        //Onclick eines Listen Items (HBox.getID() == someISBN)
        page.setOnMouseClicked((e) -> {
            if (e.getClickCount() > 1) {
                if (page.getSelectionModel().getSelectedItem() == null) return;
                String id = page.getSelectionModel().getSelectedItem().getId();
                inspectItem(id);
            }
        });
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
    public void setSceneController(SceneController controller){
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
        this.returnButton.setVisible(true);
        if (mainUser) this.returnButton.setText("Log Out");
        else this.returnButton.setText("Return");
    }

    @Override
    abstract public void loadItemList();

    @Override
    public void createItem() {
        model.setCreateMode(true);
    }

    @Override
    abstract public void bookPressed();

    @Override
    abstract public void categoryPressed();

    @Override
    abstract public void authorPressed();

    @Override
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
        if (model.isMainUser() && !model.logout()) sceneController.errorMessage("Logout Denied", "Server refused logout.");
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
            } else sceneController.errorMessage("Database Reset", "Database Reset failed!");
        }
    }
}
