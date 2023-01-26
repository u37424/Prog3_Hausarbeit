package de.medieninformatik.server.model;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("user")
public class UserResource {
    private boolean loggedIn;

    @GET
    @Path("login")
    public Response getAll() {
        if (!loggedIn){
            loggedIn = true;
            return Response.ok().build();
        } else {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("logout")
    public Response getAllBySelection() {
        if (loggedIn){
            loggedIn = false;
            return Response.ok().build();
        } else {
            return Response.noContent().status(Response.Status.NOT_FOUND).build();
        }
    }

}
