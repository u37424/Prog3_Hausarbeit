package de.medieninformatik.client.interfaces;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Dieses Interface legt alle Funktionen fest, die ein Controller fuer die Betrachtung von spezifischen Elementen besitzen sollte.
 * Die Implementationen koennen in den konreten ViewControllern variieren.
 * Das Interface faesst die Funktionen von normalem Benutzer und Hauptbenutzer zusammen.
 * </p>
 */
public interface IViewController extends IController {
    /**
     * Setzt die Optionen, die der Benutzer entsprechend zu seinem Benutzerstatus im Betrachtungsfenster haben soll.
     */
    void setOptions();

    /**
     * Setzt die eingestellten Default-Werte auf die erhaltenden Werte des zu betrachtenden Elementes.
     */
    void displayValues();

    /**
     * Leitet alle notwendigen Aktionen ein, um den Benutzer in den entsprechenden Main Modus zu bringen.
     */
    void returnToMain();

    /**
     * Prueft alle veraenderbaren Werte des dargestellten Objektes auf Korrektheit.
     *
     * @return true, wenn alle Werte den Vorgaben entsprechend vorhanden sind
     */
    boolean validateItem();

    /**
     * Ermoeglicht es dem Hauptbenutzer den aktuellen Eintrag mit den aktuellen Werten zu speichern.
     */
    void submitChanges();

    /**
     * Ermoeglicht es dem Hauptbenutzer den aktuellen Eintrag mit den aktuellen Werten zu loeschen.
     */
    void deleteItem();
}
