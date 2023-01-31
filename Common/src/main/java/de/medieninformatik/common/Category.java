package de.medieninformatik.common;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Die Klasse dient als Schnittstelle fuer alle Daten die zu Kategorien uebertragen werden sollen.
 * Kategorien, die aus der Datenbank gelesen werden, koennen hiermit auf konkrete Objekte umgewandelt werden.
 * Die Klasse bietet ausserdem die Schnittstelle fuer die Umwandlung von konkreten Kategorien in JSON und zurueck.
 */
public class Category {
    private int categoryId;
    private String name;

    public Category(){
    }

    /**
     * Gibt die Id der Kategorie zurueck
     * @return Id der Kategorie
     */
    @JsonGetter("categoryId")
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * Setzt die ID der kategore
     * @param categoryId ID der Kategorie
     */
    @JsonSetter("categoryId")
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Gibt den namen der Kategorie zurueck
     * @return Name der Kategorie
     */
    @JsonGetter("name")
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Kategorie
     * @param name Name der Kategorie
     */
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Wandelt die Kategorie in eine ausgewaehlte String Darstellung um.
     * @return String Darstellung der kategorie.
     */
    @Override
    public String toString() {
        return name;
    }
}
