package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.server.model.database.Database;
import de.medieninformatik.server.model.parsing.RequestManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.sql.SQLException;
import java.util.LinkedList;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * <p>
 * 2023-01-31
 * <p>
 * Die Klasse stellt alle REST Anfragen zu Buechern bereit.
 * Buecher koennen angefragt, erstellt, veraendert oder geloescht werden.
 * Es koennen einzelne Objekte, komplette Listen oder partielle Listen angefragt werden.
 * Diese Resource benutzt primaer den BookManager zum Bearbeiten von konkreten Datenbankanfragen.
 */
@Path("book")
public class BookResource {
    RequestManager manager = RequestManager.getInstance();

    /**
     * Fragt eine Liste aller Buecher an.
     * Die Liste wird als JSON uebertragen.
     *
     * @return Antwort des Servers mit entsprechender JSON Antwort
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            LinkedList<Book> books = manager.getBookManager().getAll();
            int max = manager.getBookManager().getMax();
            DBMeta meta = manager.getBookManager().asDBMeta(books, max);
            return Response.ok(manager.asJSON(meta)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }

    /**
     * Fragt eine partielle Liste von Buechern an.
     *
     * @param start    Start der Liste
     * @param size     Groesse der Liste
     * @param orderAsc Ordnung der Liste
     * @param string   String, nach dem die Liste gefiltert werden soll
     * @param category Kategorie, nach der die Liste gefiltert werden soll
     * @return Antwort des Servers mit entsprechender JSON Antwort
     */
    @GET
    @Path("/{start}/{size}/{orderAsc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBySelection(@PathParam("start") int start, @PathParam("size") int size, @PathParam("orderAsc") boolean orderAsc,
                                      @QueryParam("string") String string, @QueryParam("category") String category) {
        try {
            LinkedList<Book> books = manager.getBookManager().getSelection(start, size, orderAsc, string, category);
            int max = manager.getBookManager().getMax(string, category);
            DBMeta meta = manager.getBookManager().asDBMeta(books, max);
            return Response.ok(manager.asJSON(meta)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }

    /**
     * Gibt ein spezifisches Buch zurueck.
     *
     * @param isbn ISBN des Buches
     * @return Antwort des Servers mit entsprechender JSON Antwort
     */
    @GET
    @Path("/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn) {
        try {
            Book book = manager.getBookManager().getItem(isbn);
            return Response.ok(manager.asJSON(book)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }

    /**
     * Gibt ein veraendertes Buch an die Datenbank weiter, um die Daten zu aktualisieren.
     *
     * @param json Json String, der das veraenderte Objekt enthaelt
     * @return Erfolgsstatus
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putBook(String json) {
        try {
            Book book = manager.JSONasObject(json, Book.class);
            if (manager.getBookManager().putItem(book)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Leitet ein zu erstellendes Buch an die Datenbank weiter.
     *
     * @param uriInfo UriInfo
     * @param json    Json String, der das neue Objekt enthaelt
     * @return Erfolgsstatus
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBook(@Context UriInfo uriInfo, String json) {
        try {
            Book book = manager.JSONasObject(json, Book.class);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(book.getIsbn()));
            if (manager.getBookManager().postItem(book)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Leitet die Datenbank an, das spezifizierte Buch zu loeschen.
     *
     * @param isbn isbn des Buches
     * @return Erfolgsstatus
     */
    @DELETE
    @Path("/{isbn}")
    public Response deleteBook(@PathParam("isbn") String isbn) {
        try {
            if (manager.getBookManager().deleteItem(isbn)) return Response.ok().build();
            else return Response.noContent().status(Response.Status.NOT_FOUND).build();
        } catch (SQLException e) {
            return Response.noContent().build();
        }
    }
}
