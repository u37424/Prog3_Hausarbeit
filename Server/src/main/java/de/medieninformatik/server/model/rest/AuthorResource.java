package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.medieninformatik.common.Author;
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
 * Die Klasse stellt alle REST Anfragen zu Autoren bereit.
 * Autoren koennen angefragt, erstellt, veraendert oder geloescht werden.
 * Es koennen einzelne Objekte, komplette Listen oder partielle Listen angefragt werden.
 * Diese resource benutzt primaer den AutorManager zum Bearbeiten von Datenbankanfragen.
 */
@Path("author")
public class AuthorResource {
    RequestManager manager = RequestManager.getInstance();

    /**
     * Fragt eine Liste aller Autoren an.
     * @return Antwort des Servers mit potentiellem JSON.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            LinkedList<Author> authors = manager.getAuthorManager().getAll();
            int max = manager.getAuthorManager().getMax();
            DBMeta meta = manager.getAuthorManager().asDBMeta(authors, max);
            return Response.ok(manager.asJSON(meta)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }

    /**
     * Fragt eine partielle Liste von Autoen an.
     * @param start Start der Liste.
     * @param size Groesse der Liste
     * @param orderAsc Ordnung der Liste
     * @param string String, nach dem die Liste gefiltert werden soll
     * @return Antwort des Servers mit potentiellem JSON.
     */
    @GET
    @Path("/{start}/{size}/{orderAsc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBySelection(@PathParam("start") int start, @PathParam("size") int size, @PathParam("orderAsc") boolean orderAsc,
                                      @QueryParam("string") String string) {
        try {
            LinkedList<Author> authors = manager.getAuthorManager().getSelection(start, size, orderAsc, string);
            int max = manager.getAuthorManager().getMax(string);
            DBMeta meta = manager.getAuthorManager().asDBMeta(authors, max);
            return Response.ok(manager.asJSON(meta)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }

    /**
     * Gibt einen spezifischen Autor zurueck
     * @param id id des Autors
     * @return Antwort des Servers mit potentiellem JSON.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") int id) {
        try {
            Author author = manager.getAuthorManager().getItem(id);
            return Response.ok(manager.asJSON(author)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }

    /**
     * Aendert die Daten eines spezifischen Autors
     * @param json Json String, der das veraenderte Objekt enthaelt
     * @return Erfolgsstatus
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putAuthor(String json) {
        try {
            Author author = manager.JSONasObject(json, Author.class);
            if (manager.getAuthorManager().putItem(author)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Erstellt einen neuen Autor
     * @param uriInfo UriInfo
     * @param json  Json String, der das neue Objekt enthaelt
     * @return Erfolgsstatus
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAuthor(@Context UriInfo uriInfo, String json) {
        try {
            Author author = manager.JSONasObject(json, Author.class);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(author.getAuthorId()));
            if (manager.getAuthorManager().postItem(author)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Loescht einen spezifischen Author
     * @param id id des Autors
     * @return Erfolgsstatus
     */
    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id) {
        try {
            if (manager.getAuthorManager().deleteItem(id)) return Response.ok().build();
            else return Response.noContent().status(Response.Status.NOT_FOUND).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }
}
