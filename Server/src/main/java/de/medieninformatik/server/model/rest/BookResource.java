package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.Book;
import de.medieninformatik.server.model.database.Database;
import de.medieninformatik.server.model.parsing.RequestManager;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("book")
public class BookResource {
    RequestManager manager = Database.getRequestManager();

    @GET
    @Path("max")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMax() {
        String max = manager.getBookMax();
        return Response.ok(max).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        String books = manager.getAllBooks();
        return Response.ok(books).build();
    }

    @GET
    @Path("/{start}/{size}/{orderAsc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBySelection(@PathParam("start") int start, @PathParam("size") int size, @PathParam("orderAsc") boolean orderAsc,
                                      @QueryParam("string") String string, @QueryParam("category") String category) {
        String books = manager.getAllBooks(start, size, orderAsc, string, category);
        return Response.ok(books).build();
    }

    @GET
    @Path("/{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn) {
        String book = manager.getBook(isbn);
        return Response.ok(book).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putBook(String json) {
        try {
            Book book = asBook(json);
            if (manager.putBook(book)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON to " + Book.class);
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBook(@Context UriInfo uriInfo, String json) {
        try {
            Book book = asBook(json);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(book.getIsbn());
            if (manager.postBook(book)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e){
            System.err.println("Error parsing JSON to " + Book.class);
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{isbn}")
    public Response deleteBook(@PathParam("isbn") String isbn) {
        if (manager.deleteBook(isbn)) return Response.ok().build();
        else return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    private Book asBook(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Book.class);
    }
}
