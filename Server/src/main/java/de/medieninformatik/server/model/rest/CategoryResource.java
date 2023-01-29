package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.medieninformatik.common.Book;
import de.medieninformatik.common.Category;
import de.medieninformatik.server.model.database.Database;
import de.medieninformatik.server.model.parsing.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("category")
public class CategoryResource {
    RequestManager manager = Database.getRequestManager();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        String categories = manager.getAllCategories();
        return Response.ok(categories).build();
    }

    @GET
    @Path("/{start}/{size}/{orderAsc}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBySelection(@PathParam("start") int start, @PathParam("size") int size, @PathParam("orderAsc") boolean orderAsc,
                                      @QueryParam("string") String string) {
        String categories = manager.getAllCategories(start, size, orderAsc, string);
        return Response.ok(categories).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategory(@PathParam("id") int id){
        String category = manager.getCategory(id);
        return Response.ok(category).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putCategory(String json){
        try {
            Category category = asCategory(json);
            if (manager.putCategory(category)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            System.err.println("Error parsing JSON to " + Book.class);
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCategory(@Context UriInfo uriInfo, String json){
        try {
            Category category = asCategory(json);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(category.getCategoryId()));
            if (manager.postCategory(category)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e){
            System.err.println("Error parsing JSON to " + Category.class);
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") int id){
        if(manager.deleteCategory(id)) return Response.ok().build();
        else return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }


    private Category asCategory(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, Category.class);
    }
}
