package de.medieninformatik.server.model;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

//Ressourcenpfad
@Path("data")
public class DBRestData {
    //REST-Methoden: GET, PUT, POST, DELETE

    //Anfragen: Daten aus beliebigen Tabellen der DB zusammengesetzt, mit speziellen Anforderungen (match String, Order By).
    //Konkretes Buch anfragen - GET
    //Liste von Informationen zu Buechern anfragen - GET
    //Liste von Kategorien anfragen - GET
    //Liste von Autoren anfragen - GET
    //Liste von Publishern anfragen. - GET
    //Alles auch mit Spezifikationen des users (String, Category filter, Autor Filter) - GET/{}

    //Änderungsanfragen: Erstellen, Verändern mit neuen Daten, Loeschen von Buechern, Kategorien, Autoren, Publishern anhand von PRIMARY KEY
    //Erstellen eines komplett neuen Buches mit Einfuellen der Autoren und Kategorien - POST
    //Aendern eines Buches (Titel, Beschreibung, Bewertung, Jahr, Seiten, Publisher) - PUT
    //Aendern der Kategorien eines Buches, Hinzufuegen, Loeschen - PUT
    //Aendern der Autoren eines Buches, Hinzufuegen, Loeschen - PUT
    //Loeschen eines gesamten Buches - DELETE
    //Loeschen eines Publishers - DELETE
    //Loeschen eines Autors - DELETE
    //Loeschen einer Kategorie - DELETE

    //Anmelden und Abmelden beim Server als Hauptnutzer, jeder kann Hauptnutzer werden, nur ein Hauptnutzer wird verteilt und muss seine Sperre erst zurueckgeben - PUT

    @GET
    public Response getBookList() {
        return Response.ok().build();
    }
}
