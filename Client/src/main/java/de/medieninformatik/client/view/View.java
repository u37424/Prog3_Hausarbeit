package de.medieninformatik.client.view;

import de.medieninformatik.client.controller.LoginController;
import de.medieninformatik.client.interfaces.IController;
import de.medieninformatik.client.model.MainModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class View extends Application {
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Lade Login Scene
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        IController controller = new LoginController();
        loginLoader.setController(controller);
        Parent loginRoot = loginLoader.load();

        Scene loginScene = new Scene(loginRoot);
        Stage stage = new Stage();
        stage.setScene(loginScene);
        controller.setStage(stage);
        MainModel model = new MainModel();
        controller.setModel(model);

        stage.setOnCloseRequest(e -> {
            if (model.isMainUser()) model.logout();
        });

        stage.show();
    }

    public static void infoMessage(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info Message");
        alert.setHeaderText(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void errorMessage(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText(type);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean confirmMessage(String type, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(type);
        alert.setContentText(message);
        Optional<ButtonType> res = alert.showAndWait();
        return res.isPresent() && res.get().equals(ButtonType.OK);
    }

    public static void loadScene(String resource, Stage stage, MainModel model, IController controller) {
        try {
            FXMLLoader loader = new FXMLLoader(View.class.getResource(resource));
            loader.setController(controller);
            Parent parent = loader.load();
            setSceneParameters(stage, model, controller, parent);
        } catch (IOException | RuntimeException e) {
            e.printStackTrace();
            View.errorMessage("Resource Error", "Fehler beim laden von Resource: " + resource);
        }
    }

    private static void setSceneParameters(Stage stage, MainModel model, IController controller, Parent parent) {
        controller.setStage(stage);
        controller.setModel(model);

        double width = stage.getWidth();
        double height = stage.getHeight();

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
    }
}
