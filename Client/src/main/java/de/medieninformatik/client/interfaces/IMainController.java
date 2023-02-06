package de.medieninformatik.client.interfaces;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Dieses Interface legt alle Funktionen fest, die ein Controller fuer das Hauptfenster haben soll.
 * Die Implementationen koennen in den konreten MainControllern variieren.
 * Das Interface faesst die Funktionen von normalem Benutzer und Hauptbenutzer zusammen.
 */
public interface IMainController extends IController {
    /**
     * Setzt die Optionen, die der Benutzer entsprechend zu seinem Benutzerstatus im Hauptfenster haben soll.
     */
    void setOptions();

    /**
     * Laedt die aktuellen Elemente der darzustellenden Liste und zeigt sie an.
     * Die Liste kann auch nur einen gewissen Ausschnitt einer groesseren Liste darstellen.
     */
    void loadItemList();

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer einen neuen Eintrag erstellen will.
     */
    void createItem();

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Buch" Schaltflaeche benutzt.
     */
    void bookPressed();

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Category" Schaltflaeche benutzt.
     */
    void categoryPressed();

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Author" Schaltflaeche benutzt.
     */
    void authorPressed();

    /**
     * Wird ausgefuehrt, wenn der Hauptbenutzer die "Publisher" Schaltflaeche benutzt.
     */
    void publisherPressed();

    /**
     * Veraendert die Sortierreihenfolge der Liste.
     */
    void setOrder();

    /**
     * Setzt eine neue Listengroesse der anzuzeigenden Liste.
     *
     * @param size Neue Groesse der Liste
     */
    void updatePageSize(int size);

    /**
     * Setzt einen neuen Ansichtsbereich der Liste, der vor dem aktuellen Ansichtsbereich liegt.
     */
    void pageBackward();

    /**
     * Setzt einen neuen Ansichtsbereich der Liste, der hinter dem aktuellen Ansichtsbereich liegt.
     */
    void pageForward();

    /**
     * Aktualisiert alle Parameter, die fuer das Anzeigen der Liste verwendet werden.
     * Die Filter entsprechen nach Aufruf den aktuellen Werten, die vom Benutzer eingestellt wurden.
     */
    void updateFilter();

    /**
     * Aktualisiert alle Parameter, die fuer das Anzeigen der Liste verwendet werden.
     * Die Filter entsprechen nach Aufruf den voreingestellten Werten.
     */
    void resetFilter();

    /**
     * Fuehrt alle notwendigen Aktionen aus, um es dem Benutzer zu ermoeglichen, zum Login Prozess zurueckzukehren.
     * Wenn ein moeglicher Logout nicht stattfinden kann, wird der Benutzer auch nicht abgemeldet.
     */
    void returnToLogin();

    /**
     * Leitet alle notwendigen Aktionen ein, um ein spezifisches Element aus der Liste zu betrachten.
     *
     * @param id ID des Elementes, welches betrachtet werden soll.
     */
    void inspectItem(String id);

    /**
     * Leitet alle notwendigen Aktionen ein, um ein spezifisches Element aus der Liste zu editieren.
     *
     * @param id ID des Elementes, welches editiert werden soll.
     */
    void editItem(String id);

    /**
     * Leitet alle notwendigen Aktionen ein, um ein spezifisches Element aus der Liste zu loeschen.
     *
     * @param id ID des Elementes, welches geloescht werden soll.
     */
    void deleteItem(String id);

    /**
     * Leitet einen Datenbank-Reset beim Server ein.
     */
    void resetDatabase();
}
