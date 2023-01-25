package de.medieninformatik.client.view;

import de.medieninformatik.client.controller.LoginController;
import de.medieninformatik.client.model.MainModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

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
        Parent loginRoot = loginLoader.load();
        LoginController controller = loginLoader.getController();

        Scene loginScene = new Scene(loginRoot);
        Stage stage = new Stage();
        stage.setScene(loginScene);

        controller.setStage(stage);
        controller.setModel(new MainModel());

        stage.setTitle("Benutzerauswahl");
        stage.show();
    }

    public static void errorMessage(String resource_error, String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(resource_error);
            alert.setContentText(message);
            alert.showAndWait();
    }
}
