package de.medieninformatik.server.model.rest;

import de.medieninformatik.server.model.database.Database;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

/**
 * @author Luca Spirka m29987
 * @version 1.0
 * <p>
 * Programmieren 3 - Hausarbeit.
 * </p>
 * 2023-01-31
 * <p>
 * Die Klasse stellt alle Anfragen zu generellen Benutzereinstellungen bereit.
 * Es kann nur ein Hauptbenutzer beim Server angemeldet sein.
 * Ebenso wird hier die Database Reset Funktion bereitgestellt.
 * </p>
 */
@Path("user")
public class UserResource {
    private boolean loggedIn;

    /**
     * Ermoeglicht dem Benutzer sich als Hauptbenutzer anzumelden.
     * Es kann jeweils nur ein Hauptbenutzer angemeldet sein.
     * Schlaegt fehl, wenn bereits ein Hauptbenutzer existiert.
     *
     * @return Erfolgsstatus des Logins
     */
    @GET
    @Path("/login")
    public Response login() {
        if (loggedIn) return Response.noContent().status(Response.Status.NOT_FOUND).build();
        loggedIn = true;
        return Response.ok().build();
    }

    /**
     * Ermoeglicht dem Hauptbenutzer sich abzumelden.
     * Beim erfolgreichen Abmelden wird der Hauptbenutzer wieder freigegeben.
     * Schlaegt fehl, wenn es keinen Hauptbenutzer gibt.
     *
     * @return Erfolgsstatus des Logouts
     */
    @GET
    @Path("/logout")
    public Response logout() {
        if (!loggedIn) return Response.noContent().status(Response.Status.NOT_FOUND).build();
        loggedIn = false;
        return Response.ok().build();
    }

    /**
     * Setzt die Datenbank auf Werkseinstellungen zurueck.
     *
     * @return Erfolgsstatus des Resets
     */
    @POST
    @Path("/reset")
    public Response resetDatabase() {
        return Database.getInstance().resetDatabase() ? Response.ok().build() :
                Response.noContent().status(Response.Status.NOT_FOUND).build();
    }

}
