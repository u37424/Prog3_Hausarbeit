package de.medieninformatik.client.controller;

import de.medieninformatik.client.model.Model;
import de.medieninformatik.client.view.MainWindow;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class Controller {

    private final Model model;
    private final MainWindow view;

    public Controller(MainWindow window, Model model) {
        //Kennt Model und View
        this.model = model;
        this.view = window;
    }

    public void exitProgram() {
        if(model.isMainUser()) model.logout();
        System.exit(0);
    }

    public void login(Button btn, ActionEvent e) {
        if (model.login()) {
            view.mainUserTitle();
            view.switchToMainScene();
        } else btn.setStyle("-fx-background-color: #ff0000");
    }
}
