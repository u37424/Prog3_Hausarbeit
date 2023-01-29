package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Publisher;
import de.medieninformatik.server.model.database.Database;
import de.medieninformatik.server.model.parsing.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("publisher")
public class PublisherResource {
    RequestManager manager = Database.getRequestManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        String publishers = manager.getAllPublishers();
        return Response.ok(publishers).build();
    }

    @GET
    @Path("/{start}/{size}/{orderAsc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBySelection(@PathParam("start") int start, @PathParam("size") int size, @PathParam("orderAsc") boolean orderAsc,
                                      @QueryParam("string") String string) {
        String publishers = manager.getAllPublishers(start, size, orderAsc, string);
        return Response.ok(publishers).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublisher(@PathParam("id") int id){
        String publisher = manager.getPublisher(id);
        return Response.ok(publisher).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPublisher(String json){
        try {
            Publisher publisher = asPublisher(json);
            if (manager.putPublisher(publisher)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON to " + Book.class);
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPublisher(@Context UriInfo uriInfo, String json){
        try {
            Publisher publisher = asPublisher(json);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(publisher.getPublisherId()));
            if (manager.postPublisher(publisher)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e){
            System.err.println("Error parsing JSON to " + Book.class);
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePublisher(@PathParam("id") int id){
        if(manager.deletePublisher(id)) return Response.ok().build();
        else return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }


    private Publisher asPublisher(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Publisher.class);
    }
}
