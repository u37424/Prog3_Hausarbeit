package de.medieninformatik.server.model.rest;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("publisher")
public class PublisherResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(){
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPublisher(@PathParam("id") int id){
        return Response.ok().build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response putPublisher(@PathParam("id") int id){
        return Response.ok().build();
    }

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postPublisher(@PathParam("id") int id){
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletePublisher(@PathParam("id") int id){
        return Response.ok().build();
    }
}
