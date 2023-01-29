package de.medieninformatik.server.model.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.medieninformatik.common.Category;
import de.medieninformatik.common.DBMeta;
import de.medieninformatik.server.model.parsing.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("category")
public class CategoryResource {
    RequestManager manager = RequestManager.getInstance();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            DBMeta categories = manager.getCategoryManager().getAll();
            return Response.ok(manager.asJSON(categories)).build();
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
            DBMeta categories = manager.getCategoryManager().getSelection(start, size, orderAsc, string);
            return Response.ok(manager.asJSON(categories)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategory(@PathParam("id") int id) {
        try {
            Category category = manager.getCategoryManager().getItem(id);
            return Response.ok(manager.asJSON(category)).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putCategory(String json) {
        try {
            Category category = manager.asObject(json, Category.class);
            if (manager.getCategoryManager().putItem(category)) return Response.ok().build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postCategory(@Context UriInfo uriInfo, String json) {
        try {
            Category category = manager.asObject(json, Category.class);
            UriBuilder builder = uriInfo.getAbsolutePathBuilder();
            builder.path(String.valueOf(category.getCategoryId()));
            if (manager.getCategoryManager().postItem(category)) return Response.created(builder.build()).build();
        } catch (JsonProcessingException e) {
            return Response.noContent().status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") int id) {
        if (manager.getCategoryManager().deleteItem(id)) return Response.ok().build();
        else return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }
}
