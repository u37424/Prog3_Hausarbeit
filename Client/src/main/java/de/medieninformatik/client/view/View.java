package de.medieninformatik.client.view;

import de.medieninformatik.client.controller.SceneController;
import de.medieninformatik.client.model.MainModel;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Die Klasse stellt eine Stage als View und ein einheitliches Model bereit, die von den Controllern verwendet werden koennen.
 * </p>
 */
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
        //Anstatt Code zu duplizieren und hier ebenfalls mit FXMLLoader die Login Scene zu laden
        //Scene Controller auch fuer initiales laden verwenden
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
