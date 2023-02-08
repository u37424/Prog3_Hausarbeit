package de.medieninformatik.client.model;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Diese Klasse implementiert ein generelles Model.
 * Das Model stellt den Zugang zu allen Daten und Funktionen fuer Buecher, Kategorien, Autoren und Publisher bereit.
 * Ebenfalls werden hier Datenunabhaengige Einstellungen gespeichert und generelle Benutzeranfragen verarbeitet.
 * </p>
 */
public class MainModel {
    private final Request request;

    //Aufteilung in weitere Klassen, die die Anfragen und Daten einzeln beantworten
    private final UserModel userModel;
    private final BookModel bookModel;
    private final CategoryModel categoryModel;
    private final AuthorModel authorModel;
    private final PublisherModel publisherModel;

    //Generelle Einstellungen werden hier im Model gespeichert, um in der gesamten Programmbenutzung konsistent zu bleiben
    private boolean mainUser;
    private boolean editMode, createMode;

    public MainModel() {
        //benutzt die Instanz einer Hilfsklasse um generelle Anfragen an den REST Server zu taetigen und Antworten zu lesen
        this.request = new Request();
        this.userModel = new UserModel(request);
        this.bookModel = new BookModel(request);
        this.categoryModel = new CategoryModel(request);
        this.authorModel = new AuthorModel(request);
        this.publisherModel = new PublisherModel(request);
    }

    /**
     * Versucht den Benutzer, als Hauptbenutzer beim Server anzumelden.
     *
     * @return true, wenn Anmeldung erfolgreich
     */
    public boolean login() {
        return mainUser = userModel.login();
    }

    /**
     * Versucht den Benutzer, als Hauptbenutzer beim Server abzumelden.
     *
     * @return true, wenn Abmeldung erfolgreich
     */
    public boolean logout() {
        return !(mainUser = !userModel.logout());
    }

    //------REQUEST HANDLER

    /**
     * Gibt die Instanz zur Bearbeitung von User-Anfragen zurueck.
     *
     * @return Instanz zur Bearbeitung von User-Anfragen
     */
    public UserModel getUserModel() {
        return userModel;
    }

    /**
     * Gibt die Instanz zur Bearbeitung von Buch-Anfragen zurueck.
     *
     * @return Instanz zur Bearbeitung von Buch-Anfragen
     */
    public BookModel getBookModel() {
        return bookModel;
    }

    /**
     * Gibt die Instanz zur Bearbeitung von Kategorie-Anfragen zurueck.
     *
     * @return Instanz zur Bearbeitung von Kategorie-Anfragen
     */
    public CategoryModel getCategoryModel() {
        return categoryModel;
    }

    /**
     * Gibt die Instanz zur Bearbeitung von Autor-Anfragen zurueck.
     *
     * @return Instanz zur Bearbeitung von Autor-Anfragen
     */
    public AuthorModel getAuthorModel() {
        return authorModel;
    }

    /**
     * Gibt die Instanz zur Bearbeitung von Publisher-Anfragen zurueck.
     *
     * @return Instanz zur Bearbeitung von Publisher-Anfragen
     */
    public PublisherModel getPublisherModel() {
        return publisherModel;
    }

    //-----------------USER SETTINGS

    /**
     * Gibt auskunft ueber den Hauptbenutzerstatus des Benutzers.
     *
     * @return true, wenn Hauptbenutzer
     */
    public boolean isMainUser() {
        return mainUser;
    }

    /**
     * Gibt auskunft ueber den Edit-Status des Benutzers.
     *
     * @return true, wenn der Benutzer sich im Edit Modus befindet
     */
    public boolean isEditMode() {
        return editMode;
    }

    /**
     * Gibt auskunft ueber den Create-Status des Benutzers.
     *
     * @return true, wenn der Benutzer sich im Create Modus befindet
     */
    public boolean isCreateMode() {
        return createMode;
    }

    /**
     * Setzt den Edit-Status des Benutzers.
     *
     * @param editMode neuer Edit-Status des Benutzers
     */
    public void setEditMode(boolean editMode) {
        this.editMode = editMode;   //Set Edit Status
    }

    /**
     * Setzt den Crate-Status des Benutzers.
     *
     * @param createMode neuer Create-Status des Benutzers
     */
    public void setCreateMode(boolean createMode) {
        this.editMode = createMode;
        this.createMode = createMode; //Set Create Status
    }
}
