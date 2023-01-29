package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.common.Publisher;
import de.medieninformatik.server.model.parsing.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("publisher")
public class PublisherResource {
    RequestManager manager = RequestManager.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            DBMeta publishers = manager.getPublisherManager().getAll();
            return Response.ok(manager.asJSON(publishers)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{start}/{size}/{orderAsc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBySelection(@PathParam("start") int start, @PathParam("size") int size, @PathParam("orderAsc") boolean orderAsc,
                                      @QueryParam("string") String string) {
        try {
            DBMeta publishers = manager.getPublisherManager().getSelection(start, size, orderAsc, string);
            return Response.ok(manager.asJSON(publishers)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublisher(@PathParam("id") int id) {
        try {
            Publisher publisher = manager.getPublisherManager().getItem(id);
            return Response.ok(manager.asJSON(publisher)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPublisher(String json) {
        try {
            Publisher publisher = manager.asObject(json, Publisher.class);
            if (manager.getPublisherManager().putItem(publisher)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPublisher(@Context UriInfo uriInfo, String json) {
        try {
            Publisher publisher = manager.asObject(json, Publisher.class);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(publisher.getPublisherId()));
            if (manager.getPublisherManager().postItem(publisher)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePublisher(@PathParam("id") int id) {
        if (manager.getPublisherManager().deleteItem(id)) return Response.ok().build();
        else return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }
}
