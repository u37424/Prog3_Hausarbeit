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
 * Die Klasse dient als Schnittstelle fuer alle Daten die zu Publishern uebertragen werden sollen.
 * Publisher, die aus der Datenbank gelesen werden, koennen hiermit auf konkrete Objekte umgewandelt werden.
 * Die Klasse bietet ausserdem die Schnittstelle fuer die Umwandlung von konkreten Publishern in JSON und zurueck.
 */
public class Publisher {
    private int publisherId;
    private String name;
    private int foundation;
    private String country;

    public Publisher() {
    }

    /**
     * Gibt die Id des Publishers zurueck
     * @return Publisher ID
     */
    @JsonGetter("publisherId")
    public int getPublisherId() {
        return publisherId;
    }

    /**
     * Setzt die Id des Publishers
     * @param publisherId Id des Publishers
     */
    @JsonSetter("publisherId")
    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    /**
     * Gibt den namen des Publishers zurueck
     * @return Publisher name
     */
    @JsonGetter("name")
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen des Publishers
     * @param name Name des Publishers
     */
    @JsonSetter("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt das Gruendungsjahr des Publishers zurueck
     * @return Publisher jahr
     */
    @JsonGetter("foundation")
    public int getFoundation() {
        return foundation;
    }

    /**
     * Setzt das Gruendungsjahr des Publishers
     * @param foundation Gruendungsjahr des Publishers
     */
    @JsonSetter("foundation")
    public void setFoundation(int foundation) {
        this.foundation = foundation;
    }

    /**
     * Gibt das land des Publishers zurueck
     * @return Publisher Land
     */
    @JsonGetter("country")
    public String getCountry() {
        return country;
    }

    /**
     * Setzt das Land des Publishers
     * @param country Land des Publishers
     */
    @JsonSetter("country")
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Wandelt den Publisher in eine ausgewaehlte String Darstellung um.
     * @return String Darstellung des Publishers.
     */
    @Override
    public String toString() {
        return name + " (" + country + ")";
    }
}
