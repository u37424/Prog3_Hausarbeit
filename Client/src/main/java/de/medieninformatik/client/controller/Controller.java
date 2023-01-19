package de.medieninformatik.client.controller;

import de.medieninformatik.client.model.Model;
import de.medieninformatik.client.view.MainWindow;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Controller {

//    HBox myBox = new HBox();
//        myBox.setPadding(new Insets(5,10,5,10));
//        myBox.setPrefHeight(40);
//        myBox.setSpacing(10);
//        myBox.setAlignment(Pos.CENTER_LEFT);
//    Label label = new Label("Titel des Buches.");
//        label.setPrefSize(200,20);
//    Button b = new Button("Button yay");
//        myBox.getChildren().add(label);
//        myBox.getChildren().add(b);


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
            view.mainUser();
            view.switchToMainScene();
        } else btn.setStyle("-fx-background-color: #aa3333");
    }
}
