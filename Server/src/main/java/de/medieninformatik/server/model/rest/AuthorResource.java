package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.Author;
import de.medieninformatik.common.Book;
import de.medieninformatik.server.model.database.Database;
import de.medieninformatik.server.model.parsing.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("author")
public class AuthorResource {
    RequestManager manager = Database.getRequestManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        String authors = manager.getAllAuthors();
        return Response.ok(authors).build();
    }

    @GET
    @Path("max")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMax() {
        String max = manager.getAuthorMax();
        return Response.ok(max).build();
    }
    @GET
    @Path("/{start}/{size}/{orderAsc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBySelection(@PathParam("start") int start, @PathParam("size") int size, @PathParam("orderAsc") boolean orderAsc,
                                      @QueryParam("string") String string) {
        String authors = manager.getAllAuthors(start, size, orderAsc, string);
        return Response.ok(authors).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") int id){
        String author = manager.getAuthor(id);
        return Response.ok(author).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putAuthor(String json){
        try {
            Author author = asAuthor(json);
            if (manager.putAuthor(author)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON to " + Book.class);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postAuthor(@Context UriInfo uriInfo, String json){
        try {
            Author author = asAuthor(json);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(author.getAuthorId()));
            if (manager.postAuthor(author)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e){
            System.err.println("Error parsing JSON to " + Author.class);
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") int id){
        if(manager.deleteAuthor(id)) return Response.ok().build();
        else return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    private Author asAuthor(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Author.class);
    }

}
