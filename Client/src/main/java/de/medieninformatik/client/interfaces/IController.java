package de.medieninformatik.client.interfaces;

import de.medieninformatik.client.controller.SceneController;
import de.medieninformatik.client.model.MainModel;
import javafx.stage.Stage;

public interface IController {
    void setup();
    void initialize();
    void setModel(MainModel model);
    void setStage(Stage stage);
    void setSceneController(SceneController sceneController);
}
