package de.medieninformatik.client.interfaces;

import de.medieninformatik.client.controller.SceneController;
import de.medieninformatik.client.model.MainModel;
import javafx.stage.Stage;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Dieses Interface legt alle Funktionen fest, die alle davon abgeleiteten Controller besitzen sollen.
 * Dieses Interface kann benutzt werden, um die darin festgelegten Aktionen durchzufuehren, ohne den konkreten Controller zu kennen.
 * </p>
 */
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
