package de.medieninformatik.server.model.rest;

import de.medieninformatik.server.model.database.Database;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("user")
public class UserResource {
    private boolean loggedIn;

    @GET
    @Path("/login")
    public Response login() {
        if (!loggedIn) {
            loggedIn = true;
            return Response.ok().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/logout")
    public Response logout() {
        if (loggedIn) {
            loggedIn = false;
            return Response.ok().build();
        }
        return Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/reset")
    public Response resetDatabase() {
        return Database.getInstance().resetDatabase() ? Response.ok().build() :
                Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

}
