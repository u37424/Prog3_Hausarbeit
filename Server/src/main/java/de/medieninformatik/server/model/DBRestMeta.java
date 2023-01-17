package de.medieninformatik.server.model;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("meta")
public class DBRestMeta {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response geMeta(){
        return Response.ok().build();
    }
}
