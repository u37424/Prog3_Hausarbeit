package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.medieninformatik.common.Category;
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
 * Die Klasse stellt alle REST Anfragen zu Kategorien bereit.
 * Kategorien koennen angefragt, erstellt, veraendert oder geloescht werden.
 * Es koennen einzelne Objekte, komplette Listen oder partielle Listen angefragt werden.
 * Diese resource benutzt primaer den CategoryManager zum Bearbeiten von Datenbankanfragen.
 */
@Path("category")
public class CategoryResource {
    RequestManager manager = RequestManager.getInstance();

    /**
     * Fragt eine Liste aller Kategorien an.
     * @return Antwort des Servers mit potentiellem JSON.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            LinkedList<Category> categories = manager.getCategoryManager().getAll();
            int max = manager.getCategoryManager().getMax();
            DBMeta meta = manager.getCategoryManager().asDBMeta(categories, max);
            return Response.ok(manager.asJSON(meta)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }

    /**
     * Fragt eine partielle Liste von Kategorien an.
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
            LinkedList<Category> categories = manager.getCategoryManager().getSelection(start, size, orderAsc, string);
            int max = manager.getCategoryManager().getMax(string);
            DBMeta meta = manager.getCategoryManager().asDBMeta(categories, max);
            return Response.ok(manager.asJSON(meta)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }


    /**
     * Gibt eine spezifische Kategorie zurueck
     * @param id id der Kategorie
     * @return Antwort des Servers mit potentiellem JSON.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategory(@PathParam("id") int id) {
        try {
            Category category = manager.getCategoryManager().getItem(id);
            return Response.ok(manager.asJSON(category)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }

    /**
     * Aendert die Daten einer Kategorie
     * @param json Json String, der das veraenderte Objekt enthaelt
     * @return Erfolgsstatus
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putCategory(String json) {
        try {
            Category category = manager.JSONasObject(json, Category.class);
            if (manager.getCategoryManager().putItem(category)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Erstellt eine neue Kategorie
     * @param uriInfo UriInfo
     * @param json  Json String, der das neue Objekt enthaelt
     * @return Erfolgsstatus
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCategory(@Context UriInfo uriInfo, String json) {
        try {
            Category category = manager.JSONasObject(json, Category.class);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(category.getCategoryId()));
            if (manager.getCategoryManager().postItem(category)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Loescht eine spezifische kategorie
     * @param id id der kategorie
     * @return Erfolgsstatus
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") int id) {
        try {
            if (manager.getCategoryManager().deleteItem(id)) return Response.ok().build();
            else return Response.noContent().status(Response.Status.NOT_FOUND).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }
}
