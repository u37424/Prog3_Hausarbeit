package de.medieninformatik.client.view;

import de.medieninformatik.client.controller.Controller;
import de.medieninformatik.client.model.Model;
import de.medieninformatik.client.program.Main;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

    private Stage stage;
    private Scene loginScene;
    private Scene mainScene;
    private Scene bookScene;
    private Controller controller;

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
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will not be
     *              primary stages.
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
        Model model = new Model();
        Controller controller = new Controller(this, model);
        this.controller = controller;
        this.stage = stage;

        Scene loginScene = createLoginScene();
        this.loginScene = loginScene;
        Scene mainScene = createMainScene();
        this.mainScene = mainScene;
        Scene bookScene = createBookScene();
        this.bookScene = bookScene;

        stage.setScene(loginScene);
        stage.setResizable(true);
        stage.setOnCloseRequest((e) -> controller.exitProgram());
        stage.setTitle("Informatik Viewer v.0.1");
        stage.show();
    }

    private Scene createLoginScene() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(10);
        box.setPrefSize(1000,800);

        Button normal = new Button("Normal");
        Button admin = new Button("Admin");

        normal.setOnAction((e) -> switchToMainScene());

        admin.setOnAction((e) -> controller.login(admin, e));

        box.getChildren().add(normal);
        box.getChildren().add(admin);
        Scene scene = new Scene(box);
        return scene;
    }

    private Scene createMainScene() {
        VBox box = new VBox();
        box.setPrefSize(1000,800);
        Scene scene = new Scene(box);
        return scene;
    }

    public void switchToMainScene() {
        this.stage.setScene(mainScene);
    }

    private Scene createBookScene() {
        VBox box = new VBox();
        box.setPrefSize(1000, 800);
        Scene scene = new Scene(box);
        return scene;
    }

    public void switchToBookScene() {
        this.stage.setScene(bookScene);
    }

    public void mainUserTitle() {
        this.stage.setTitle(stage.getTitle()+" [Hauptbenutzer]");
    }
}
