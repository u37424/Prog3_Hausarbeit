package de.medieninformatik.client.interfaces;

import de.medieninformatik.client.controller.SceneController;
import de.medieninformatik.client.model.MainModel;
import javafx.stage.Stage;

public interface IController {
    /**
     * Wird beim Laden der FXML Datei aufgerufen, um initiale Einstellungen beim Laden des Controllers zu machen.
     */
    void initialize();

    /**
     * Setzt das entsprechende Model, welches vom Controller verwendet werden soll.
     *
     * @param model Model, welches verwendet werden soll
     */
    void setModel(MainModel model);

    /**
     * Setzt die entsprechende Stage, welche vom Controller als View verwendet werden soll.
     *
     * @param stage Stage, welche verwendet werden soll
     */
    void setStage(Stage stage);

    /**
     * Setzt die Instanz des entsprechenden SceneControllers.
     *
     * @param sceneController Instanz des SceneControllers
     */
    void setSceneController(SceneController sceneController);
}
