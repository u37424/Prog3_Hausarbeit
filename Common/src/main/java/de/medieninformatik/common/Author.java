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
 * Die Klasse dient als Schnittstelle fuer alle Daten die zu Autoren zwischen Server und Klient uebertragen werden sollen.
 * Autoren, die aus der Datenbank gelesen werden, koennen hiermit auf konkrete Objekte umgewandelt werden.
 * Die Klasse bietet ausserdem die Schnittstelle fuer die Umwandlung von konkreten Autoren in JSON und zurueck.
 */
public class Author {
    private int authorId;
    private String firstName;
    private String lastName;
    private String alias;
    private String birthday;
    private int age;

    public Author(){
    }

    /**
     * Gibt die ID des Autors zurueck.
     * @return ID des Autors.
     */
    @JsonGetter("authorId")
    public int getAuthorId() {
        return authorId;
    }

    /**
     * Setzt die ID des Autors.
     * @param authorId ID des Autors
     */
    @JsonSetter("authorId")
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    /**
     * Gibt den Vornamen des Autors zurueck.
     * @return Vorname des Autors
     */
    @JsonGetter("firstName")
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setzt den Vornamen des Autors.
     * @param firstName Vorname des Autors
     */
    @JsonSetter("firstName")
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gibt den Nachnamen des Autors zurueck.
     * @return Nachname des Autors
     */
    @JsonGetter("lastName")
    public String getLastName() {
        return lastName;
    }

    /**
     * Setzt den Nachnamen des Autors.
     * @param lastName Nachname des Autors
     */
    @JsonSetter("lastName")
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gibt den Alias des Autors zurueck.
     * @return Alias des Autors
     */
    @JsonGetter("alias")
    public String getAlias() {
        return alias;
    }

    /**
     * Setzt den Alias des Autors.
     * @param alias Alias des Autors
     */
    @JsonSetter("alias")
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * Gibt den Geburtstag des Autors zurueck.
     * @return Geburtstag des Autors
     */
    @JsonGetter("birthday")
    public String getBirthday() {
        return birthday;
    }

    /**
     * Setzt den Geburtstag des Autors.
     * @param birthday Geburtstag des Autors
     */
    @JsonSetter("birthday")
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    /**
     * Gibt das Alter des Autors zurueck.
     * @return Alter des Autors
     */
    @JsonGetter("age")
    public int getAge() {
        return age;
    }

    /**
     * Setzt das Alter des Autors.
     * @param age Alter des Autors
     */
    @JsonSetter("age")
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Wandelt den Autor in eine ausgewaehlte String Darstellung um.
     * @return String Darstellung des Autors.
     */
    @Override
    public String toString() {
        return firstName+ " "+lastName;
    }
}
