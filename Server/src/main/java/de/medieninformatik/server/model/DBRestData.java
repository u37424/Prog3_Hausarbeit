package de.medieninformatik.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.Publisher;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

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
    //Query handler um Anfragen der Methoden entgegenzunehmen, Daten zu holen und in Objekte zu wandeln
    private QueryToObject query = QueryToObject.getInstance();

    @GET
    @Path("book/{from}/{amount}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookList(@PathParam("from") int from, @PathParam("amount") int amount, @QueryParam("order") String order, @QueryParam("match") String match, @QueryParam("category") String category) {
        System.err.println("GET BookList from " + from + " to " + amount + " order:" + order + " match:" + match + " category:" + category);
        try {
            Book book = query.getBookList(from, amount, order, match, category);
            return sendAsJSON(book);
        } catch (RuntimeException e) {
            System.err.println("Error in Program Logic.");
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            System.err.println("Could not retrieve Information from Database.");
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("book/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn) {
        System.err.println("GET " + isbn);
        try {
            Book book = query.getAllBookData(isbn);
            return sendAsJSON(book);
        } catch (RuntimeException e) {
            System.err.println("Error in Program Logic.");
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            System.err.println("Could not retrieve Information from Database.");
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("category")
    public Response getCategories() {
        System.err.println("GET Categories");
        try {
            Category category = query.getCategoryList();
            return sendAsJSON(category);
        } catch (RuntimeException e) {
            System.err.println("Error in Program Logic.");
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            System.err.println("Could not retrieve Information from Database.");
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("publisher")
    public Response getPublishers() {
        System.err.println("GET Publishers");
        try {
            Publisher publisher = query.getPublisherList();
            return sendAsJSON(publisher);
        } catch (RuntimeException e) {
            System.err.println("Error in Program Logic.");
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            System.err.println("Could not retrieve Information from Database.");
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("author")
    public Response getAuthors() {
        System.err.println("GET Authors");
        try {
            Author author =  query.getAuthorList();
            return sendAsJSON(author);
        } catch (RuntimeException e) {
            System.err.println("Error in Program Logic.");
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            System.err.println("Could not retrieve Information from Database.");
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
    }

    private<T> Response sendAsJSON(T obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(obj);
            return Response.ok(json).build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("test")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putTest(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Category c = mapper.readValue(json, Category.class);
            //Put if exists (MyQuery->putIfExists(c))
            return Response.ok().build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
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
