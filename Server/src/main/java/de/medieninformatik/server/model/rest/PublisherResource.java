package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.common.Publisher;
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
 * Die Klasse stellt alle REST Anfragen zu Publishern bereit.
 * Publisher koennen angefragt, erstellt, veraendert oder geloescht werden.
 * Es koennen einzelne Objekte, komplette Listen oder partielle Listen angefragt werden.
 * Diese resource benutzt primaer den PublisherManager zum Bearbeiten von Datenbankanfragen.
 */
@Path("publisher")
public class PublisherResource {
    RequestManager manager = RequestManager.getInstance();

    /**
     * Fragt eine Liste aller Publisher an.
     * @return Antwort des Servers mit potentiellem JSON.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            LinkedList<Publisher> publishers = manager.getPublisherManager().getAll();
            int max = manager.getPublisherManager().getMax();
            DBMeta meta = manager.getPublisherManager().asDBMeta(publishers, max);
            return Response.ok(manager.asJSON(meta)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }

    /**
     * Fragt eine partielle Liste von Publishern an.
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
            LinkedList<Publisher> publishers = manager.getPublisherManager().getSelection(start, size, orderAsc, string);
            int max = manager.getPublisherManager().getMax(string);
            DBMeta meta = manager.getPublisherManager().asDBMeta(publishers, max);
            return Response.ok(manager.asJSON(meta)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }


    /**
     * Gibt einen spezifischen Publisher zurueck
     * @param id id des Publishers
     * @return Antwort des Servers mit potentiellem JSON.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublisher(@PathParam("id") int id) {
        try {
            Publisher publisher = manager.getPublisherManager().getItem(id);
            return Response.ok(manager.asJSON(publisher)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }
    /**
     * Aendert die Daten eines spezifischen Publishers
     * @param json Json String, der das veraenderte Objekt enthaelt
     * @return Erfolgsstatus
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPublisher(String json) {
        try {
            Publisher publisher = manager.JSONasObject(json, Publisher.class);
            if (manager.getPublisherManager().putItem(publisher)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Erstellt einen neuen Publisher
     * @param uriInfo UriInfo
     * @param json  Json String, der das neue Objekt enthaelt
     * @return Erfolgsstatus
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPublisher(@Context UriInfo uriInfo, String json) {
        try {
            Publisher publisher = manager.JSONasObject(json, Publisher.class);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(publisher.getPublisherId()));
            if (manager.getPublisherManager().postItem(publisher)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    /**
     * Loescht einen spezifischen Publisher
     * @param id id des Publishers
     * @return Erfolgsstatus
     */
    @DELETE
    @Path("/{id}")
    public Response deletePublisher(@PathParam("id") int id) {
        try {
            if (manager.getPublisherManager().deleteItem(id)) return Response.ok().build();
            else return Response.noContent().status(Response.Status.NOT_FOUND).build();
        } catch (SQLException e) {
            Database.getInstance().printSQLErrors(e);
            return Response.noContent().build();
        }
    }
}
