package de.medieninformatik.server.model;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("book")
public class BookResource {
    @GET
    @Path("max")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBySelection(){
        return Response.ok().build();
    }

    @GET
    @Path("{isbn}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBook(@PathParam("isbn") String isbn){
        return Response.ok().build();
    }

    @PUT
    @Path("{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putBook(@PathParam("isbn") String isbn){
        return Response.ok().build();
    }

    @POST
    @Path("{isbn}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postBook(@PathParam("isbn") String isbn){
        return Response.ok().build();
    }

    @DELETE
    @Path("{isbn}")
    public Response deleteBook(@PathParam("isbn") String isbn){
        return Response.ok().build();
    }
}
