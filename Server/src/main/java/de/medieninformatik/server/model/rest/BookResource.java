package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.server.model.parsing.RequestManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("book")
public class BookResource {
    RequestManager manager = RequestManager.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            DBMeta books = manager.getBookManager().getAll();
            return Response.ok(manager.asJSON(books)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{start}/{size}/{orderAsc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBySelection(@PathParam("start") int start, @PathParam("size") int size, @PathParam("orderAsc") boolean orderAsc,
                                      @QueryParam("string") String string, @QueryParam("category") String category) {
        try {
            DBMeta books = manager.getBookManager().getSelection(start, size, orderAsc, string, category);
            return Response.ok(manager.asJSON(books)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn) {
        try {
            Book book = manager.getBookManager().getItem(isbn);
            return Response.ok(manager.asJSON(book)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putBook(String json) {
        try {
            Book book = manager.asObject(json, Book.class);
            if (manager.getBookManager().putItem(book)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBook(@Context UriInfo uriInfo, String json) {
        try {
            Book book = manager.asObject(json, Book.class);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(book.getIsbn()));
            if (manager.getBookManager().postItem(book)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{isbn}")
    public Response deleteBook(@PathParam("isbn") String isbn) {
        if (manager.getBookManager().deleteItem(isbn)) return Response.ok().build();
        else return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }
}
