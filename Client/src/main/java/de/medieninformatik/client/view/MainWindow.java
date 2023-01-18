package de.medieninformatik.client.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param stage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage stage) throws Exception {
        //Darstellung der Daten fuer den Client
        //Erste Scene: Anmeldung als normaler Nutzer oder Hauptbenutzer

        //Haupt-Scene:
        //Ausgewaehlte Buecherliste (Titel Jahr Autor Verlag [Kategorien] Bewertung), als Default von A-Z in Schritten (z.B. 10)
        //Filtereingabe fuer FilterString -> Threshold -> Liste wird angepasst
        //Kategorie Selection -> Submit Button -> Liste wird angepasst
        //Reset Button fuer Filter
        //Hinzufuegen Button fuer Hauptbenutzer
        //Loeschen Button fuer Hauptbenutzer
        //Eintrag bearbeiten Button fuer Hauptbenutzer

        //Buecher-View (oder Buecher Popup):
        //Titel, Autoren, Beschreibung, Bewertung, Jahr, Kategorien, Verlag
        //Kategorie Klick -> Kategorie Selection & Submit
        //Autor Klick -> Unsichtbarer Autor Filter & Submit
        //Loeschen Button fuer Hauptbenutzer
        //Eintrag bearbeiten Button fuer Hauptbenutzer

        VBox box = new VBox();
        box.setPrefSize(1000, 750);
        Scene scene = new Scene(box);
        stage.setScene(scene);
        stage.show();
    }
}
