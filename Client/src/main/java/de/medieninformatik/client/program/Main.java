package de.medieninformatik.client.program;

import de.medieninformatik.client.view.View;
import javafx.application.Application;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Die Klasse startet die JavaFX Anwendung des Klienten.
 * Als View Klasse dient die Klasse View.
 */
public class Main {
    public static void main(String[] args) {
        Application.launch(View.class, args);
    }
}
