package de.medieninformatik.client.program;

import de.medieninformatik.client.controller.Controller;
import de.medieninformatik.client.model.Model;
import de.medieninformatik.client.model.Request;
import de.medieninformatik.client.view.MainWindow;
import javafx.application.Application;

public class Main {
    //Startet die Anwendung
    public static void main(String[] args) {
        Request.getInstance().getTest();
        Request.getInstance().putTest();

        Application.launch(MainWindow.class, args);
    }
}
