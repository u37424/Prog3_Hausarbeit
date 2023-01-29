package de.medieninformatik.client.view;

import de.medieninformatik.client.controller.LoginController;
import de.medieninformatik.client.controller.SceneController;
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
        Stage stage = new Stage();
        MainModel model = new MainModel();
        SceneController controller = new SceneController();
        controller.setStage(stage);
        controller.setModel(model);
        controller.loadLoginScene();

        stage.setOnCloseRequest(e -> {
            if (model.isMainUser()) model.logout();
        });
        stage.show();
    }
}
