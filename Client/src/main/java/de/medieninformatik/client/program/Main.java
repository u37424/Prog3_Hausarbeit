package de.medieninformatik.client.program;

import de.medieninformatik.client.model.Request;
import de.medieninformatik.client.view.MainWindow;
import javafx.application.Application;

import java.util.ResourceBundle;

public class Main {
    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("Server");
        String serverAddress = bundle.getString("ServerAddress");
        final String BASE_URI = "http://"+serverAddress+":8080/informatik";
        Request request = new Request(BASE_URI);
        request.get("/data");
        Application.launch(MainWindow.class, args);
    }
}
