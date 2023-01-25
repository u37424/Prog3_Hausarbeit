package de.medieninformatik.client.interfaces;

import de.medieninformatik.client.model.MainModel;
import javafx.stage.Stage;

public interface IController {
    void setModel(MainModel model);
    void setStage(Stage stage);
}
