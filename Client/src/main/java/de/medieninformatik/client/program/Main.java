package de.medieninformatik.client.program;

import de.medieninformatik.client.model.Request;
import de.medieninformatik.client.view.MainWindow;
import javafx.application.Application;

public class Main {
    //Startet die Anwendung
    public static void main(String[] args) {
        Request.getInstance().getBookListTest(1,10,"","com","");
        Application.launch(MainWindow.class, args);
    }
}
