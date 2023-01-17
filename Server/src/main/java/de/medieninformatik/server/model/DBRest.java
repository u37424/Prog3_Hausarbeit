package de.medieninformatik.server.model;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("data")
public class DBRest {
    private static boolean mainUserLogin = false;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getBookList() {
        System.out.println("here");
        return Response.ok("hello").build();
    }

    public static boolean loginMainUser(){
        if(mainUserLogin) return false;
        else {
            mainUserLogin = true;
            return true;
        }
    }

    public static boolean logoutMainUser(){
        if(!mainUserLogin) return false;
        else {
            mainUserLogin = false;
            return true;
        }
    }
}
