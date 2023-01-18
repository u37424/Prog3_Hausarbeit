package de.medieninformatik.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.Category;
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

    //Realisierung durch verschiedene Paths
    private boolean hasMainUser;

    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest() {
        Category c = new Category(1);
        c.setName("myCategory");
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(c);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.ok(json).build();
    }

    @PUT
    @Path("test")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putTest(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Category c = mapper.readValue(json, Category.class);
            System.out.println(c);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return Response.ok().build();
    }

    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login() {
        if (hasMainUser) return Response.noContent().status(Response.Status.NOT_FOUND).build();
        else {
            hasMainUser = true;
            System.err.println("Main user logged in.");
            return Response.ok().build();
        }
    }

    @GET
    @Path("logout")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout() {
        if (!hasMainUser) return Response.noContent().status(Response.Status.NOT_FOUND).build();
        else {
            hasMainUser = false;
            System.err.println("Main user logged out.");
            return Response.ok().build();
        }
    }
}
