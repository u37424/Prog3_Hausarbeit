package de.medieninformatik.client.interfaces;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Dieses Interface legt alle Funktionen fest, die ein Controller fuer den Login Prozess besitzen sollte.
 */
public interface ILoginController extends IController {
    /**
     * Leitet den Benutzer im normalen Modus weiter.
     * Alle Einstellungen werden auf den Normalen Modus angepasst.
     */
    void loginNormalUser();

    /**
     * Versucht eine Anmeldung als Hauptbenutzer durchzufuehren.
     * Wenn der Benutzer sich erfolgreich anmelden konnte, wird er im Hauptbenutzer Modus weitergeleitet.
     * Alle Einstellungen werden auf den Hauptbenutzerstatus angepasst.
     * Kann sich der Benutzer nicht anmelden, werden alle weiteren Einstellungen abgebrochen.
     */
    void loginMainUser();
}
